package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.block.PassableLeavesBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.FDDatapackRegistriesGen;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum FDTrees implements FDDatapackRegistriesGen.SpawnModifier {
	PEAR(() -> Blocks.BIRCH_LOG, 5, 30, 3, 0.3f, false),
	HAWBERRY(() -> Blocks.SPRUCE_LOG, 5, 30, 1, 0.5f, true),
	LYCHEE(() -> Blocks.JUNGLE_LOG, 5, 30, 2, 0.3f, true),
	MANGO(() -> Blocks.JUNGLE_LOG, 5, 30, 3, 0.5f, false),
	PERSIMMON(() -> Blocks.SPRUCE_LOG, 5, 30, 3, 0.5f, false);

	private final BlockEntry<PassableLeavesBlock> leaves;
	private final BlockEntry<SaplingBlock> sapling;
	private final ItemEntry<Item> fruit;
	private final Lazy<TreeConfiguration> treeConfig;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	FDTrees(Supplier<Block> log, int height, int flowers, int food, float sat, boolean fast) {
		String name = name().toLowerCase(Locale.ROOT);
		this.treeConfig = Lazy.of(() -> buildTreeConfig(log, height, flowers));
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree"));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree"));

		leaves = FruitsDelight.REGISTRATE
				.block(name + "_leaves", p -> new PassableLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)))
				.blockstate(this::buildLeavesModel)
				.loot((pvd, block) -> buildFruit(pvd, block, getSapling(), getFruit()))
				.tag(BlockTags.LEAVES)
				.item().tag(ItemTags.LEAVES).build()
				.register();

		sapling = FruitsDelight.REGISTRATE.block(
						name + "_sapling", p -> new SaplingBlock(new TreeGrower(),
								BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)
						))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
						.cross(ctx.getName(), pvd.modLoc("block/" + ctx.getName()))
						.renderType("cutout")))
				.tag(BlockTags.SAPLINGS)
				.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/" + ctx.getName())))
				.tag(ItemTags.SAPLINGS).build()
				.register();

		fruit = FruitsDelight.REGISTRATE
				.item(name, p -> new Item(p.food(food(food, sat, fast))))
				.register();

	}

	public PassableLeavesBlock getLeaves() {
		return leaves.get();
	}

	public Item getFruit() {
		return fruit.get();
	}

	public SaplingBlock getSapling() {
		return sapling.get();
	}

	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		ctx.register(configKey, new ConfiguredFeature<>(Feature.TREE, treeConfig.get()));
	}

	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		ctx.register(placementKey, new PlacedFeature(
				ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKey),
				List.of(PlacementUtils.filteredByBlockSurvival(getSapling()))));
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
	}

	private TreeConfiguration buildTreeConfig(Supplier<Block> log, int height, int flowers) {
		return new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(log.get()),
				new StraightTrunkPlacer(height, 2, 0),
				new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
						.add(getLeaves().defaultBlockState(), 100 - flowers)
						.add(getLeaves().defaultBlockState().setValue(PassableLeavesBlock.STATE, PassableLeavesBlock.State.FLOWERS), flowers)
						.build()),
				new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
				new TwoLayersFeatureSize(1, 0, 1))
				.ignoreVines().build();
	}

	private void buildLeavesModel(DataGenContext<Block, PassableLeavesBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> {
							String name = name().toLowerCase(Locale.ROOT) + "_" +
									state.getValue(PassableLeavesBlock.STATE).getSerializedName();
							return ConfiguredModel.builder()
									.modelFile(pvd.models().withExistingParent(name, "block/leaves")
											.texture("all", "block/" + name)).build();
						},
						LeavesBlock.DISTANCE, LeavesBlock.PERSISTENT, LeavesBlock.WATERLOGGED);
	}

	private static void buildFruit(RegistrateBlockLootTables pvd, Block block, Block sapling, Item fruit) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(
				AlternativesEntry.alternatives(
						LootItem.lootTableItem(block)
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
										.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
												MinMaxBounds.Ints.atLeast(1))))),
						LootItem.lootTableItem(fruit)
								.when(LootItemBlockStatePropertyCondition
										.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties()
												.hasProperty(PassableLeavesBlock.STATE, PassableLeavesBlock.State.FRUITS)))
								.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)),
						LootItem.lootTableItem(sapling)
								.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
										1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f))
				)
		).when(ExplosionCondition.survivesExplosion())));
	}

	private static FoodProperties food(int food, float sat, boolean fast) {
		var ans = new FoodProperties.Builder()
				.nutrition(food).saturationMod(sat);
		if (fast) ans.fast();
		return ans.build();
	}

	public static void register() {
	}

	private class TreeGrower extends AbstractTreeGrower {

		@Nullable
		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean large) {
			return configKey;
		}

	}

}
