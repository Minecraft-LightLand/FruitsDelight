package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.block.BaseBushBlock;
import dev.xkmc.fruitsdelight.content.block.BushFruitItem;
import dev.xkmc.fruitsdelight.content.block.FruitBushBlock;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.Locale;

import static net.minecraft.world.level.block.DoublePlantBlock.HALF;

public enum FDBushes implements FruitPlant<FDBushes> {
	BLUEBERRY(2, 0.3f, true, 32, FDBushType.BLOCK),
	LEMON(4, 0.3f, false, 32, FDBushType.TALL),
	CRANBERRY(2, 0.3f, true, 32, FDBushType.CROSS);

	private final BlockEntry<? extends BushBlock> bush;
	private final ItemEntry<BushFruitItem> seedItem;
	private final ItemEntry<FDFoodItem> mid;

	private final int rarity;
	private final FDBushType type;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	FDBushes(int food, float sat, boolean fast, int rarity, FDBushType type) {
		String name = name().toLowerCase(Locale.ROOT);
		String suffix = type == FDBushType.TALL ? "tree" : "bush";
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, name + "_" + suffix));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, name + "_" + suffix));
		this.rarity = rarity;
		this.type = type;
		bush = type.build(name + "_" + suffix, this);
		if (type == FDBushType.TALL) {
			mid = FruitsDelight.REGISTRATE.item(name, p -> new FDFoodItem(p.food(food(food, sat, fast)), null))
					.transform(b -> PlantDataEntry.addFruitTags(name, b))
					.register();
			seedItem = FruitsDelight.REGISTRATE
					.item(name + "_seeds", p -> new BushFruitItem(getBush(), p))
					.register();
		} else {
			mid = null;
			seedItem = FruitsDelight.REGISTRATE
					.item(name, p -> new BushFruitItem(getBush(), p.food(food(food, sat, fast))))
					.transform(b -> PlantDataEntry.addFruitTags(name, b))
					.register();
		}
	}


	public void registerComposter() {
		if (type == FDBushType.TALL)
			ComposterBlock.COMPOSTABLES.put(mid.get(), 0.35f);
		ComposterBlock.COMPOSTABLES.put(seedItem.get(), 0.15f);
		if (type != FDBushType.CROSS)
			ComposterBlock.COMPOSTABLES.put(getBush().asItem(), 0.65f);
	}

	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		BlockState state = getBush().defaultBlockState().setValue(BaseBushBlock.AGE, 4);
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(BlockStateProvider.simple(state)),
						List.of(Blocks.GRASS_BLOCK), 20));
	}

	public void registerPlacements(BootstapContext<PlacedFeature> ctx) {
		ctx.register(placementKey, new PlacedFeature(
				ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configKey),
				List.of(
						RarityFilter.onAverageOnceEvery(rarity),
						InSquarePlacement.spread(),
						PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
						BiomeFilter.biome()
				)));
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public ResourceKey<PlacedFeature> getPlacementKey() {
		return placementKey;
	}

	@Override
	public void genRecipe(RegistrateRecipeProvider pvd) {
		if (mid != null) {
			pvd.singleItem(DataIngredient.items(mid.get()), RecipeCategory.MISC, seedItem, 1, 1);
		}
	}

	public void buildLoot(RegistrateBlockLootTables pvd, BushBlock block) {
		var table = LootTable.lootTable();
		if (type != FDBushType.CROSS) {
			var pool = LootPool.lootPool()
					.add(LootItem.lootTableItem(getBush().asItem())
							.when(LootTableTemplate.silk(false))
							.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
									.setProperties(StatePropertiesPredicate.Builder.properties()
											.hasProperty(FruitBushBlock.AGE, 0))
									.or(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
											.setProperties(StatePropertiesPredicate.Builder.properties()
													.hasProperty(FruitBushBlock.AGE, 1)))
									.invert()
							))
					.when(ExplosionCondition.survivesExplosion());

			table.withPool(pool);
		}
		var pool = LootPool.lootPool()
				.add(LootItem.lootTableItem(getFruit())
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties()
										.hasProperty(FruitBushBlock.AGE, 4)))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
				.when(ExplosionCondition.survivesExplosion());
		if (type == FDBushType.TALL) {
			pool.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
					.setProperties(StatePropertiesPredicate.Builder.properties()
							.hasProperty(HALF, DoubleBlockHalf.LOWER)));
		}
		table.withPool(pool);
		pvd.add(block, table);
	}

	public BushBlock getBush() {
		return bush.get();
	}

	public Item getFruit() {
		return type == FDBushType.TALL ? mid.get() : seedItem.get();
	}

	public Item getSeed() {
		return seedItem.get();
	}


	public void buildBushModel(DataGenContext<Block, ? extends BushBlock> ctx, RegistrateBlockstateProvider pvd) {
		if (type == FDBushType.CROSS) {
			pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
				int age = state.getValue(FruitBushBlock.AGE);
				String id = ctx.getName();
				if (age == 1) {
					return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_0"))).build();
				}
				id += "_" + (age == 0 ? 0 : age - 1);
				var model = pvd.models().cross(id, pvd.modLoc("block/" + id)).renderType("cutout");
				return ConfiguredModel.builder().modelFile(model).build();
			});
			return;
		}
		if (type == FDBushType.BLOCK) {
			pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
				int age = state.getValue(FruitBushBlock.AGE);
				String id = ctx.getName();
				String parent = "bush";
				if (age == 0) {
					id += "_small";
					parent += "_small";
				}
				if (age == 1) {
					return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_small"))).build();
				}
				if (age == 3) id += "_flowers";
				if (age == 4) id += "_fruits";
				var model = pvd.models().getBuilder(id)
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + parent)));
				model.texture("face", "block/" + id + "_face");
				model.texture("cross", "block/" + id + "_cross");
				return ConfiguredModel.builder().modelFile(model).build();
			});
			return;
		}
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(FruitBushBlock.AGE);
			String modelId = ctx.getName();
			if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
				return ConfiguredModel.builder().modelFile(pvd.models()
						.withExistingParent(modelId + "_upper", pvd.mcLoc("block/air"))
						.texture("particle", pvd.modLoc("block/" + modelId + "_top"))
				).build();
			}
			String parent = "tall_bush";
			String trunk = age <= 1 ? "_small_trunk" : "_trunk";
			String tex = "";
			if (age == 0) {
				modelId += "_small";
				parent += "_small";
			}
			if (age == 1) {
				modelId += "_mid";
				parent += "_mid";
			}
			if (age == 3) tex = "_flowers";
			if (age == 4) tex = "_fruits";
			modelId += tex;
			tex = ctx.getName() + tex;
			var model = pvd.models().getBuilder(modelId)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + parent)));
			model.texture("upper", "block/" + tex + "_upper");
			model.texture("lower", "block/" + tex + "_lower");
			model.texture("trunk", "block/" + ctx.getName() + trunk);
			model.texture("top", "block/" + tex + "_top");
			return ConfiguredModel.builder().modelFile(model).build();
		});
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
