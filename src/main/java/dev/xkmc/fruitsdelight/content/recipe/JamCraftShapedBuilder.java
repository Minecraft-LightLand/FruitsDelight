package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.l2core.serial.recipe.CustomShapedBuilder;
import net.minecraft.world.level.ItemLike;

public class JamCraftShapedBuilder extends CustomShapedBuilder<JamCraftShapedRecipe> {

	public JamCraftShapedBuilder(ItemLike result, int count) {
		super(JamCraftShapedRecipe::new, result, count);
	}
}
