package dev.xkmc.fruitsdelight.init.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface IFoodClass {

	Item build(@Nullable Block block, Item.Properties food, IFDFood type);

}
