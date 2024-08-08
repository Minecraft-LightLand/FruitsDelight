package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.l2core.serial.recipe.CustomShapelessBuilder;
import net.minecraft.world.level.ItemLike;

public class JellyCraftShapelessBuilder extends CustomShapelessBuilder<JamCraftShapelessRecipe> {

	public JellyCraftShapelessBuilder(ItemLike result, int count) {
		super(JamCraftShapelessRecipe::new, result, count);
	}

}
