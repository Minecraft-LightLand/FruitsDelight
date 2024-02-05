package dev.xkmc.fruitsdelight.init.plants;

import dev.xkmc.fruitsdelight.content.block.BaseBushBlock;
import dev.xkmc.fruitsdelight.content.block.BushFruitItem;
import dev.xkmc.fruitsdelight.content.block.FruitBushBlock;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2library.repack.registrate.util.DataIngredient;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import dev.xkmc.l2library.util.data.LootTableTemplate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.List;
import java.util.Locale;

public enum FDBushes implements PlantDataEntry<FDBushes> {
	BLUEBERRY(2, 0.3f, true, 32, FDBushType.BLOCK),
	LEMON(4, 0.3f, false, 32, FDBushType.TALL),
	CRANBERRY(2, 0.3f, true, 32, FDBushType.CROSS);

	private final BlockEntry<FruitBushBlock> bush;
	private final ItemEntry<BushFruitItem> seedItem;
	private final ItemEntry<FDFoodItem> mid;

	private final int rarity;
	private final FDBushType type;

	public final ResourceLocation configKey, placementKey;

	FDBushes(int food, float sat, boolean fast, int rarity, FDBushType type) {
		String name = name().toLowerCase(Locale.ROOT);
		String suffix = type == FDBushType.TALL ? "tree" : "bush";
		this.configKey = new ResourceLocation(FruitsDelight.MODID, name + "_" + suffix);
		this.placementKey = new ResourceLocation(FruitsDelight.MODID, name + "_" + suffix);
		this.rarity = rarity;
		this.type = type;

		if (type == FDBushType.CROSS) {
			bush = FruitsDelight.REGISTRATE
					.block(name + "_" + suffix, p -> new FruitBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH), this::getFruit, type))
					.blockstate(this::buildBushModel)
					.loot(this::buildLoot)
					.register();
		} else {
			bush = FruitsDelight.REGISTRATE
					.block(name + "_" + suffix, p -> new FruitBushBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA), this::getFruit, type))
					.blockstate(this::buildBushModel)
					.loot(this::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.item().build()
					.register();
		}

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

	private Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> bushCF;
	private Holder<PlacedFeature> bushPF;

	public void registerConfigs() {
		BlockState state = getBush().defaultBlockState().setValue(BaseBushBlock.AGE, 4);
		bushCF = FeatureUtils.register(configKey.toString(), Feature.RANDOM_PATCH,
				FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
						new SimpleBlockConfiguration(BlockStateProvider.simple(state)),
						List.of(Blocks.GRASS_BLOCK), 20));
	}

	public void registerPlacements() {
		bushPF = PlacementUtils.register(placementKey.toString(), bushCF,
				List.of(
						RarityFilter.onAverageOnceEvery(rarity),
						InSquarePlacement.spread(),
						PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
						BiomeFilter.biome()
				));

	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public Holder<PlacedFeature> getPlacementKey() {
		return bushPF;
	}

	@Override
	public void genRecipe(RegistrateRecipeProvider pvd) {
		if (mid != null) {
			pvd.singleItem(DataIngredient.items(mid.get()), seedItem, 1, 1);
		}
	}

	private void buildLoot(RegistrateBlockLootTables pvd, FruitBushBlock block) {
		var table = LootTable.lootTable();
		if (type != FDBushType.CROSS)
			table.withPool(LootPool.lootPool()
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
					.when(ExplosionCondition.survivesExplosion()));
		table.withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(getFruit())
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties()
										.hasProperty(FruitBushBlock.AGE, 4)))
						.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1)))
				.when(ExplosionCondition.survivesExplosion()));
		pvd.add(block, table);
	}

	public FruitBushBlock getBush() {
		return bush.get();
	}

	public Item getFruit() {
		return type == FDBushType.TALL ? mid.get() : seedItem.get();
	}

	public Item getSeed() {
		return seedItem.get();
	}

	private void buildBushModel(DataGenContext<Block, FruitBushBlock> ctx, RegistrateBlockstateProvider pvd) {
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
		boolean tall = type == FDBushType.TALL;
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(FruitBushBlock.AGE);
			String id = ctx.getName();
			String parent = "bush";
			if (tall) parent = "tall_" + parent;
			if (age == 0) {
				id += "_small";
				parent += "_small";
			}
			if (age == 1) {
				if (tall) {
					id += "_mid";
					parent += "_mid";
				} else {
					return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_small"))).build();
				}
			}
			if (age == 3) id += "_flowers";
			if (age == 4) id += "_fruits";
			var model = pvd.models().getBuilder(id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + parent)));
			model.texture("face", "block/" + id + "_face");
			model.texture("cross", "block/" + id + "_cross");
			if (tall) {
				model.texture("top", "block/" + id + "_top");
			}
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
