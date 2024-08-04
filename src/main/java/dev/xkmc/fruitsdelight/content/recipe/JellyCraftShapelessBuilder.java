package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.l2core.serial.recipe.CustomShapelessBuilder;
import net.minecraft.world.level.ItemLike;

public class JellyCraftShapelessBuilder extends CustomShapelessBuilder<JellyCraftShapelessRecipe> {

	public JellyCraftShapelessBuilder(ItemLike result, int count) {
		super(JellyCraftShapelessRecipe::new, result, count);
	}

}
