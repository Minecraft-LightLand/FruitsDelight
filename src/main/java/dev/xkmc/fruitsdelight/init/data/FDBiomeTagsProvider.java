package dev.xkmc.fruitsdelight.init.data;

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
		var birch = asTag("birch");
		var taiga = asTag("taiga");
		var taiga_cold = asTag("taiga_cold");
		var swamp = asTag("swamp");
		var oak = asTag("fruitful_forest");
		var tropical = asTag("tropical");
		var windswpet = asTag("windswept");
		tag(birch).add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
		tag(taiga).add(Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA);
		tag(taiga_cold).add(Biomes.SNOWY_TAIGA, Biomes.GROVE);
		tag(swamp).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
		tag(oak).add(Biomes.FOREST, Biomes.FLOWER_FOREST);
		tag(tropical).add(Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE);
		tag(windswpet).add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST);

		tag(asTag(FDTrees.PEAR)).addTag(birch);
		tag(asTag(FDTrees.HAWBERRY)).addTag(taiga);
		tag(asTag(FDTrees.PERSIMMON)).addTag(taiga_cold);
		tag(asTag(FDTrees.LYCHEE)).addTag(tropical);
		tag(asTag(FDTrees.MANGO)).addTag(tropical);
		tag(asTag(FDTrees.ORANGE)).addTag(oak);
		tag(asTag(FDTrees.APPLE)).addTag(oak);
		tag(asTag(FDTrees.MANGOSTEEN)).addTags(swamp, tropical);
		tag(asTag(FDBushes.CRANBERRY)).addTags(swamp, tropical).add(Biomes.DARK_FOREST);
		tag(asTag(FDBushes.BLUEBERRY)).addTags(taiga, taiga_cold, swamp, windswpet);
		tag(asTag(FDTrees.PEACH)).add(Biomes.SPARSE_JUNGLE);
		tag(asTag(FDBushes.LEMON)).add(Biomes.PLAINS);
		tag(asTag(FDMelons.HAMIMELON)).add(Biomes.DESERT);
		tag(asTag(FDPineapple.PINEAPPLE)).add(Biomes.BEACH);
	}

	public static TagKey<Biome> asTag(PlantDataEntry<?> plant) {
		return asTag(plant.getName() + "_biomes");
	}

	public static TagKey<Biome> asTag(String name) {
		return TagKey.create(Registries.BIOME, new ResourceLocation(FruitsDelight.MODID, name));
	}

}