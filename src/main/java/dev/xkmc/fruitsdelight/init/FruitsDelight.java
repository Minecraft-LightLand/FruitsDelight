package dev.xkmc.fruitsdelight.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import dev.xkmc.fruitsdelight.init.data.*;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.fruitsdelight.init.registrate.FDFood;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
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
		FDFood.register();
		FDModConfig.init();
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PlantDataEntry.run(PlantDataEntry::registerComposter);
		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var output = event.getGenerator().getPackOutput();
		var reg = new FDDatapackRegistriesGen(output, event.getLookupProvider());
		event.getGenerator().addProvider(event.includeServer(), reg);
	}

}
