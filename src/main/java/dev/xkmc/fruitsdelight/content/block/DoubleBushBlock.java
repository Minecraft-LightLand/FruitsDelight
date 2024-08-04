package dev.xkmc.fruitsdelight.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.CommonHooks;

import javax.annotation.Nullable;

public abstract class DoubleBushBlock extends DoublePlantBlock implements BonemealableBlock {

	public DoubleBushBlock(Properties pProperties) {
		super(pProperties);
	}

	public static final IntegerProperty AGE = BlockStateProperties.AGE_4;
	public static final int MAX_AGE = 4;

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
		super.createBlockStateDefinition(builder);
	}

	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) {
		return state.getValue(AGE) < MAX_AGE;
	}

	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	public abstract int getDoubleBlockStart();

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(AGE) < MAX_AGE;
	}

	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		int i = state.getValue(AGE);
		if (i < MAX_AGE && level.getRawBrightness(pos.above(), 0) >= 9 &&
				CommonHooks.canCropGrow(level, pos, state, rand.nextInt(5) == 0)) {
			setGrowth(level, pos, i + 1, 2);
			CommonHooks.fireCropGrowPost(level, pos, state);
		}
	}

	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			var lower = level.getBlockState(pos.below());
			if (lower.is(this) && lower.getValue(HALF) == DoubleBlockHalf.LOWER) {
				pos = pos.below();
				state = lower;
			} else return;
		}
		int i = Math.min(MAX_AGE, state.getValue(AGE) + 1);
		setGrowth(level, pos, i, 2);
	}

	public void setGrowth(Level level, BlockPos pos, int age, int flag) {
		if (age >= getDoubleBlockStart()) {
			boolean fail = level.isOutsideBuildHeight(pos.above());
			if (!fail) {
				var above = level.getBlockState(pos.above());
				fail = !above.is(this) && !above.canBeReplaced();
			}
			if (fail) {
				age = getDoubleBlockStart() - 1;
				if (age < 0) return;
			}
		}
		BlockState state = defaultBlockState().setValue(AGE, age);
		level.setBlock(pos, state, flag);
		if (age >= getDoubleBlockStart()) {
			level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), flag);
		} else {
			var above = level.getBlockState(pos.above());
			if (above.is(this) && above.getValue(HALF) == DoubleBlockHalf.UPPER) {
				level.removeBlock(pos.above(), false);
			}
		}
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
	}

	public BlockState updateShape(BlockState state, Direction dir, BlockState sourceState, LevelAccessor level, BlockPos pos, BlockPos sourcePos) {
		DoubleBlockHalf half = state.getValue(HALF);
		if (half == DoubleBlockHalf.LOWER && dir == Direction.DOWN && !state.canSurvive(level, pos))
			return Blocks.AIR.defaultBlockState();
		if (dir.getAxis() == Direction.Axis.Y) {
			boolean illegal = !sourceState.is(this) || sourceState.getValue(HALF) == half;
			if (half == DoubleBlockHalf.UPPER && dir == Direction.DOWN && illegal) {
				return Blocks.AIR.defaultBlockState();
			}
			if (half == DoubleBlockHalf.LOWER && dir == Direction.UP && illegal) {
				if (state.getValue(AGE) >= getDoubleBlockStart())
					return Blocks.AIR.defaultBlockState();
			}
		}
		return super.updateShape(state, dir, sourceState, level, pos, sourcePos);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos blockpos = ctx.getClickedPos();
		Level level = ctx.getLevel();
		return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(ctx) ?
				super.getStateForPlacement(ctx) : null;
	}

	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
	}

}
