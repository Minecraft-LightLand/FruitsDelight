package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.l2core.serial.recipe.CustomShapedBuilder;
import net.minecraft.world.level.ItemLike;

public class JellyCraftShapedBuilder extends CustomShapedBuilder<JamCraftShapedRecipe> {

	public JellyCraftShapedBuilder(ItemLike result, int count) {
		super(JamCraftShapedRecipe::new, result, count);
	}
}
