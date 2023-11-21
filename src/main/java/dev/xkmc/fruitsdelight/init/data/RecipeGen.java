package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		PlantDataEntry.gen(pvd, PlantDataEntry::genRecipe);

		{
			jelly(pvd, FDFood.APPLE_JELLY, 1);
			jelly(pvd, FDFood.BLUEBERRY_JELLY, 3);
			jelly(pvd, FDFood.GLOWBERRY_JELLY, 3);
			jelly(pvd, FDFood.MANGO_JELLY, 1);
			jelly(pvd, FDFood.HAMIMELON_JELLY, 2);
			jelly(pvd, FDFood.HAWBERRY_JELLY, 3);
			jelly(pvd, FDFood.LYCHEE_JELLY, 3);
			jelly(pvd, FDFood.ORANGE_JELLY, 1);
			jelly(pvd, FDFood.PEACH_JELLY, 1);
			jelly(pvd, FDFood.PEAR_JELLY, 1);
			jelly(pvd, FDFood.PERSIMMON_JELLY, 1);
			jelly(pvd, FDFood.PINEAPPLE_JELLY, 2);
			jelly(pvd, FDFood.SWEETBERRY_JELLY, 3);
		}

	}

	private static void jelly(RegistrateRecipeProvider pvd, FDFood jelly, int count) {
		unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, jelly.item)::unlockedBy, jelly.getFruit())
				.requires(Items.GLASS_BOTTLE).requires(Items.SUGAR).requires(jelly.getFruit(), count)
				.save(pvd);
	}

	private static void juice(RegistrateRecipeProvider pvd, FDFood juice, int count) {
		unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, juice.item)::unlockedBy, juice.getFruit())
				.requires(Items.GLASS_BOTTLE).requires(juice.getFruit(), count)
				.save(pvd);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}


}
