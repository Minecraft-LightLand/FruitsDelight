package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.content.item.IFDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class JellyCraftShapedRecipe extends AbstractShapedRecipe<JellyCraftShapedRecipe> {

	public JellyCraftShapedRecipe(String group, ShapedRecipePattern pattern, ItemStack result) {
		super(group, pattern, result);
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
	public Serializer<JellyCraftShapedRecipe> getSerializer() {
		return FDMiscs.SHAPED.get();
	}

}
