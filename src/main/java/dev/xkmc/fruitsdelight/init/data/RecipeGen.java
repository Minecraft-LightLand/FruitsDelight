package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.fruitsdelight.init.registrate.FDMelons;
import dev.xkmc.fruitsdelight.init.registrate.FDPineapple;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.world.item.Item;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		PlantDataEntry.gen(pvd, PlantDataEntry::genRecipe);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, InventoryChangeTrigger.TriggerInstance, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCritereon(pvd));
	}


}
