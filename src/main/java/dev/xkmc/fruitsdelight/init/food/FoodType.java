package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.compat.diet.DietTagGen;
import dev.xkmc.fruitsdelight.content.item.FDBlockItem;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum FoodType {
	JUICE(FoodClass.GLASS, 6, 0.2f, 10, false, true, TagGen.JUICE, DietTagGen.FRUITS.tag),
	JELLY(FoodClass.JELLY, 4, 0.3f, 20, false, false, TagGen.JELLY, DietTagGen.FRUITS.tag, DietTagGen.SUGARS.tag),
	JELLO(FoodClass.BOWL, 1, 0.3f, 20, true, true, TagGen.JELLO, DietTagGen.SUGARS.tag),
	FRUIT(FoodClass.NONE, 4, 0.3f, 5, false, false, DietTagGen.FRUITS.tag),
	SLICE(FoodClass.NONE, 2, 0.3f, 0, true, false, DietTagGen.FRUITS.tag),
	SHEET(FoodClass.NONE, 3, 0.3f, 10, true, false, DietTagGen.FRUITS.tag),
	STICK(FoodClass.STICK, 4, 0.4f, 10, true, false, DietTagGen.FRUITS.tag),
	SWEET(FoodClass.NONE, 8, 0.5f, 20, false, false, DietTagGen.FRUITS.tag, DietTagGen.GRAINS.tag, DietTagGen.SUGARS.tag),
	CREAM(FoodClass.GLASS, 6, 0.5f, 20, false, false, DietTagGen.FRUITS.tag, DietTagGen.SUGARS.tag),
	BOWL(FoodClass.BOWL, 8, 0.6f, 30, false, false, DietTagGen.FRUITS.tag),
	MEAL(FoodClass.BOWL, 12, 0.8f, 40, false, false, DietTagGen.FRUITS.tag, DietTagGen.PROTEINS.tag),
	DESSERT(FoodClass.NONE, 5, 0.4f, 20, false, false, DietTagGen.FRUITS.tag, DietTagGen.GRAINS.tag, DietTagGen.SUGARS.tag),
	STAPLE(FoodClass.BOWL, 14, 0.8f, 40, false, false, DietTagGen.FRUITS.tag, DietTagGen.GRAINS.tag, DietTagGen.VEGETABLES.tag),
	ROLL(FoodClass.NONE, 3, 0.4f, 10, true, false, DietTagGen.FRUITS.tag, DietTagGen.SUGARS.tag),
	COOKIE(FoodClass.NONE, 1, 0.3f, 5, true, false, DietTagGen.SUGARS.tag),
	MANGOSTEEN_CAKE((b, p, e) -> new FDBlockItem(FDBlocks.MANGOSTEEN_CAKE.get(), p.craftRemainder(Items.BOWL).stacksTo(16), e),
			9, 0.6f, 30, false, false, DietTagGen.FRUITS.tag),
	;

	public final int food;
	private final IFoodClass cls;
	private final float sat;
	private final boolean fast, alwaysEat;
	public final TagKey<Item>[] tags;
	public final int effectLevel;

	@SafeVarargs
	FoodType(IFoodClass cls, int food, float sat, int effectLevel, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this.cls = cls;
		this.food = food;
		this.sat = sat;
		this.effectLevel = effectLevel;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.tags = tags;
	}

	public Item build(Item.Properties p, IFDFood type) {
		return build(p, type, null);
	}

	public Item build(Item.Properties p, IFDFood type, @Nullable Block block) {
		var val = new FoodProperties.Builder();
		val.nutrition(food).saturationMod(sat);
		if (fast) val.fast();
		if (alwaysEat) val.alwaysEat();
		if (effectLevel > 0)
			for (var e : type.fruit().eff) {
				val.effect(() -> e.getEffect(effectLevel), e.getChance(effectLevel));
			}
		for (var e : type.getEffects()) {
			val.effect(e::getEffect, e.chance());
		}
		return cls.build(block, p.food(val.build()), type);
	}


	public <T> ItemBuilder<Item, T> model(ItemBuilder<Item, T> b, int overlay, FruitType fruit) {
		if (overlay > 0) {
			return b.model((ctx, pvd) -> {
				ResourceLocation[] res = new ResourceLocation[overlay + 1];
				res[0] = pvd.modLoc("item/" + ctx.getName());
				for (int i = 1; i <= overlay; i++) {
					res[i] = pvd.modLoc("item/" + ctx.getName() + "_filler_" + (i - 1));
				}
				pvd.generated(ctx, res);
			}).color(() -> () -> FDFoodItem::color);
		}
		return b.defaultModel();
	}
}
