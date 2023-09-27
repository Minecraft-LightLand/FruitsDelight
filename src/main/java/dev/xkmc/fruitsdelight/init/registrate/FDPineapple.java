package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.block.PineappleBlock;
import dev.xkmc.fruitsdelight.content.block.WildPineappleBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.PlantDataEntry;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.Locale;

public enum FDPineapple implements PlantDataEntry<FDPineapple> {
	PINEAPPLE(3, 0.5f, true);

	private final BlockEntry<PineappleBlock> PLANT;
	private final BlockEntry<WildPineappleBlock> WILD;

	private final ItemEntry<Item> fruit, slice;
	private final ItemEntry<ItemNameBlockItem> seed;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	FDPineapple(int food, float sat, boolean fast) {
		String name = name().toLowerCase(Locale.ROOT);
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, name));

		PLANT = FruitsDelight.REGISTRATE.block(name, p -> new PineappleBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)))
				.blockstate(this::buildPlantModel)
				.loot(this::buildPlantLoot)
				.register();

		WILD = FruitsDelight.REGISTRATE.block("wild_" + name, p -> new WildPineappleBlock(BlockBehaviour.Properties.copy(Blocks.GRASS)))
				.blockstate(this::buildWildModel)
				.loot(this::buildWildLoot)
				.item().model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("block/" + name + "_wild"))).build()
				.register();

		fruit = FruitsDelight.REGISTRATE.item(name, Item::new).register();
		slice = FruitsDelight.REGISTRATE.item(name + "_slice", p -> new Item(p.food(food(food, sat, fast))))
				.register();
		seed = FruitsDelight.REGISTRATE
				.item(name + "_sapling", p -> new ItemNameBlockItem(getPlant(), p))
				.register();

	}

	private void buildPlantModel(DataGenContext<Block, PineappleBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(PineappleBlock.AGE);
			String tex = getName() + "_stage_" + age;
			return ConfiguredModel.builder().modelFile(pvd.models().cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout")).build();
		});
	}

	private void buildWildModel(DataGenContext<Block, WildPineappleBlock> ctx, RegistrateBlockstateProvider pvd) {
		String tex = getName() + "_wild";
		pvd.simpleBlock(ctx.get(), pvd.models().cross(tex, pvd.modLoc("block/" + tex)).renderType("cutout"));
	}

	private void buildPlantLoot(RegistrateBlockLootTables pvd, PineappleBlock block) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(getWholeFruit())
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties()
										.hasProperty(PineappleBlock.AGE, 3)))
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(getSapling())))
				)));
	}

	private void buildWildLoot(RegistrateBlockLootTables pvd, WildPineappleBlock block) {
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(block.asItem())
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
								.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
										MinMaxBounds.Ints.atLeast(1)))))
						.otherwise(pvd.applyExplosionDecay(block, LootItem.lootTableItem(getWholeFruit())))
				)));
	}

	public void genRecipe(RegistrateRecipeProvider pvd) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(getWholeFruit()),
						Ingredient.of(ForgeTags.TOOLS_KNIVES), getSlice(), 6, 1)
				.addResult(getSapling())
				.addResultWithChance(getSapling(), 0.5f)
				.build(pvd, new ResourceLocation(FruitsDelight.MODID, getName() + "_cutting"));
	}

	public Block getPlant() {
		return PLANT.get();
	}

	public Block getWildPlant() {
		return WILD.get();
	}

	public Item getWholeFruit() {
		return fruit.get();
	}

	public Item getSlice() {
		return slice.get();
	}

	public Item getSapling() {
		return seed.get();
	}

	@Override
	public void registerComposter() {
		ComposterBlock.COMPOSTABLES.put(getSapling(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(getSlice(), 0.5f);
		ComposterBlock.COMPOSTABLES.put(getWholeFruit(), 0.65f);
	}

	@Override
	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(24, 5, 3,
						PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
										BlockStateProvider.simple(getWildPlant())),
								BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.SAND)))));
	}

	@Override
	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		PlacementUtils.register(ctx, placementKey, ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKey),
				RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
	}

	private static FoodProperties food(int food, float sat, boolean fast) {
		var ans = new FoodProperties.Builder()
				.nutrition(food).saturationMod(sat);
		if (fast) ans.fast();
		return ans.build();
	}

	public static void register() {

	}

}
