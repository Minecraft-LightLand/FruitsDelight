package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.fruitsdelight.content.recipe.JellyCraftShapedRecipe;
import dev.xkmc.fruitsdelight.content.recipe.JellyCraftShapelessRecipe;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.l2core.serial.recipe.AbstractShapelessRecipe;
import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.l2library.serial.recipe.AbstractShapelessRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class FDMiscs {

	public static final RegistryEntry<AbstractShapelessRecipe.Serializer<JellyCraftShapelessRecipe>> SHAPELESS =
			reg("jelly_craft_shapeless", () -> new AbstractShapelessRecipe.Serializer<>(JellyCraftShapelessRecipe::new));

	public static final RegistryEntry<AbstractShapedRecipe.Serializer<JellyCraftShapedRecipe>> SHAPED =
			reg("jelly_craft_shaped", () -> new AbstractShapedRecipe.Serializer<>(JellyCraftShapedRecipe::new));

	private static <A extends RecipeSerializer<?>> RegistryEntry<A> reg(String id, NonNullSupplier<A> sup) {
		return FruitsDelight.REGISTRATE.simple(id, ForgeRegistries.Keys.RECIPE_SERIALIZERS, sup);
	}

	public static void register() {
	}

}
