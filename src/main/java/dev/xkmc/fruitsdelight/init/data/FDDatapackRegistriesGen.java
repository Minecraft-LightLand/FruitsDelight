package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDTrees;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FDDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ctx -> FDTrees.gen(ctx, FDTrees::registerConfigs))
			.add(Registries.PLACED_FEATURE, ctx -> FDTrees.gen(ctx, FDTrees::registerPlacements))
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, FDDatapackRegistriesGen::registerBiomeModifiers);

	private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		registerTreeBiome(ctx, FDTrees.PEAR, biomes, features, Biomes.BIRCH_FOREST);
		registerTreeBiome(ctx, FDTrees.HAWBERRY, biomes, features, Biomes.TAIGA);
		registerTreeBiome(ctx, FDTrees.PERSIMMON, biomes, features, Biomes.SNOWY_TAIGA);
		registerTreeBiome(ctx, FDTrees.LYCHEE, biomes, features, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE);
		registerTreeBiome(ctx, FDTrees.MANGO, biomes, features, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE);
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	private static void registerTreeBiome(BootstapContext<BiomeModifier> ctx,
										  FDTrees tree,
										  HolderGetter<Biome> biomeGetter,
										  HolderGetter<PlacedFeature> features,
										  ResourceKey<Biome>... biomes) {
		HolderSet<Biome> set = HolderSet.direct(Arrays.stream(biomes).map(biomeGetter::getOrThrow).toArray(Holder[]::new));
		ctx.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(FruitsDelight.MODID,
				tree.name().toLowerCase(Locale.ROOT))), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(set,
				HolderSet.direct(features.getOrThrow(tree.placementKey)),
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
