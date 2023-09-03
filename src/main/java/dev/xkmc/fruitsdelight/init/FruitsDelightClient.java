package dev.xkmc.fruitsdelight.init;

import dev.xkmc.fruitsdelight.init.registrate.FDMelons;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StemBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FruitsDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FruitsDelightClient {

	@SubscribeEvent
	public static void registerBlockColor(RegisterColorHandlersEvent.Block event) {
		FDMelons.registerColor(event);
	}

	@SubscribeEvent
	public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
	}

}
