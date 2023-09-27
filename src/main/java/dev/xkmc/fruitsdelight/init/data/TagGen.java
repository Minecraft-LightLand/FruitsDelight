package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class TagGen {

	public static final TagKey<Block> PINEAPPLE_GROW = BlockTags.create(new ResourceLocation(FruitsDelight.MODID, "pineapple_grows_on"));

	public static void onBlockTagGen(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(PINEAPPLE_GROW).add(Blocks.SAND, Blocks.RED_SAND, Blocks.COARSE_DIRT);

	}

}
