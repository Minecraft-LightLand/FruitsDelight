package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.fruitsdelight.compat.diet.DietTagGen;
import dev.xkmc.fruitsdelight.compat.sereneseasons.SeasonCompat;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class TagGen {

	public static final TagKey<Block> PINEAPPLE_GROW = BlockTags.create(FruitsDelight.loc("pineapple_grows_on"));

	public static final TagKey<Item> JELLY = ItemTags.create(FruitsDelight.loc("jelly"));
	public static final TagKey<Item> JUICE = ItemTags.create(FruitsDelight.loc("juice"));
	public static final TagKey<Item> ALLOW_JELLY = ItemTags.create(FruitsDelight.loc("allow_jelly"));

	public static final TagKey<Item> JELLO = ItemTags.create(FruitsDelight.loc("jello"));


	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(PINEAPPLE_GROW).add(Blocks.SAND, Blocks.RED_SAND, Blocks.COARSE_DIRT);
		SeasonCompat.genBlock(pvd);
	}

	public static void onItemTagGen(RegistrateItemTagsProvider pvd) {
		pvd.addTag(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "berries")))
				.add(FDBushes.BLUEBERRY.getFruit(), FDBushes.CRANBERRY.getFruit());
		SeasonCompat.genItem(pvd);
		pvd.addTag(DietTagGen.GRAINS.tag).add(FDFood.MANGOSTEEN_CAKE.item.get());
		pvd.addTag(DietTagGen.SUGARS.tag).add(FDFood.PEAR_WITH_ROCK_SUGAR.get());
	}

}
