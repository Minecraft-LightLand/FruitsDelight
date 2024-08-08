package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.content.item.IFDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2core.serial.recipe.AbstractShapelessRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class JamCraftShapelessRecipe extends AbstractShapelessRecipe<JamCraftShapelessRecipe> {

	public JamCraftShapelessRecipe(String group, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(group, result, ingredients);
	}

	@Override
	public ItemStack assemble(CraftingInput cont, HolderLookup.Provider access) {
		ItemStack stack = super.assemble(cont, access);
		List<FruitType> list = new ArrayList<>();
		for (int i = 0; i < cont.size(); i++) {
			ItemStack input = cont.getItem(i);
			if (!input.isEmpty() && input.getItem() instanceof IFDFoodItem item && input.is(TagGen.JAM)) {
				list.add(item.food().fruit());
			}
		}
		return FDItems.FRUITS.set(stack, list);
	}

	@Override
	public Serializer<JamCraftShapelessRecipe> getSerializer() {
		return FDMiscs.SHAPELESS.get();
	}

}
