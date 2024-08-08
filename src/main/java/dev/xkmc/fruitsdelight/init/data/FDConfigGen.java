package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.cuisinedelight.content.logic.CookTransformConfig;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.CuisineDelight;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2core.serial.config.ConfigDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class FDConfigGen extends ConfigDataProvider {

	public FDConfigGen(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pvd) {
		super(generator, pvd, "Fruits Delight Config");
	}

	@Override
	public void add(Collector collector) {
		var config = new IngredientConfig();
		var transform = new CookTransformConfig();
		for (var e : FruitType.values()) {
			Item item = e.getFruit();
			ResourceLocation rl = BuiltInRegistries.ITEM.getKey(item);
			if (rl.getNamespace().equals("minecraft"))
				continue;
			var ing = IngredientConfig.get(Ingredient.of(item), FoodType.VEG,
					60, 180, 60, 0, 0.5f, 1, 6);
			for (var eff : e.eff) {
				var ins = eff.getEffect(20);
				ing.effects.add(new IngredientConfig.EffectEntry(ins.getEffect(), ins.getAmplifier(), ins.getDuration()));
			}
			config.entries.add(ing);
		}
		for (var e : FruitType.values()) {
			Item item = e.getJam();
			var ing = IngredientConfig.get(Ingredient.of(item), FoodType.VEG,
					0, 180, 60, 0, 0, 0, 0);
			for (var eff : e.eff) {
				var ins = eff.getEffect(20);
				ing.effects.add(new IngredientConfig.EffectEntry(ins.getEffect(), ins.getAmplifier(), ins.getDuration()));
			}
			config.entries.add(ing);
			transform.fluid(item, e.color);
		}
		collector.add(CuisineDelight.INGREDIENT, FruitsDelight.loc("fruits"), config);
		collector.add(CuisineDelight.TRANSFORM, FruitsDelight.loc("jam"), transform);
	}

}
