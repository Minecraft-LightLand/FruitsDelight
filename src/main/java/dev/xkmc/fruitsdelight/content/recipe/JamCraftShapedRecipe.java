package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.content.item.IFDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.ArrayList;
import java.util.List;

public class JellyCraftShapedRecipe extends AbstractShapedRecipe<JellyCraftShapedRecipe> {

	public JellyCraftShapedRecipe(String group, ShapedRecipePattern pattern, ItemStack result) {
		super(group, pattern, result);
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
	public Serializer<JellyCraftShapedRecipe> getSerializer() {
		return FDMiscs.SHAPED.get();
	}

}
