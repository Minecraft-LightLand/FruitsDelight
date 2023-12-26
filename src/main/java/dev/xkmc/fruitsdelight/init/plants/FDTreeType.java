package dev.xkmc.fruitsdelight.init.plants;

import dev.xkmc.fruitsdelight.content.block.PassableLeavesBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

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

	public BlockEntry<PassableLeavesBlock> buildLeave(String name, FDTrees tree) {
		return FruitsDelight.REGISTRATE
				.block(name + "_leaves", p -> new PassableLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))
				.blockstate((ctx, pvd) -> buildLeavesModel(ctx, pvd, name))
				.loot((pvd, block) -> buildFruit(pvd, block, tree.getSapling(), tree.getFruit()))
				.tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
				.item().tag(ItemTags.LEAVES).build()
				.register();
	}

	private void buildLeavesModel(DataGenContext<Block, PassableLeavesBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> ctx.get().buildModel(pvd, name, state),
						LeavesBlock.DISTANCE, LeavesBlock.PERSISTENT, LeavesBlock.WATERLOGGED);
	}

	private void buildFruit(RegistrateBlockLootTables pvd, Block block, Block sapling, Item fruit) {
		var leaves = LootItem.lootTableItem(block)
				.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
						.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
								MinMaxBounds.Ints.atLeast(1)))));
		var fruits = LootItem.lootTableItem(fruit)
				.when(LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PassableLeavesBlock.STATE, PassableLeavesBlock.State.FRUITS)))
				.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1));
		var saplings = LootItem.lootTableItem(sapling)
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
						1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f));
		var drops = AlternativesEntry.alternatives(leaves, fruits, saplings);

		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}


}
