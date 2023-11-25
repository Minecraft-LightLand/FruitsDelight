package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.builders.ItemBuilder;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public enum FoodType {
	JUICE(FoodClass.GLASS, 1, 0.2f, 10, false),
	JELLY(FoodClass.GLASS, 2, 0.2f, 20, false, TagGen.JELLY),
	FRUIT(FoodClass.NONE, 4, 0.3f, 0, false),
	SLICE(FoodClass.NONE, 2, 0.3f, 0, true),
	ROLL(FoodClass.NONE, 2, 0.3f, 10, true),
	STICK(FoodClass.STICK, 4, 0.4f, 10, true),
	SWEET(FoodClass.NONE, 6, 0.5f, 20, false),
	CREAM(FoodClass.GLASS, 6, 0.5f, 20, false),
	BOWL(FoodClass.BOWL, 6, 0.5f, 30, false),
	MEAL(FoodClass.BOWL, 10, 0.8f, 40, false),
	;

	private final FoodClass cls;
	private final int food;
	private final float sat;
	private final boolean fast;
	public final TagKey<Item>[] tags;
	public final int effectLevel;

	FoodType(FoodClass cls, int food, float sat, int effectLevel, boolean fast, TagKey<Item>... tags) {
		this.cls = cls;
		this.food = food;
		this.sat = sat;
		this.effectLevel = effectLevel;
		this.fast = fast;
		this.tags = tags;
	}

	public FDFoodItem build(Item.Properties p, FruitType fruit, EffectEntry[] effs, FDFood type) {
		var val = new FoodProperties.Builder();
		val.nutrition(food).saturationMod(sat);
		if (fast) val.fast();
		if (effectLevel > 0)
			for (var e : fruit.eff) {
				val.effect(() -> e.getEffect(effectLevel), e.getChance(effectLevel));
			}
		for (var e : effs) {
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
					}).color(() -> () -> FDFoodItem::color)
					.transform(e -> e.tab(FruitsDelight.TAB.getKey(), x -> e.getEntry().fillItemCategory(overlay, x)));
		}
		return b.defaultModel();
	}
}
