package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2library.base.recipe.CustomShapedBuilder;
import net.minecraft.world.level.ItemLike;

public class JellyCraftShapedBuilder extends CustomShapedBuilder<JellyCraftShapedRecipe> {

	public JellyCraftShapedBuilder(ItemLike result, int count) {
		super(FDMiscs.SHAPED, result, count);
	}
}
