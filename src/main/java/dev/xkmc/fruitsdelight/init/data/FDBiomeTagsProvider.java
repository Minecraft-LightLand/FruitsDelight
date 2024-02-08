package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.compat.biomes.ModBiomeKeys;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public final class FDBiomeTagsProvider extends BiomeTagsProvider {

	public FDBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
		super(output, pvd, FruitsDelight.MODID, helper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider pvd) {

		var plain = asTag("vanilla/plains");
		var birch = asTag("vanilla/birch");
		var taiga = asTag("vanilla/taiga");
		var taiga_cold = asTag("vanilla/taiga_cold");
		var swamp = asTag("vanilla/swamp");
		var oak = asTag("vanilla/fruitful_forest");
		var tropical = asTag("vanilla/tropical");
		var windswpet = asTag("vanilla/windswept");

		tag(plain).add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.MEADOW);
		tag(birch).add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
		tag(taiga).add(Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA);
		tag(taiga_cold).add(Biomes.SNOWY_TAIGA, Biomes.GROVE, Biomes.SNOWY_PLAINS);
		tag(swamp).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
		tag(oak).add(Biomes.FOREST, Biomes.FLOWER_FOREST);
		tag(tropical).add(Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE);
		tag(windswpet).add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST);

		tag(asTag(FDTrees.ORANGE)).addTags(ModBiomeKeys.WARM.asTag(), oak);
		tag(asTag(FDTrees.APPLE)).addTags(ModBiomeKeys.WARM.asTag(), oak);
		tag(asTag(FDTrees.KIWI)).addTags(ModBiomeKeys.WARM.asTag(), oak);
		tag(asTag(FDTrees.FIG)).addTags(ModBiomeKeys.WARM.asTag(), oak);

		tag(asTag(FDTrees.LYCHEE)).addTags(ModBiomeKeys.HOT_HUMID.asTag(), tropical);
		tag(asTag(FDTrees.MANGO)).addTags(ModBiomeKeys.HOT_HUMID.asTag(), tropical);
		tag(asTag(FDTrees.BAYBERRY)).addTags(ModBiomeKeys.HOT_HUMID.asTag(), swamp);
		tag(asTag(FDTrees.MANGOSTEEN)).addTags(ModBiomeKeys.HOT_HUMID.asTag(), swamp, tropical);

		tag(asTag(FDTrees.HAWBERRY)).addTags(ModBiomeKeys.COLD.asTag(), taiga);
		tag(asTag(FDTrees.PERSIMMON)).addTags(ModBiomeKeys.COLD.asTag(), taiga_cold);
		tag(asTag(FDTrees.PEAR)).addTags(ModBiomeKeys.WARM.asTag(), birch);
		tag(asTag(FDTrees.PEACH)).addTag(ModBiomeKeys.HOT_HUMID.asTag()).add(Biomes.SPARSE_JUNGLE);
		tag(asTag(FDTrees.DURIAN)).addTags(ModBiomeKeys.HOT_HUMID.asTag(), swamp, tropical).add(Biomes.SPARSE_JUNGLE);

		tag(asTag(FDBushes.LEMON)).addTags(ModBiomeKeys.WARM.asTag(), plain);
		tag(asTag(FDBushes.CRANBERRY)).addTags(ModBiomeKeys.HOT_HUMID.asTag(), swamp, tropical).add(Biomes.DARK_FOREST);
		tag(asTag(FDBushes.BLUEBERRY)).addTags(ModBiomeKeys.COLD.asTag(), taiga, taiga_cold, swamp, windswpet);
		tag(asTag(FDMelons.HAMIMELON)).addTag(ModBiomeKeys.DESERT.asTag()).add(Biomes.DESERT);
		tag(asTag(FDPineapple.PINEAPPLE)).addTag(ModBiomeKeys.BEACH.asTag()).add(Biomes.BEACH);

		ModBiomeKeys.generate(this::tag);
	}

	public static TagKey<Biome> asTag(PlantDataEntry<?> plant) {
		return asTag("plants/" + plant.getName() + "_biomes");
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, new ResourceLocation(FruitsDelight.MODID, name));
	}

	// savanna,savanna_plateau, windswept_savanna


}