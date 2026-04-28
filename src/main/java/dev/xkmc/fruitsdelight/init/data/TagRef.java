package dev.xkmc.fruitsdelight.init.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagRef {

	public static final TagKey<Block> HEAT_SOURCES = modBlockTag("heat_sources");
	public static final TagKey<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");
	public static final TagKey<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");
	public static final TagKey<Block> FEASTS = modBlockTag("feasts");

	public static final TagKey<Block> BLOCK_PIES = modBlockTag("pies");
	public static final TagKey<Block> BLOCK_CABINETS = modBlockTag("cabinets");
	public static final TagKey<Block> BLOCK_WOODEN_CABINETS = modBlockTag("cabinets/wooden");
	public static final TagKey<Block> BLOCK_MUSHROOM_COLONIES = modBlockTag("mushroom_colonies");
	public static final TagKey<Item> ITEM_PIES = modItemTag("pies");
	public static final TagKey<Item> ITEM_CABINETS = modItemTag("cabinets");
	public static final TagKey<Item> ITEM_WOODEN_CABINETS = modItemTag("cabinets/wooden");
	public static final TagKey<Item> ITEM_MUSHROOM_COLONIES = modItemTag("mushroom_colonies");

	public static final TagKey<Item> TOOLS_KNIVES = forgeItemTag("tools/knives");
	public static final TagKey<Item> BREAD = forgeItemTag("bread");
	public static final TagKey<Item> CROPS_ONION = forgeItemTag("crops/onion");
	public static final TagKey<Item> CROPS_TOMATO = forgeItemTag("crops/tomato");
	public static final TagKey<Item> CROPS_RICE = forgeItemTag("crops/rice");
	public static final TagKey<Item> VEGETABLES_BEETROOT = forgeItemTag("vegetables/beetroot");
	public static final TagKey<Item> RAW_PORK = forgeItemTag("raw_pork");
	public static final TagKey<Item> PASTA_RAW_PASTA = forgeItemTag("pasta/raw_pasta");
	public static final TagKey<Item> EGGS = forgeItemTag("eggs");
	public static final TagKey<Item> SALAD_INGREDIENTS = forgeItemTag("salad_ingredients");

	public static final TagKey<Item> SWEETS = modItemTag("sweets");
	public static final TagKey<Item> SNACKS = modItemTag("snacks");

	public static final TagKey<Item> FRUITS = dietTag("fruits");
	public static final TagKey<Item> GRAINS = dietTag("grains");
	public static final TagKey<Item> PROTEINS = dietTag("proteins");
	public static final TagKey<Item> SUGARS = dietTag("sugars");
	public static final TagKey<Item> VEGETABLES = dietTag("vegetables");

	private static TagKey<Item> forgeItemTag(String path) {
		return ItemTags.create(new ResourceLocation("forge", path));
	}

	private static TagKey<Item> modItemTag(String path) {
		return ItemTags.create(new ResourceLocation("farmersdelight", path));
	}

	private static TagKey<Block> modBlockTag(String path) {
		return BlockTags.create(new ResourceLocation("farmersdelight", path));
	}


	private static TagKey<Item> dietTag(String path) {
		return ItemTags.create(new ResourceLocation("diet", path));
	}

}
