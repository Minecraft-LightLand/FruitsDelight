package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDBushes;
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
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FDDatapackRegistriesGen extends DatapackBuiltinEntriesProvider {

	public interface SpawnModifier {

		void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx);

		void registerPlacements(BootstapContext<PlacedFeature> ctx);

		String getName();

		ResourceKey<PlacedFeature> getPlacementKey();
	}

	private static <T> void gen(T val, BiConsumer<SpawnModifier, T> mod) {
		for (FDTrees e : FDTrees.values()) {
			mod.accept(e, val);
		}
		for (FDBushes e : FDBushes.values()) {
			mod.accept(e, val);
		}
	}

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, ctx -> gen(ctx, SpawnModifier::registerConfigs))
			.add(Registries.PLACED_FEATURE, ctx -> gen(ctx, SpawnModifier::registerPlacements))
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, FDDatapackRegistriesGen::registerBiomeModifiers);

	private static void registerBiomeModifiers(BootstapContext<BiomeModifier> ctx) {
		var biomes = ctx.lookup(Registries.BIOME);
		var features = ctx.lookup(Registries.PLACED_FEATURE);
		registerTreeBiome(ctx, FDTrees.PEAR, biomes, features, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
		registerTreeBiome(ctx, FDTrees.HAWBERRY, biomes, features, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA);
		registerTreeBiome(ctx, FDTrees.PERSIMMON, biomes, features, Biomes.SNOWY_TAIGA, Biomes.GROVE);
		registerTreeBiome(ctx, FDTrees.LYCHEE, biomes, features, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE);
		registerTreeBiome(ctx, FDTrees.MANGO, biomes, features, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE);
		registerTreeBiome(ctx, FDBushes.BLUEBERRY, biomes, features, Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA,
				Biomes.SNOWY_TAIGA, Biomes.GROVE,
				Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST);
	}

	@SuppressWarnings("unchecked")
	@SafeVarargs
	private static void registerTreeBiome(BootstapContext<BiomeModifier> ctx,
										  SpawnModifier tree,
										  HolderGetter<Biome> biomeGetter,
										  HolderGetter<PlacedFeature> features,
										  ResourceKey<Biome>... biomes) {
		HolderSet<Biome> set = HolderSet.direct(Arrays.stream(biomes).map(biomeGetter::getOrThrow).toArray(Holder[]::new));
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
