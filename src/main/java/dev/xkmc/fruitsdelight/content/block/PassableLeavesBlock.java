package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import java.util.Locale;

public class PassableLeavesBlock extends LeavesBlock {

	public enum State implements StringRepresentable {
		LEAVES, FLOWERS, FRUITS;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public static final EnumProperty<State> STATE = EnumProperty.create("type", State.class);

	public PassableLeavesBlock(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(STATE) == State.FRUITS) {
			if (level instanceof ServerLevel sl) {
				dropFruit(state, sl, pos, level.getRandom());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(STATE);
	}

	private void dropFruit(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		dropResources(state, level, pos);
		State st = random.nextDouble() < FDModConfig.COMMON.flowerDecayChance.get() ? State.LEAVES : State.FLOWERS;
		level.setBlockAndUpdate(pos, state.setValue(STATE, st));
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(STATE) != State.LEAVES || super.isRandomlyTicking(state);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!decaying(state)) {
			State st = state.getValue(STATE);
			if (st == State.FLOWERS) {
				boolean grow = random.nextDouble() < FDModConfig.COMMON.fruitsGrowChance.get();
				if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
					level.setBlockAndUpdate(pos, state.setValue(STATE, State.FRUITS));
					ForgeHooks.onCropsGrowPost(level, pos, state);
					return;
				}
			}
			if (st == State.FRUITS) {
				if (random.nextDouble() < FDModConfig.COMMON.fruitsDropChance.get()) {
					dropFruit(state, level, pos, random);
					return;
				}
			}
		}
		super.randomTick(state, level, pos, random);
	}

	@Deprecated
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		if (ctx instanceof EntityCollisionContext ectx && ectx.getEntity() instanceof ItemEntity) {
			return Shapes.empty();
		}
		return super.getCollisionShape(state, level, pos, ctx);
	}

}
