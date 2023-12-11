package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
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
				CRATES.add(new FDCrates(e, e::getFruit));
		}
		for (var e : FDBushes.values()) {
			CRATES.add(new FDCrates(e, e::getFruit));
		}
		for (var e : FDPineapple.values()) {
			CRATES.add(new FDCrates(e, e::getWholeFruit));
		}
	}

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		for (var e : CRATES) {
			e.genRecipe(pvd);
		}
	}

	private final BlockEntry<Block> block;

	private final Supplier<Item> type;

	public FDCrates(Enum<?> en, Supplier<Item> type) {
		this.type = type;
		String name = en.name().toLowerCase(Locale.ROOT) + "_crate";
		block = FruitsDelight.REGISTRATE.block(name, p -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
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
		pvd.storage(type::get, block);
	}

}
