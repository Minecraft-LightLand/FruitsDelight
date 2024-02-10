package dev.xkmc.fruitsdelight.init.plants;

import dev.xkmc.fruitsdelight.content.block.BaseLeavesBlock;
import dev.xkmc.fruitsdelight.content.block.DurianLeavesBlock;
import dev.xkmc.fruitsdelight.content.block.PassableLeavesBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.BlockBuilder;
import dev.xkmc.l2library.repack.registrate.builders.ItemBuilder;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraftforge.common.util.NonNullFunction;

import java.util.OptionalInt;
import java.util.function.Supplier;

public enum FDTreeType {
	NORMAL(10, 30, PassableLeavesBlock::new, () -> new StraightTrunkPlacer(5, 2, 0),
			() -> new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3)),

	TALL(7, 21, PassableLeavesBlock::new, () -> new StraightTrunkPlacer(5, 2, 0),
			() -> new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 4)),
	FANCY(2, 6, PassableLeavesBlock::new, () -> new FancyTrunkPlacer(6, 11, 3),
			() -> new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4)),
	DURIAN(2, 6, DurianLeavesBlock::new, () -> new FancyTrunkPlacer(6, 11, 3),
			() -> new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4)),
	;


	private final int flowerWild, flowerSapling;
	private final NonNullFunction<BlockBehaviour.Properties, BaseLeavesBlock> block;
	private final Supplier<TrunkPlacer> trunk;
	private final Supplier<FoliagePlacer> foliage;

	FDTreeType(int flowerWild, int flowerSapling, NonNullFunction<BlockBehaviour.Properties, BaseLeavesBlock> block, Supplier<TrunkPlacer> trunk, Supplier<FoliagePlacer> foliage) {
		this.flowerWild = flowerWild;
		this.flowerSapling = flowerSapling;
		this.block = block;
		this.trunk = trunk;
		this.foliage = foliage;
	}

	public TreeConfiguration build(Block log, BaseLeavesBlock leaves, boolean wild) {
		int flowers = wild ? flowerWild : flowerSapling;
		var leaf = leaves.defaultBlockState();
		var flower = leaves.flowerState();
		return new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(log), trunk.get(),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(leaf, 100 - flowers).add(flower, flowers).build()),
				foliage.get(),
				new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))
				.ignoreVines().build();
	}

	public BlockEntry<? extends BaseLeavesBlock> buildLeave(String name, FDTrees tree) {
		return FruitsDelight.REGISTRATE
				.block(name + "_leaves", p -> block.apply(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))
				.blockstate((ctx, pvd) -> ctx.get().buildLeavesModel(ctx, pvd, name))
				.loot((pvd, block) -> block.buildLoot(pvd, block, tree.getSapling(), tree.getFruit()))
				.tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
				.item().tag(ItemTags.LEAVES).build()
				.register();
	}

	public ItemBuilder<? extends BlockItem, BlockBuilder<SaplingBlock, L2Registrate>> sapling(
			BlockBuilder<SaplingBlock, L2Registrate> builder) {
		if (this == DURIAN) return Durian.sapling(builder);
		return builder.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/" + ctx.getName())));
	}
}
