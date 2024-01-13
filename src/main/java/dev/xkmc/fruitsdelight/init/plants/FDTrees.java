package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.PassableLeavesBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
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
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FDTrees implements PlantDataEntry<FDTrees> {
	PEAR(() -> Blocks.BIRCH_LOG, FDTreeType.TALL, 3, 0.3f, false),
	HAWBERRY(() -> Blocks.SPRUCE_LOG, FDTreeType.TALL, 2, 0.3f, true),
	LYCHEE(() -> Blocks.JUNGLE_LOG, FDTreeType.TALL, 2, 0.3f, true),
	MANGO(() -> Blocks.JUNGLE_LOG, FDTreeType.NORMAL, 3, 0.3f, false),
	PERSIMMON(() -> Blocks.SPRUCE_LOG, FDTreeType.FANCY, 3, 0.3f, false),
	PEACH(() -> Blocks.JUNGLE_LOG, FDTreeType.NORMAL, 3, 0.3f, false),
	ORANGE(() -> Blocks.OAK_LOG, FDTreeType.NORMAL, 3, 0.3f, false),
	APPLE(() -> Blocks.OAK_LOG, FDTreeType.NORMAL, str -> () -> Items.APPLE),
	MANGOSTEEN(() -> Blocks.OAK_LOG, FDTreeType.FANCY, 3, 0.3f, false),
	;

	private final BlockEntry<PassableLeavesBlock> leaves;
	private final BlockEntry<SaplingBlock> sapling;
	private final Supplier<Item> fruit;
	private final Lazy<TreeConfiguration> treeConfig, treeConfigWild;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey, configKeyWild;
	public final ResourceKey<PlacedFeature> placementKey;

	public final Supplier<Block> log;
	public boolean genTree = false;

	FDTrees(Supplier<Block> log, FDTreeType height, Function<String, Supplier<Item>> items) {
		String name = name().toLowerCase(Locale.ROOT);
		this.log = log;
		this.treeConfig = Lazy.of(() -> buildTreeConfig(log, height, false));
		this.treeConfigWild = Lazy.of(() -> buildTreeConfig(log, height, true));
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree"));
		this.configKeyWild = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree_wild"));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, "tree/" + name + "_tree"));

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

	FDTrees(Supplier<Block> log, FDTreeType height, int food, float sat, boolean fast) {
		this(log, height, name -> FruitsDelight.REGISTRATE
				.item(name, p -> new Item(p.food(food(food, sat, fast))))
				.transform(b -> PlantDataEntry.addFruitTags(name, b))
				.register());
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

	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		ctx.register(configKey, new ConfiguredFeature<>(Feature.TREE, treeConfig.get()));
		ctx.register(configKeyWild, new ConfiguredFeature<>(Feature.TREE, treeConfigWild.get()));
	}

	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		ctx.register(placementKey, new PlacedFeature(
				ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKeyWild),
				VegetationPlacements.treePlacement(
						PlacementUtils.countExtra(0, 0.2F, 2), getSapling())));
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
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

		@Nullable
		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource rand, boolean large) {
			return configKey;
		}

	}

}
