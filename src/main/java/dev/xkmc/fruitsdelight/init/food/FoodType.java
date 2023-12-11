package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public enum FoodType {
	JUICE(FoodClass.GLASS, 6, 0.2f, 10, false, false, TagGen.JUICE),
	JELLY(FoodClass.GLASS, 4, 0.3f, 20, false, false, TagGen.JELLY),
	JELLO(FoodClass.BOWL, 1, 0.3f, 20, true, true),
	FRUIT(FoodClass.NONE, 4, 0.3f, 5, false, false),
	SLICE(FoodClass.NONE, 2, 0.3f, 0, true, false),
	SHEET(FoodClass.NONE, 3, 0.3f, 10, true, false),
	STICK(FoodClass.STICK, 4, 0.4f, 10, true, false),
	SWEET(FoodClass.NONE, 8, 0.5f, 20, false, false),
	CREAM(FoodClass.GLASS, 6, 0.5f, 20, false, false),
	BOWL(FoodClass.BOWL, 8, 0.6f, 30, false, false),
	MEAL(FoodClass.BOWL, 12, 0.8f, 40, false, false),
	DESSERT(FoodClass.NONE, 5, 0.4f, 20, false, false),
	STAPLE(FoodClass.BOWL, 14, 0.8f, 40, false, false),
	ROLL(FoodClass.NONE, 3, 0.4f, 10, true, false),
	COOKIE(FoodClass.NONE, 1, 0.3f, 5, true, false),
	;

	public final int food;
	private final FoodClass cls;
	private final float sat;
	private final boolean fast, alwaysEat;
	public final TagKey<Item>[] tags;
	public final int effectLevel;

	@SafeVarargs
	FoodType(FoodClass cls, int food, float sat, int effectLevel, boolean fast, boolean alwaysEat, TagKey<Item>... tags) {
		this.cls = cls;
		this.food = food;
		this.sat = sat;
		this.effectLevel = effectLevel;
		this.fast = fast;
		this.alwaysEat = alwaysEat;
		this.tags = tags;
	}

	public FDFoodItem build(Item.Properties p, IFDFood type) {
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
		return cls.factory.apply(p.food(val.build()), type);
	}


	public ItemBuilder<FDFoodItem, L2Registrate> model(ItemBuilder<FDFoodItem, L2Registrate> b, int overlay, FruitType fruit) {
		if (this == JELLY) {
			return b.model((ctx, pvd) -> pvd.generated(ctx,
							pvd.modLoc("item/jelly_bottle"),
							pvd.modLoc("item/jelly_content")))
					.color(() -> () -> (stack, layer) -> layer == 0 ? -1 : fruit.color);
		}
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
