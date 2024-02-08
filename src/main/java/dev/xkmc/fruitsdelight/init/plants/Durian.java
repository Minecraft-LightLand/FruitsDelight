package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.block.DurianBlock;
import dev.xkmc.fruitsdelight.content.item.DurianHelmetItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.SeparateTransformsModelBuilder;

import java.util.function.Supplier;

public class Durian {

	public static BlockEntry<DurianBlock> FRUIT;
	public static ItemEntry<DurianHelmetItem> UPPER;
	public static ItemEntry<Item> LOWER;

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
		UPPER = FruitsDelight.REGISTRATE.item("upper_durian_shell", DurianHelmetItem::new)
				.model((ctx, pvd) -> pvd.getBuilder(ctx.getName())
						.customLoader(SeparateTransformsModelBuilder::begin)
						.base(new ItemModelBuilder(null, pvd.existingFileHelper)
								.parent(new ModelFile.UncheckedModelFile("item/generated"))
								.texture("layer0", pvd.modLoc("item/durian_shell_upper")))
						.perspective(ItemDisplayContext.HEAD,
								new ItemModelBuilder(null, pvd.existingFileHelper)
										.texture("upper", pvd.modLoc("item/durian_shell_upper"))
										.texture("inner", pvd.modLoc("item/durian_helmet_inner"))
										.texture("top", pvd.modLoc("block/durian_top"))
						))
				.register();
		LOWER = FruitsDelight.REGISTRATE.item("lower_durian_shell", Item::new)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/durian_shell_lower")))
				.register();
		return FRUIT::asItem;
	}

	public static ItemBuilder<? extends BlockItem, BlockBuilder<SaplingBlock, L2Registrate>> sapling(
			BlockBuilder<SaplingBlock, L2Registrate> builder) {
		return builder.item(ItemNameBlockItem::new)
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/durian_seed")))
				.defaultLang();
	}

	public static Item getSlice() {
		return FDFood.DURIAN_FLESH.get();
	}

}
