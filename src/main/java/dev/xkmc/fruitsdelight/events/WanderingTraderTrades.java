package dev.xkmc.fruitsdelight.events;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(modid = FruitsDelight.MODID)
public class WanderingTraderTrades {

	private static final int MAX_USES = 8;
	private static final int VILLAGER_XP = 1;
	private static final float PRICE_MULTIPLIER = 0.05F;

	@SubscribeEvent
	public static void onWandererTrades(WandererTradesEvent event) {
		var trades = event.getGenericTrades();
		for (var tree : FDTrees.values()) {
			trades.add(new BasicItemListing(5, new ItemStack(tree.getSapling()),
					MAX_USES, VILLAGER_XP, PRICE_MULTIPLIER));
		}
		for (var bush : FDBushes.values()) {
			trades.add(new BasicItemListing(1, new ItemStack(bush.getFruit()),
					MAX_USES, VILLAGER_XP, PRICE_MULTIPLIER));
		}
	}

}
