package dev.xkmc.fruitsdelight.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class FruitBushBlock extends BaseBushBlock {

	private static final VoxelShape SMALL = Shapes.or(
			Block.box(4.0D, 3.0D, 4.0D, 12.0D, 10.0D, 12.0D),
			Block.box(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D)
	);

	private static final VoxelShape SHAPE = Shapes.or(
			Block.box(2.0D, 3.0D, 2.0D, 14.0D, 14.0D, 14.0D),
			Block.box(6.0D, 0.0D, 6.0D, 10.0D, 3.0D, 10.0D)
	);

	private static final VoxelShape TALL_SMALL = Shapes.or(
			Block.box(4.0D, 3.0D, 4.0D, 12.0D, 11.0D, 12.0D),
			Block.box(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D)
	);

	private static final VoxelShape TALL_MID = Shapes.or(
			Block.box(0.0D, 6.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D)
	);

	private static final VoxelShape TALL_SHAPE = Shapes.or(
			Block.box(0.0D, 12.0D, 0.0D, 16.0D, 24.0D, 16.0D),
			Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D)
	);

	private final Supplier<Item> item;
	private final boolean hasIntermediate;

	public FruitBushBlock(BlockBehaviour.Properties properties, Supplier<Item> item, boolean hasIntermediate) {
		super(properties);
		this.item = item;
		this.hasIntermediate = hasIntermediate;
		registerDefaultState(defaultBlockState().setValue(AGE, 2));
	}

	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		int i = state.getValue(AGE);
		boolean flag = i == 4;
		if (!flag && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		} else if (flag) {
			int j = 1 + level.random.nextInt(2);
			popResource(level, pos, new ItemStack(item.get(), j));
			level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
			BlockState blockstate = state.setValue(AGE, 2);
			level.setBlock(pos, blockstate, 2);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.use(state, level, pos, player, hand, result);
		}
	}

	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int age = state.getValue(AGE);
		if (hasIntermediate) {
			return age == 0 ? TALL_SMALL : age == 1 ? TALL_MID : TALL_SHAPE;
		}
		return age <= 1 ? SMALL : SHAPE;
	}

	@Override
	public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
		return hasIntermediate && state.getValue(AGE) >= 2;
	}

}
