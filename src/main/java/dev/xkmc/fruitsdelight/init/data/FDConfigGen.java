package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.init.data.NetworkManager;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2library.serial.network.BaseConfig;
import dev.xkmc.l2library.serial.network.ConfigDataProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class FDConfigGen extends ConfigDataProvider {

	public FDConfigGen(DataGenerator generator) {
		super(generator, "data/", "Fruits Delight Config");
	}

	@Override
	public void add(Map<String, BaseConfig> map) {
		var config = new IngredientConfig();
		for (var e : FruitType.values()) {
			Item item = e.fruit.get();
			ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);
			if (rl == null || rl.getNamespace().equals("minecraft"))
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
			Item item = e.getJelly();
			var ing = IngredientConfig.get(Ingredient.of(item), FoodType.VEG,
					0, 180, 60, 0, 0, 0, 0);
			for (var eff : e.eff) {
				var ins = eff.getEffect(20);
				ing.effects.add(new IngredientConfig.EffectEntry(ins.getEffect(), ins.getAmplifier(), ins.getDuration()));
			}
			config.entries.add(ing);
		}
		map.put(FruitsDelight.MODID + "/cuisinedelight_config/" + NetworkManager.INGREDIENT.getID() + "/fruits", config);
	}

}
