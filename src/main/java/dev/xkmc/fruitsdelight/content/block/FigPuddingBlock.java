package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.l2modularblock.core.VoxelBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.CommonTags;

public class FigPuddingBlock extends BaseCakeBlock {

	public static final int MAX_BITES = 3;
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, MAX_BITES);
	protected static final VoxelShape[][] SHAPES = new VoxelShape[4][4];

	static {
		var root = new VoxelBuilder(8, 0, 8, 13, 6, 13);
		var peak = new VoxelBuilder(8, 6, 8, 10, 8, 10);
		var complete = Block.box(3, 0, 3, 13, 6, 13);
		for (var dir : Direction.values()) {
			if (dir.getAxis() == Direction.Axis.Y) continue;
			for (int bite = 0; bite < 4; bite++) {
				SHAPES[dir.get2DDataValue()][bite] = switch (bite) {
					case 0 -> Shapes.or(
							peak.rotateFromNorth(dir),
							root.rotateFromNorth(dir)
					);
					case 1 -> Shapes.or(
							peak.rotateFromNorth(dir),
							root.rotateFromNorth(dir),
							root.rotateFromNorth(dir.getClockWise())
					);
					case 2 -> Shapes.or(
							peak.rotateFromNorth(dir),
							root.rotateFromNorth(dir),
							root.rotateFromNorth(dir.getClockWise()),
							root.rotateFromNorth(dir.getCounterClockWise())
					);
					default -> Shapes.or(
							peak.rotateFromNorth(dir),
							complete
					);
				};
			}
		}
	}

	public FigPuddingBlock(Properties properties) {
		super(properties, BITES, MAX_BITES, false);
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int bite = state.getValue(BITES);
		var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		return SHAPES[dir.get2DDataValue()][bite];
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (stack.is(CommonTags.TOOLS_KNIFE)) {
			int i = state.getValue(bite);
			if (i > 0) {
				level.setBlockAndUpdate(pos, state.setValue(bite, i - 1));
			} else {
				level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
				level.destroyBlock(pos, true);
			}
			popResource(level, pos, FDFood.FIG_PUDDING_SLICE.get().getDefaultInstance());
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	protected void eat(Player player) {
		player.eat(player.level(), FDFood.FIG_PUDDING_SLICE.item.asStack());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, BITES);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING,
				pContext.getHorizontalDirection().getOpposite());
	}

}