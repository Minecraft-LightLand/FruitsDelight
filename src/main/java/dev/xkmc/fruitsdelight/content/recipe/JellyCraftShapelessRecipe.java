package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.content.item.IFDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2core.serial.recipe.AbstractShapelessRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;

public class JellyCraftShapelessRecipe extends AbstractShapelessRecipe<JellyCraftShapelessRecipe> {

	public JellyCraftShapelessRecipe(String group, ItemStack result, NonNullList<Ingredient> ingredients) {
		super(group, result, ingredients);
	}

	@Override
	public ItemStack assemble(CraftingInput cont, HolderLookup.Provider access) {
		ItemStack stack = super.assemble(cont, access);
		ListTag list = new ListTag();
		for (int i = 0; i < cont.size(); i++) {
			ItemStack input = cont.getItem(i);
			if (!input.isEmpty() && input.getItem() instanceof IFDFoodItem item && item.food() != null && input.is(TagGen.JELLY)) {
				list.add(StringTag.valueOf(item.food().fruit().name()));
			}
		}
		stack.getOrCreateTag().put(FDFoodItem.ROOT, list);
		return stack;
	}

	@Override
	public Serializer<JellyCraftShapelessRecipe> getSerializer() {
		return FDMiscs.SHAPELESS.get();
	}

}
