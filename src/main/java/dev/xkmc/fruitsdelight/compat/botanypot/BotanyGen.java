package dev.xkmc.fruitsdelight.compat.botanypot;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDMelons;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import dev.xkmc.l2core.serial.config.RecordDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BotanyGen extends RecordDataProvider {

	private static final String PATH = FruitsDelight.MODID + "/recipes/botanypots/";

	public BotanyGen(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd) {
		super(output, pvd, "Botany Tree Gen");
	}

	@Override
	public void add(BiConsumer<String, Record> con) {
		for (var tree : FDTrees.values()) {
			con.accept(PATH + tree.getName(),
					PotRecipeSimple.of(tree.getSapling().asItem(), tree.getSapling(), 2400,
							PotResult.of(1, 1, 2, tree.log.get().asItem()),
							PotResult.of(1, 2, 4, tree.getFruit()),
							PotResult.of(0.15, 1, 1, tree.getSapling().asItem())
					));
		}
		for (var bush : FDBushes.values()) {
			con.accept(PATH + bush.getName(),
					PotRecipeBush.of(bush.getSeed(), bush.getBush(), 1200, 12,
							PotResult.of(1, 1, 1, bush.getFruit()),
							PotResult.of(0.05, 1, 1, bush.getFruit())
					));
		}
		for (var melon : FDMelons.values()) {
			con.accept(PATH + melon.getName(),
					PotRecipeSimple.of(melon.getSeed(), melon.getMelonBlock(), 1200,
							List.of("dirt", "farmland"),
							PotResult.of(1, 3, 6, melon.getSlice())
					));
		}
		for (var pineapple : FDPineapple.values()) {
			con.accept(PATH + pineapple.getName(),
					PotRecipeBush.of(pineapple.getSapling(), pineapple.getPlant(), 1200, 12,
							PotResult.of(1, 2, 4, pineapple.getSlice()),
							PotResult.of(0.1, 1, 1, pineapple.getWholeFruit())
					));
		}
	}

}
