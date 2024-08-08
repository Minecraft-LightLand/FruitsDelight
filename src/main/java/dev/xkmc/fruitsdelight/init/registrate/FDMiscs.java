package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.content.recipe.JamCraftShapedRecipe;
import dev.xkmc.fruitsdelight.content.recipe.JamCraftShapelessRecipe;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import dev.xkmc.l2core.serial.recipe.AbstractShapelessRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FDMiscs {


	private static final SR<RecipeSerializer<?>> RS = SR.of(FruitsDelight.REG, BuiltInRegistries.RECIPE_SERIALIZER);

	public static final Val<AbstractShapelessRecipe.Serializer<JamCraftShapelessRecipe>> SHAPELESS =
			RS.reg("jam_craft_shapeless", () -> new AbstractShapelessRecipe.Serializer<>(JamCraftShapelessRecipe::new));

	public static final Val<AbstractShapedRecipe.Serializer<JamCraftShapedRecipe>> SHAPED =
			RS.reg("jam_craft_shaped", () -> new AbstractShapedRecipe.Serializer<>(JamCraftShapedRecipe::new));

	public static void register() {
	}

}
