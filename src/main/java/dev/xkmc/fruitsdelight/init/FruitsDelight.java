package dev.xkmc.fruitsdelight.init;

import com.mojang.logging.LogUtils;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.data.RecipeGen;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.food.FDCauldrons;
import dev.xkmc.fruitsdelight.init.food.FDCrates;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.plants.*;
import dev.xkmc.fruitsdelight.init.registrate.*;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.base.effects.EffectSyncEvents;
import dev.xkmc.l2library.repack.registrate.providers.ProviderType;
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

	public static final FDTab TAB = new FDTab();

	static {
		REGISTRATE.creativeModeTab(() -> TAB);
	}

	public FruitsDelight() {
		FDTrees.register();
		FDBushes.register();
		FDMelons.register();
		FDPineapple.register();
		FDCrates.register();
		FDBlocks.register();
		FDItems.register();
		FDEffects.register();
		FDFood.register();
		FDCauldrons.register();
		FDMiscs.register();
		FDModConfig.init();
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PlantDataEntry.run(PlantDataEntry::registerConfigs);
			PlantDataEntry.run(PlantDataEntry::registerPlacements);
			PlantDataEntry.run(PlantDataEntry::registerComposter);
			FDCauldrons.init();

			EffectSyncEvents.TRACKED.add(FDEffects.RAGE_AURA.get());
			EffectSyncEvents.TRACKED.add(FDEffects.HEAL_AURA.get());
		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
	}

}
