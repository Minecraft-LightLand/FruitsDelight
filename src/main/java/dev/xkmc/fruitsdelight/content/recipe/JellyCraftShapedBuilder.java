package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.l2core.serial.recipe.CustomShapedBuilder;
import net.minecraft.world.level.ItemLike;

public class JellyCraftShapedBuilder extends CustomShapedBuilder<JellyCraftShapedRecipe> {

	public JellyCraftShapedBuilder(ItemLike result, int count) {
		super(JellyCraftShapedRecipe::new, result, count);
	}
}
