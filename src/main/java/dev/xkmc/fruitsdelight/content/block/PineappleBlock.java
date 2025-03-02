package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PineappleBlock extends BaseBushBlock implements HarvestableBlock {

	public PineappleBlock(Properties properties) {
		super(properties);
	}

	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(TagGen.PINEAPPLE_GROW);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).is(TagGen.PINEAPPLE_GROW);
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		int age = state.getValue(AGE);
		if (age < MAX_AGE) return null;
		return new HarvestResult(Blocks.AIR.defaultBlockState(), List.of(
				FDPineapple.PINEAPPLE.getWholeFruit().getDefaultInstance()
		));
	}

}
