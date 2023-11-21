package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.fruitsdelight.init.food.FDBushes;
import dev.xkmc.fruitsdelight.init.food.FDMelons;
import dev.xkmc.fruitsdelight.init.food.FDPineapple;
import dev.xkmc.fruitsdelight.init.food.FDTrees;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface PlantDataEntry<E extends Enum<E> & PlantDataEntry<E>> {

	List<Supplier<PlantDataEntry<?>[]>> LIST = List.of(
			FDTrees::values, FDBushes::values, FDMelons::values, FDPineapple::values
	);

	static <T> void gen(T val, BiConsumer<PlantDataEntry<?>, T> mod) {
		for (var e : LIST) {
			for (var i : e.get()) {
				mod.accept(i, val);
			}
		}
	}

	static void run(Consumer<PlantDataEntry<?>> mod) {
		for (var e : LIST) {
			for (var i : e.get()) {
				mod.accept(i);
			}
		}
	}

	void registerComposter();

	void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx);

	void registerPlacements(BootstapContext<PlacedFeature> ctx);

	String getName();

	ResourceKey<PlacedFeature> getPlacementKey();

	default void genRecipe(RegistrateRecipeProvider pvd) {

	}

}
