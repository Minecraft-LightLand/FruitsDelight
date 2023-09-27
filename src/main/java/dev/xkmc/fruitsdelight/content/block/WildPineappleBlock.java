package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.TagGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WildPineappleBlock extends BushBlock {

	public WildPineappleBlock(Properties properties) {
		super(properties);
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(TagGen.PINEAPPLE_GROW);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(TagGen.PINEAPPLE_GROW);
	}

}
