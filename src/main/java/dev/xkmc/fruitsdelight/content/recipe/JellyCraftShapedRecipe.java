package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2library.serial.recipe.AbstractShapedRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class JellyCraftShapedRecipe extends AbstractShapedRecipe<JellyCraftShapedRecipe> {

	public JellyCraftShapedRecipe(ResourceLocation rl, String group, int w, int h, NonNullList<Ingredient> ingredients, ItemStack result) {
		super(rl, group, w, h, ingredients, result);
	}

	@Override
	public ItemStack assemble(CraftingContainer cont, RegistryAccess access) {
		ItemStack stack = super.assemble(cont, access);
		ListTag list = new ListTag();
		for (int i = 0; i < cont.getContainerSize(); i++) {
			ItemStack input = cont.getItem(i);
			if (!input.isEmpty() && input.getItem() instanceof FDFoodItem item && item.food != null && input.is(TagGen.JELLY)) {
				list.add(StringTag.valueOf(item.food.fruit.name()));
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
