package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2library.serial.recipe.CustomShapelessBuilder;
import net.minecraft.world.level.ItemLike;

public class JellyCraftShapelessBuilder extends CustomShapelessBuilder<JellyCraftShapelessRecipe> {

	public JellyCraftShapelessBuilder(ItemLike result, int count) {
		super(FDMiscs.SHAPELESS, result, count);
	}

}
