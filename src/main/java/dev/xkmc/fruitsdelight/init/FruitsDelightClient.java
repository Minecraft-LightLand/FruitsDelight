package dev.xkmc.fruitsdelight.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FruitsDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FruitsDelightClient {

	@SubscribeEvent
	public static void registerBlockColor(RegisterColorHandlersEvent.Block event) {
	}

	@SubscribeEvent
	public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
	}

}
