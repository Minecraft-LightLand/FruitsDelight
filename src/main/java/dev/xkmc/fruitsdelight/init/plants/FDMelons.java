package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2core.serial.loot.LootHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
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
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.BiConsumer;

public enum FDMelons implements PlantDataEntry<FDMelons> {
	HAMIMELON(2, 0.3f, true);

	private final BlockEntry<Block> melon;
	private final BlockEntry<StemBlock> stem;
	private final BlockEntry<AttachedStemBlock> attachedStem;

	private final ItemEntry<Item> slice;
	private final ItemEntry<ItemNameBlockItem> seed;

	public final ResourceKey<ConfiguredFeature<?, ?>> configKey;
	public final ResourceKey<PlacedFeature> placementKey;

	FDMelons(int food, float sat, boolean fast) {
		String name = name().toLowerCase(Locale.ROOT);
		this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE,
				FruitsDelight.loc(name));
		this.placementKey = ResourceKey.create(Registries.PLACED_FEATURE,
				FruitsDelight.loc(name));

		melon = FruitsDelight.REGISTRATE
				.block(name, p -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MELON)))
				.blockstate(this::buildMelonModel)
				.loot(this::buildMelonLoot)
				.tag(BlockTags.ENDERMAN_HOLDABLE, BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
				.item().build()
				.register();
		stem = FruitsDelight.REGISTRATE
				.block(name + "_stem", p -> new StemBlock(getMelonBlock(), this::getSeed,
						BlockBehaviour.Properties.ofFullCopy(Blocks.MELON_STEM)))
				.blockstate(this::buildStemModel)
				.loot(this::buildStemLoot)
				.color(() -> () -> FDMelons::stemColor)
				.tag(BlockTags.MAINTAINS_FARMLAND, BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
				.register();
		attachedStem = FruitsDelight.REGISTRATE
				.block("attached_" + name + "_stem", p -> new AttachedStemBlock(getMelonBlock(), this::getSeed,
						BlockBehaviour.Properties.ofFullCopy(Blocks.ATTACHED_MELON_STEM)))
				.blockstate(this::buildAttachedStemModel)
				.loot(this::buildAttachedStemLoot)
				.color(() -> () -> FDMelons::attachedColor)
				.tag(BlockTags.MAINTAINS_FARMLAND, BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
				.register();

		slice = FruitsDelight.REGISTRATE
				.item(name + "_slice", p -> new Item(p.food(food(food, sat, fast))))
				.register();

		seed = FruitsDelight.REGISTRATE
				.item(name + "_seeds", p -> new ItemNameBlockItem(getStem(), p))
				.register();

	}

	public Block getMelonBlock() {
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
	public void registerConfigs(BootstrapContext<ConfiguredFeature<?, ?>> ctx) {
		FeatureUtils.register(ctx, configKey, Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(24, 5, 3,
						PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(
										BlockStateProvider.simple(getMelonBlock())),
								BlockPredicate.allOf(BlockPredicate.replaceable(), BlockPredicate.noFluid(),
										BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.SAND)))));
	}

	@Override
	public void registerPlacements(BootstrapContext<PlacedFeature> ctx) {
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

	public void registerComposter(BiConsumer<Item, Float> builder) {
		builder.accept(getSeed(), 0.3f);
		builder.accept(getSlice(), 0.5f);
		builder.accept(getMelonBlock().asItem(), 0.65f);
	}

	public void genRecipe(RegistrateRecipeProvider pvd) {
		pvd.singleItem(DataIngredient.items(getSlice()), RecipeCategory.MISC, this::getSeed, 1, 1);
		pvd.square(DataIngredient.items(getSlice()), RecipeCategory.MISC, this::getMelonBlock, false);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(getMelonBlock()),
				Ingredient.of(CommonTags.TOOLS_KNIFE), getSlice(), 9, 1).build(pvd,
				FruitsDelight.loc(getName() + "_cutting"));
	}

	private void buildMelonModel(DataGenContext<Block, Block> ctx, RegistrateBlockstateProvider pvd) {
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

	private void buildMelonLoot(RegistrateBlockLootTables pvd, Block block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, pvd.createSilkTouchDispatchTable(block,
				pvd.applyExplosionDecay(block,
						LootItem.lootTableItem(getSlice())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 7.0F)))
								.apply(helper.fortuneCount(1))
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
				.nutrition(food).saturationModifier(sat);
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

	public static void register() {

	}

}
