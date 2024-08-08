package dev.xkmc.fruitsdelight.content.recipe;

import dev.xkmc.l2core.serial.recipe.CustomShapelessBuilder;
import net.minecraft.world.level.ItemLike;

public class JamCraftShapelessBuilder extends CustomShapelessBuilder<JamCraftShapelessRecipe> {

	public JamCraftShapelessBuilder(ItemLike result, int count) {
		super(JamCraftShapelessRecipe::new, result, count);
	}

}
