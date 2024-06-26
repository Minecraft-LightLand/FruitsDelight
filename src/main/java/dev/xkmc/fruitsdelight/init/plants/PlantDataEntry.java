package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.fruitsdelight.compat.diet.DietTagGen;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
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

	static <T extends Item> ItemBuilder<T, L2Registrate> addFruitTags(String name, ItemBuilder<T, L2Registrate> b) {
		return b.tag(
				ItemTags.create(new ResourceLocation("forge", "fruits")),
				DietTagGen.FRUITS.tag,
				ItemTags.create(new ResourceLocation("forge", "fruits/" + name))
		);
	}

	void registerComposter();

	void registerConfigs(BootstapContext<ConfiguredFeature<?, ?>> ctx);

	void registerPlacements(BootstapContext<PlacedFeature> ctx);

	String getName();

	ResourceKey<PlacedFeature> getPlacementKey();

	default void genRecipe(RegistrateRecipeProvider pvd) {

	}

}
