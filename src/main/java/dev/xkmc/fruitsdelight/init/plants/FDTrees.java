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
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
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

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FDTrees implements PlantDataEntry<FDTrees> {
	PEAR(() -> Blocks.BIRCH_LOG, FDTreeType.TALL, 3, 0.3f, false, 15),
	HAWBERRY(() -> Blocks.SPRUCE_LOG, FDTreeType.TALL, 2, 0.3f, true, 20),
	LYCHEE(() -> Blocks.JUNGLE_LOG, FDTreeType.TALL, 2, 0.3f, true, 4),
	MANGO(() -> Blocks.JUNGLE_LOG, FDTreeType.NORMAL, 3, 0.3f, false, 4),
	PERSIMMON(() -> Blocks.SPRUCE_LOG, FDTreeType.FANCY, 3, 0.3f, false, 50),
	PEACH(() -> Blocks.JUNGLE_LOG, FDTreeType.NORMAL, 3, 0.3f, false, 8),
	ORANGE(() -> Blocks.OAK_LOG, FDTreeType.NORMAL, 3, 0.3f, false, 20),
	APPLE(() -> Blocks.OAK_LOG, FDTreeType.NORMAL, str -> () -> Items.APPLE, 20),
	MANGOSTEEN(() -> Blocks.OAK_LOG, FDTreeType.FANCY, 3, 0.3f, false, 50),
	BAYBERRY(() -> Blocks.SPRUCE_LOG, FDTreeType.TALL, 2, 0.3f, true, 15),
	KIWI(() -> Blocks.JUNGLE_LOG, FDTreeType.NORMAL, 3, 0.3f, true, 20),
	FIG(() -> Blocks.OAK_LOG, FDTreeType.NORMAL, 3, 0.3f, false, 20),
	;

	private final BlockEntry<PassableLeavesBlock> leaves;
	private final BlockEntry<SaplingBlock> sapling;
	private final Supplier<Item> fruit;
	private final Lazy<TreeConfiguration> treeConfig, treeConfigWild;

	public final ResourceLocation configKey, configKeyWild;
	public final ResourceLocation placementKey;

	public final Supplier<Block> log;
	private final int spawn;
	public boolean genTree = false;

	FDTrees(Supplier<Block> log, FDTreeType height, Function<String, Supplier<Item>> items, int spawn) {
		this.spawn = spawn;
		String name = name().toLowerCase(Locale.ROOT);
		this.log = log;
		this.treeConfig = Lazy.of(() -> buildTreeConfig(log, height, false));
		this.treeConfigWild = Lazy.of(() -> buildTreeConfig(log, height, true));
		this.configKey = new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree");
		this.configKeyWild = new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree_wild");
		this.placementKey = new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree");

		leaves = height.buildLeave(name, this);
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

		fruit = items.apply(name);
	}

	FDTrees(Supplier<Block> log, FDTreeType height, int food, float sat, boolean fast, int spawn) {
		this(log, height, name -> FruitsDelight.REGISTRATE
				.item(name, p -> new Item(p.food(food(food, sat, fast))))
				.transform(b -> PlantDataEntry.addFruitTags(name, b))
				.register(), spawn);
		genTree = true;
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

	public void registerComposter() {
		ComposterBlock.COMPOSTABLES.put(getFruit(), 0.65f);
		ComposterBlock.COMPOSTABLES.put(getLeaves().asItem(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(getSapling().asItem(), 0.3f);
	}


	private Holder<ConfiguredFeature<TreeConfiguration, ?>> treeCF, wildCF;
	private Holder<PlacedFeature> wildPF;

	public void registerConfigs() {
		treeCF = FeatureUtils.register(configKey.toString(), Feature.TREE, treeConfig.get());
		wildCF = FeatureUtils.register(configKeyWild.toString(), Feature.TREE, treeConfigWild.get());
	}

	public void registerPlacements() {
		wildPF = PlacementUtils.register(placementKey.toString(), wildCF, VegetationPlacements.treePlacement(
				PlacementUtils.countExtra(0,  1f / (spawn + 3e-6f), 1), getSapling()));
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public Holder<PlacedFeature> getPlacementKey() {
		return wildPF;
	}

	private TreeConfiguration buildTreeConfig(Supplier<Block> log, FDTreeType height, boolean wild) {
		return height.build(log.get(), getLeaves(), wild);
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

		@Override
		protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean large) {
			return treeCF;
		}

	}

}
