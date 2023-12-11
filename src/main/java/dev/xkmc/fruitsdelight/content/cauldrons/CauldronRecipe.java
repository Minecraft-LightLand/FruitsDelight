package dev.xkmc.fruitsdelight.content.cauldrons;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CauldronRecipe(Item block, Item item, int count, Item outBlock, ItemStack outItem, boolean requiresHeat) {

	public static final List<CauldronRecipe> LIST = new ArrayList<>();

	public static void create(Map<Item, CauldronInteraction> map, Item block, Item item, int count, FDCauldronInteraction action, Item outBlock) {
		map.put(item, action);
		LIST.add(new CauldronRecipe(block, item, count, outBlock, action.result(), action.requiresHeat()));
	}

	public static void create(Map<Item, CauldronInteraction> map, Item block, Item item, FDCauldronInteraction action, Item outBlock) {
		create(map, block, item, 1, action, outBlock);
	}

	public static void create(FDCauldronBlock block, Item item, int count, FDCauldronInteraction action, Item outBlock) {
		create(block.getInteractions(), block.asItem(), item, count, action, outBlock);
	}

	public static void empty(JellyCauldronBlock block, Item item, int count, FDCauldronInteraction action, Item outBlock) {
		create(block.getInteractions(), block.asItem(), item, count, action, outBlock);
	}

	public ItemStack getBlockInput() {
		return new ItemStack(block);
	}

	public ItemStack getItemInput() {
		return new ItemStack(item, count);
	}

	public ItemStack getBlockOutput() {
		return new ItemStack(outBlock);
	}

	public ItemStack getItemOutput() {
		if (outBlock == Items.CAULDRON) {
			ItemStack ans = outItem().copy();
			ans.setCount(count);
			return ans;
		}
		return outItem();
	}

}
