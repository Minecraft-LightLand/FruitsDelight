package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.TagGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class PineappleBlock extends BaseBushBlock {

	public PineappleBlock(Properties properties) {
		super(properties);
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(TagGen.PINEAPPLE_GROW);
	}

}
