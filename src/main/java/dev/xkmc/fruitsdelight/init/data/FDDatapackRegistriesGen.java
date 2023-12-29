package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.PlantDataEntry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FDDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ctx -> PlantDataEntry.gen(ctx, PlantDataEntry::registerConfigs))
			.add(Registries.PLACED_FEATURE, ctx -> PlantDataEntry.gen(ctx, PlantDataEntry::registerPlacements))
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, FDDatapackRegistriesGen::registerBiomeModifiers);

	private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		PlantDataEntry.run(e -> registerTreeBiome(ctx, e, biomes, features));
	}

	private static void registerTreeBiome(BootstapContext<BiomeModifier> ctx,
										  PlantDataEntry<?> tree,
										  HolderGetter<Biome> biomeGetter,
										  HolderGetter<PlacedFeature> features) {
		HolderSet<Biome> set = biomeGetter.getOrThrow(FDBiomeTagsProvider.asTag(tree));
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FruitsDelight.MODID,
				tree.getName())), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(set,
				HolderSet.direct(features.getOrThrow(tree.getPlacementKey())),
				GenerationStep.Decoration.VEGETAL_DECORATION));
	}

	public FDDatapackRegistriesGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, BUILDER, Set.of("minecraft", FruitsDelight.MODID));
	}

	@NotNull
	public String getName() {
		return "Fruits Delight Data";
	}

}
