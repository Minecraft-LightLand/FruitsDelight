package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.content.recipe.JellyCraftShapedRecipe;
import dev.xkmc.fruitsdelight.content.recipe.JellyCraftShapelessRecipe;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.l2core.serial.recipe.AbstractShapelessRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FDMiscs {


	private static final SR<RecipeSerializer<?>> RS = SR.of(FruitsDelight.REG, BuiltInRegistries.RECIPE_SERIALIZER);

	public static final Val<AbstractShapelessRecipe.Serializer<JellyCraftShapelessRecipe>> SHAPELESS =
			RS.reg("jelly_craft_shapeless", () -> new AbstractShapelessRecipe.Serializer<>(JellyCraftShapelessRecipe::new));

	public static final Val<AbstractShapedRecipe.Serializer<JellyCraftShapedRecipe>> SHAPED =
			RS.reg("jelly_craft_shaped", () -> new AbstractShapedRecipe.Serializer<>(JellyCraftShapedRecipe::new));

	public static void register() {
	}

}
