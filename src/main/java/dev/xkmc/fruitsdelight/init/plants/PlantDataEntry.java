package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.fruitsdelight.compat.diet.DietTagGen;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

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
		return b.tag(Tags.Items.FOODS_FRUIT, DietTagGen.FRUITS.tag,
				ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "foods/fruit/" + name))
		);
	}

	static void registerComposter(RegistrateDataMapProvider pvd) {
		var builder = pvd.builder(NeoForgeDataMaps.COMPOSTABLES);
		BiConsumer<Item, Float> cons = (i, f) -> builder.add(BuiltInRegistries.ITEM.wrapAsHolder(i),
				new Compostable(f, true), false);
		PlantDataEntry.run(e -> e.registerComposter(cons));
		Durian.registerComposter(cons);
	}

	void registerComposter(BiConsumer<Item, Float> builder);

	void registerConfigs(BootstrapContext<ConfiguredFeature<?, ?>> ctx);

	void registerPlacements(BootstrapContext<PlacedFeature> ctx);

	String getName();

	ResourceKey<PlacedFeature> getPlacementKey();

	default void genRecipe(RegistrateRecipeProvider pvd) {

	}

}
