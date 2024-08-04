package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class FDCrates {

	private static final List<FDCrates> CRATES = new ArrayList<>();

	public static void register() {
		for (var e : FDTrees.values()) {
			if (e.genTree)
				CRATES.add(new FDCrates(e.name().toLowerCase(Locale.ROOT), e::getFruit));
		}
		for (var e : FDBushes.values()) {
			CRATES.add(new FDCrates(e.name().toLowerCase(Locale.ROOT), e::getFruit));
		}
		for (var e : FDPineapple.values()) {
			CRATES.add(new FDCrates(e.name().toLowerCase(Locale.ROOT), e::getWholeFruit));
		}
		CRATES.add(new FDCrates("apple", () -> Items.APPLE));
	}

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		for (var e : CRATES) {
			e.genRecipe(pvd);
		}
	}

	private final BlockEntry<Block> block;

	private final Supplier<Item> type;

	public FDCrates(String str, Supplier<Item> type) {
		this.type = type;
		String name = str + "_crate";
		block = FruitsDelight.REGISTRATE.block(name, p -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
						.strength(2.0F, 3.0F).sound(SoundType.WOOD)))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeBottomTop(ctx.getName(),
						pvd.modLoc("block/" + name + "_side"),
						pvd.modLoc("block/crate_bottom"),
						pvd.modLoc("block/" + name + "_top"))))
				.tag(BlockTags.MINEABLE_WITH_AXE)
				.simpleItem()
				.register();
	}

	public void genRecipe(RegistrateRecipeProvider pvd) {
		pvd.storage(type::get, RecipeCategory.MISC, block);
	}

}
