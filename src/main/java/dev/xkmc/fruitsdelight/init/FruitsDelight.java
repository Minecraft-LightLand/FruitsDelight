package dev.xkmc.fruitsdelight.init;

import com.mojang.logging.LogUtils;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(FruitsDelight.MODID)
@Mod.EventBusSubscriber(modid = FruitsDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FruitsDelight {

	public static final String MODID = "fruitsdelight";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public FruitsDelight() {
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {

	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
	}

}
