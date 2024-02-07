package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.DurianBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class Durian {

	public static BlockEntry<DurianBlock> FRUIT;

	public static Supplier<Item> buildItem(String s) {
		FRUIT = FruitsDelight.REGISTRATE.block(s, p ->
						new DurianBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).noOcclusion()))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
						.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/durian_base")))
						.texture("top", pvd.modLoc("block/durian_top"))
						.texture("side", pvd.modLoc("block/durian_side"))
						.renderType("cutout")
				)).tag(BlockTags.MINEABLE_WITH_AXE)
				.simpleItem().register();
		return FRUIT::asItem;
	}

	public static Item getSlice() {
		return FDFood.DURIAN_FLESH.get();
	}

}
