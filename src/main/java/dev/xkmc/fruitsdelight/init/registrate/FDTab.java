package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.init.food.FDTrees;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FDTab extends CreativeModeTab {

	public FDTab() {
		super("fruitsdelight.fruits_delight");
	}

	@Override
	public @NotNull ItemStack makeIcon() {
		return FDTrees.LYCHEE.getFruit().getDefaultInstance();
	}

}
