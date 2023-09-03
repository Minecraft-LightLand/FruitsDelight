package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.registrate.FDBushes;
import dev.xkmc.fruitsdelight.init.registrate.FDMelons;
import dev.xkmc.fruitsdelight.init.registrate.FDTrees;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface PlantDataEntry {

	static <T> void gen(T val, BiConsumer<PlantDataEntry, T> mod) {
		for (FDTrees e : FDTrees.values()) {
			mod.accept(e, val);
		}
		for (FDBushes e : FDBushes.values()) {
			mod.accept(e, val);
		}
		for (FDMelons e : FDMelons.values()) {
			mod.accept(e, val);
		}
	}

	static void run(Consumer<PlantDataEntry> mod) {
		for (FDTrees e : FDTrees.values()) {
			mod.accept(e);
		}
		for (FDBushes e : FDBushes.values()) {
			mod.accept(e);
		}
		for (FDMelons e : FDMelons.values()) {
			mod.accept(e);
		}
	}

	void registerComposter();

	void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx);

	void registerPlacements(BootstapContext<PlacedFeature> ctx);

	String getName();

	ResourceKey<PlacedFeature> getPlacementKey();

}
