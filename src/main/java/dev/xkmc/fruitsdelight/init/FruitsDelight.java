package dev.xkmc.fruitsdelight.init;

import com.mojang.logging.LogUtils;
import dev.xkmc.fruitsdelight.init.data.FDDatapackRegistriesGen;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.fruitsdelight.init.registrate.FDTrees;
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
		FDBlocks.register();
		FDItems.register();
		FDModConfig.init();
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {

	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var output = event.getGenerator().getPackOutput();
		var reg = new FDDatapackRegistriesGen(output, event.getLookupProvider());
		event.getGenerator().addProvider(event.includeServer(), reg);
	}

}
