package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.PlantDataEntry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FDDatapackRegistriesGen {

	public static void registerBiomeModifiers(BootstrapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		PlantDataEntry.run(e -> registerTreeBiome(ctx, e, biomes, features));
	}

	private static void registerTreeBiome(BootstrapContext<BiomeModifier> ctx,
										  PlantDataEntry<?> tree,
										  HolderGetter<Biome> biomeGetter,
										  HolderGetter<PlacedFeature> features) {
		HolderSet<Biome> set = biomeGetter.getOrThrow(FDBiomeTagsProvider.asTag(tree));
		ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, FruitsDelight.loc(
				tree.getName())), new BiomeModifiers.AddFeaturesBiomeModifier(set,
				HolderSet.direct(features.getOrThrow(tree.getPlacementKey())),
				GenerationStep.Decoration.VEGETAL_DECORATION));
	}


}
