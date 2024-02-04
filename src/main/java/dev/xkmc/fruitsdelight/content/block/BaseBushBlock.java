package dev.xkmc.fruitsdelight.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;

public class BaseBushBlock extends BushBlock implements BonemealableBlock {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_4;

	public BaseBushBlock(Properties properties) {
		super(properties);
	}

	public boolean isRandomlyTicking(BlockState state) {
		return state.getValue(AGE) < 4;
	}

	@Deprecated
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		int i = state.getValue(AGE);
		if (i < 4 && level.getRawBrightness(pos.above(), 0) >= 9 &&
				ForgeHooks.onCropsGrowPre(level, pos, state, rand.nextInt(5) == 0)) {
			BlockState blockstate = state.setValue(AGE, i + 1);
			level.setBlock(pos, blockstate, 2);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
			ForgeHooks.onCropsGrowPost(level, pos, state);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
		super.createBlockStateDefinition(builder);
	}

	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) {
		return state.getValue(AGE) < 4;
	}

	public boolean isBonemealSuccess(Level level, RandomSource rand, BlockPos pos, BlockState state) {
		return true;
	}

	public void performBonemeal(ServerLevel level, RandomSource rand, BlockPos pos, BlockState state) {
		int i = Math.min(4, state.getValue(AGE) + 1);
		level.setBlock(pos, state.setValue(AGE, i), 2);
	}

}
