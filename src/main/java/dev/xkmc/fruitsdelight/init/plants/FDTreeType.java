package dev.xkmc.fruitsdelight.init.plants;

import dev.xkmc.fruitsdelight.content.block.PassableLeavesBlock;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public enum FDTreeType {
	NORMAL(5, 2, 0, 3),
	TALL(6, 3, 3, 5),
	;

	private final int height, hra, hrb, leaveHeight;

	FDTreeType(int height, int hra, int hrb, int leaveHeight) {
		this.height = height;
		this.hra = hra;
		this.hrb = hrb;
		this.leaveHeight = leaveHeight;
	}

	public TreeConfiguration build(Block log, PassableLeavesBlock leaves, boolean wild) {
		int flowers = wild ? 10 : 30;
		return new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(log),
				new StraightTrunkPlacer(height, hra, hrb),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(leaves.defaultBlockState(), 100 - flowers)
						.add(leaves.defaultBlockState().setValue(PassableLeavesBlock.STATE, PassableLeavesBlock.State.FLOWERS), flowers)
						.build()),
				new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), leaveHeight),
				new TwoLayersFeatureSize(1, 0, 1))
				.ignoreVines().build();
	}
}
