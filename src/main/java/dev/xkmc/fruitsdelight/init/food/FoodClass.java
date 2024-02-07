package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.content.item.FDBlockItem;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public enum FoodClass implements IFoodClass {
	NONE((b, p, e) -> new FDFoodItem(p, e)),
	STICK((b, p, e) -> new FDFoodItem(p.craftRemainder(Items.STICK), e)),
	GLASS((b, p, e) -> new FDFoodItem(p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), e, UseAnim.DRINK)),
	JELLY((b, p, e) -> new FDBlockItem(b, p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), e, UseAnim.DRINK)),
	BOWL((b, p, e) -> new FDFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16), e)),
	;

	public final IFoodClass factory;

	FoodClass(IFoodClass factory) {
		this.factory = factory;
	}

	public Item build(@Nullable Block block, Item.Properties food, IFDFood type) {
		return factory.build(block, food, type);
	}

}
