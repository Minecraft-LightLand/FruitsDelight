package dev.xkmc.fruitsdelight.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BaseCakeBlock extends Block {

	public final IntegerProperty bite;
	public final int maxBite;

	public BaseCakeBlock(Properties properties, IntegerProperty bite, int max) {
		super(properties);
		this.bite = bite;
		this.maxBite = max;
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(bite, max)
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	protected abstract void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);

	protected abstract void eat(Player player);

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (level.isClientSide) {
			if (eat(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}

			if (itemstack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}
		return eat(level, pos, state, player);
	}

	protected InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
		if (!player.canEat(false)) {
			return InteractionResult.PASS;
		} else {
			int i = state.getValue(bite);
			if (i > 0) {
				eat(player);
				level.gameEvent(player, GameEvent.EAT, pos);
				level.setBlockAndUpdate(pos, state.setValue(bite, i - 1));
			} else {
				level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
				level.destroyBlock(pos, true);
			}
			return InteractionResult.SUCCESS;
		}
	}

	public BlockState updateShape(BlockState state, Direction direction, BlockState other, LevelAccessor level,
								  BlockPos selfPos, BlockPos otherPos) {
		return direction == Direction.DOWN && !state.canSurvive(level, selfPos) ? Blocks.AIR.defaultBlockState() :
				super.updateShape(state, direction, other, level, selfPos, otherPos);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).getMaterial().isSolid();
	}

	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return state.getValue(bite) * 2;
	}

	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

}