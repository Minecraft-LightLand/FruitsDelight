package dev.xkmc.fruitsdelight.compat.botanypot;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDMelons;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import net.darkhax.botanypots.common.impl.data.display.types.BasicOptions;
import net.darkhax.botanypots.common.impl.data.display.types.SimpleDisplayState;
import net.darkhax.botanypots.common.impl.data.itemdrops.SimpleDropProvider;
import net.darkhax.botanypots.common.impl.data.recipe.crop.BasicCrop;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BotanyGen {

	public static void onRecipeGen(RegistrateRecipeProvider pvd) {
		for (var tree : FDTrees.values()) {
			pvd.accept(FruitsDelight.loc("botanypots/" + tree.getName()),
					new BasicCrop(new BasicCrop.Properties(
							Ingredient.of(tree.getSapling().asItem()),
							BasicCrop.DIRT, 2400,
							List.of(new SimpleDisplayState(tree.getSapling().defaultBlockState(), BasicOptions.ofDefault())), 7,
							List.of(new SimpleDropProvider(List.of(
									of(tree.log.get(), 1, 1),
									of(tree.log.get(), 1, 0.5f),
									of(tree.getFruit(), 2, 1),
									of(tree.getFruit(), 2, 0.5f),
									of(tree.getSapling(), 1, 0.15f)
							))),
							Optional.empty(),
							Optional.empty(),
							1, 0)),
					null, new ModLoadedCondition("botanypots")
			);
		}
		for (var bush : FDBushes.values()) {
			pvd.accept(FruitsDelight.loc("botanypots/" + bush.getName()),
					new BasicCrop(new BasicCrop.Properties(
							Ingredient.of(bush.getSeed()),
							BasicCrop.DIRT, 1200,
							List.of(new SimpleDisplayState(bush.getBush().defaultBlockState(), BasicOptions.ofDefault())), 12,
							List.of(new SimpleDropProvider(List.of(
									of(bush.getFruit(), 1, 1),
									of(bush.getFruit(), 1, 0.05f)
							))),
							Optional.empty(),
							Optional.empty(),
							1, 0)),
					null, new ModLoadedCondition("botanypots")
			);
		}
		for (var melon : FDMelons.values()) {
			pvd.accept(FruitsDelight.loc("botanypots/" + melon.getName()),
					new BasicCrop(new BasicCrop.Properties(
							Ingredient.of(melon.getSeed()),
							BasicCrop.DIRT, 1200,
							List.of(new SimpleDisplayState(melon.getMelonBlock().defaultBlockState(), BasicOptions.ofDefault())), 7,
							List.of(new SimpleDropProvider(List.of(
									of(melon.getSlice(), 3, 1),
									of(melon.getSlice(), 1, 0.5f),
									of(melon.getSlice(), 1, 0.5f),
									of(melon.getSlice(), 1, 0.5f)
							))),
							Optional.empty(),
							Optional.empty(),
							1, 0)),
					null, new ModLoadedCondition("botanypots")
			);
		}
		for (var pineapple : FDPineapple.values()) {
			pvd.accept(FruitsDelight.loc("botanypots/" + pineapple.getName()),
					new BasicCrop(new BasicCrop.Properties(
							Ingredient.of(pineapple.getSapling()),
							BasicCrop.DIRT, 1200,
							List.of(new SimpleDisplayState(pineapple.getPlant().defaultBlockState(), BasicOptions.ofDefault())), 12,
							List.of(new SimpleDropProvider(List.of(
									of(pineapple.getSlice(), 2, 1),
									of(pineapple.getSlice(), 1, 0.5f),
									of(pineapple.getSlice(), 1, 0.5f),
									of(pineapple.getWholeFruit(), 1, 0.1f)
							))),
							Optional.empty(),
							Optional.empty(),
							1, 0)),
					null, new ModLoadedCondition("botanypots")
			);
		}
	}

	private static SimpleDropProvider.SimpleDrop of(ItemLike item, int count, float chance) {
		return new SimpleDropProvider.SimpleDrop(
				item.asItem().getDefaultInstance().copyWithCount(count), chance
		);
	}

}
