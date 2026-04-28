package dev.xkmc.fruitsdelight.init.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

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

	public static final TagKey<Item> KNIFE = commonItemTag("tools/knife");
	public static final TagKey<Item> CROPS_RICE = commonItemTag("crops/rice");
	public static final TagKey<Item> FOODS_RAW_PORK = commonItemTag("foods/raw_pork");
	public static final TagKey<Item> FOODS_PASTA = commonItemTag("foods/pasta");
	public static final TagKey<Item> FOODS_MILK = Tags.Items.DRINKS_MILK;
	public static final TagKey<Item> FOODS_LEAFY_GREEN = commonItemTag("foods/leafy_green");
	public static final TagKey<Item> FOODS_ONION = commonItemTag("foods/onion");
	public static final TagKey<Item> FOODS_TOMATO = commonItemTag("foods/tomato");

	public static final TagKey<Item> SWEETS = modItemTag("sweets");
	public static final TagKey<Item> SNACKS = modItemTag("snacks");

	public static final TagKey<Item> FRUITS = dietTag("fruits");
	public static final TagKey<Item> GRAINS = dietTag("grains");
	public static final TagKey<Item> PROTEINS = dietTag("proteins");
	public static final TagKey<Item> SUGARS = dietTag("sugars");
	public static final TagKey<Item> VEGETABLES = dietTag("vegetables");

	private static TagKey<Item> modItemTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("farmersdelight", path));
	}

	private static TagKey<Block> modBlockTag(String path) {
		return BlockTags.create(ResourceLocation.fromNamespaceAndPath("farmersdelight", path));
	}

	private static TagKey<Item> commonItemTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
	}

	private static TagKey<Item> dietTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("diet", path));
	}

}
