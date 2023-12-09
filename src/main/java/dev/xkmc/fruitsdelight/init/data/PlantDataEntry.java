package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.fruitsdelight.init.food.FDBushes;
import dev.xkmc.fruitsdelight.init.food.FDMelons;
import dev.xkmc.fruitsdelight.init.food.FDPineapple;
import dev.xkmc.fruitsdelight.init.food.FDTrees;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.core.Holder;
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
				ItemTags.create(new ResourceLocation("forge", "fruits/" + name))
		);
	}

	void registerComposter();

	void registerConfigs();

	void registerPlacements();

	String getName();

	Holder<PlacedFeature> getPlacementKey();

	default void genRecipe(RegistrateRecipeProvider pvd) {

	}

}
