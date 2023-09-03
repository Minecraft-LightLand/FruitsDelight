package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.PlantDataEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
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
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import javax.annotation.Nullable;
import java.util.Locale;

public enum FDMelons implements PlantDataEntry {
	HAMIMELON(2, 0.3f, true);

	private final BlockEntry<FDMelonBlock> melon;
	private final BlockEntry<StemBlock> stem;
	private final BlockEntry<AttachedStemBlock> attachedStem;

	private final ItemEntry<Item> slice;
	private final ItemEntry<ItemNameBlockItem> seed;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	FDMelons(int food, float sat, boolean fast) {
		String name = name().toLowerCase(Locale.ROOT);
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				new ResourceLocation(FruitsDelight.MODID, name));

		melon = FruitsDelight.REGISTRATE
				.block(name, p -> new FDMelonBlock(BlockBehaviour.Properties.copy(Blocks.MELON)))
				.blockstate(this::buildMelonModel)
				.loot(this::buildMelonLoot)
				.tag(BlockTags.ENDERMAN_HOLDABLE, BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
				.item().build()
				.register();
		stem = FruitsDelight.REGISTRATE
				.block(name + "_stem", p -> new StemBlock(getMelonBlock(), this::getSeed,
						BlockBehaviour.Properties.copy(Blocks.MELON_STEM)))
				.blockstate(this::buildStemModel)
				.loot(this::buildStemLoot)
				.tag(BlockTags.MAINTAINS_FARMLAND, BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
				.register();
		attachedStem = FruitsDelight.REGISTRATE
				.block("attached_" + name + "_stem", p -> new AttachedStemBlock(getMelonBlock(), this::getSeed,
						BlockBehaviour.Properties.copy(Blocks.ATTACHED_MELON_STEM)))
				.blockstate(this::buildAttachedStemModel)
				.loot(this::buildAttachedStemLoot)
				.tag(BlockTags.MAINTAINS_FARMLAND, BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
				.register();

		slice = FruitsDelight.REGISTRATE
				.item(name + "_slice", p -> new Item(p.food(food(food, sat, fast))))
				.register();

		seed = FruitsDelight.REGISTRATE
				.item(name + "_seeds", p -> new ItemNameBlockItem(getStem(), p))
				.register();

	}

	public StemGrownBlock getMelonBlock() {
		return melon.get();
	}

	public StemBlock getStem() {
		return stem.get();
	}

	public AttachedStemBlock getAttachedStem() {
		return attachedStem.get();
	}

	public Item getSlice() {
		return slice.get();
	}

	public ItemNameBlockItem getSeed() {
		return seed.get();
	}

	@Override
	public void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(64, 7, 3,
						PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
										BlockStateProvider.simple(getMelonBlock())),
								BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)))));
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

	public void registerComposter() {
		ComposterBlock.COMPOSTABLES.put(getSeed(), 0.3f);
		ComposterBlock.COMPOSTABLES.put(getSlice(), 0.5f);
		ComposterBlock.COMPOSTABLES.put(getMelonBlock(), 0.65f);
	}

	public void genRecipe(RegistrateRecipeProvider pvd) {
		pvd.singleItem(DataIngredient.items(getSlice()), RecipeCategory.MISC, this::getSeed, 1, 1);
		pvd.square(DataIngredient.items(getSlice()), RecipeCategory.MISC, this::getMelonBlock, false);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(getMelonBlock()),
				Ingredient.of(ForgeTags.TOOLS_KNIVES), getSlice(), 9, 1).build(pvd,
				new ResourceLocation(FruitsDelight.MODID, getName() + "_cutting"));
	}

	private void buildMelonModel(DataGenContext<Block, FDMelonBlock> ctx, RegistrateBlockstateProvider pvd) {
		String name = name().toLowerCase(Locale.ROOT);
		pvd.simpleBlock(ctx.get(), pvd.models().cubeColumn(ctx.getName(),
				pvd.modLoc("block/" + name + "_side"),
				pvd.modLoc("block/" + name + "_top")));
	}

	private void buildStemModel(DataGenContext<Block, StemBlock> ctx, RegistrateBlockstateProvider pvd) {
		String name = name().toLowerCase(Locale.ROOT);
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int age = state.getValue(StemBlock.AGE);
			return ConfiguredModel.builder().modelFile(pvd.models()
					.withExistingParent(ctx.getName() + "_stage" + age, "block/stem_growth" + age)
					.texture("stem", pvd.modLoc("block/" + name + "_stem"))
					.renderType("cutout")).build();
		});
	}

	private void buildAttachedStemModel(DataGenContext<Block, AttachedStemBlock> ctx, RegistrateBlockstateProvider pvd) {
		String name = name().toLowerCase(Locale.ROOT);
		pvd.horizontalBlock(ctx.get(), pvd.models().withExistingParent(ctx.getName(), "block/stem_fruit")
				.texture("stem", pvd.modLoc("block/" + name + "_stem"))
				.texture("upperstem", pvd.modLoc("block/" + name + "_attached"))
				.renderType("cutout"), 270);
	}

	private void buildMelonLoot(RegistrateBlockLootTables pvd, FDMelonBlock block) {
		pvd.add(block, RegistrateBlockLootTables.createSilkTouchDispatchTable(block,
				pvd.applyExplosionDecay(block,
						LootItem.lootTableItem(getSlice())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F)))
								.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
								.apply(LimitCount.limitCount(IntRange.upperBound(9))))));
	}

	private void buildStemLoot(RegistrateBlockLootTables pvd, StemBlock block) {
		pvd.add(block, pvd.createStemDrops(block, getSeed()));
	}

	private void buildAttachedStemLoot(RegistrateBlockLootTables pvd, AttachedStemBlock block) {
		pvd.add(block, pvd.createAttachedStemDrops(block, getSeed()));
	}

	private static FoodProperties food(int food, float sat, boolean fast) {
		var ans = new FoodProperties.Builder()
				.nutrition(food).saturationMod(sat);
		if (fast) ans.fast();
		return ans.build();
	}

	private static int stemColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
		int i = state.getValue(StemBlock.AGE);
		return i << 21 | (255 - (i << 3)) << 8 | i << 2;
	}

	private static int attachedColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
		return 14731036;
	}

	public static void registerColor(RegisterColorHandlersEvent.Block event) {
		for (FDMelons melon : FDMelons.values()) {
			event.register(FDMelons::stemColor, melon.getStem());
			event.register(FDMelons::attachedColor, melon.getAttachedStem());
		}
	}

	public static void register() {

	}

	private class FDMelonBlock extends StemGrownBlock {

		public FDMelonBlock(Properties properties) {
			super(properties);
		}

		@Override
		public StemBlock getStem() {
			return stem.get();
		}

		@Override
		public AttachedStemBlock getAttachedStem() {
			return attachedStem.get();
		}

	}

}
