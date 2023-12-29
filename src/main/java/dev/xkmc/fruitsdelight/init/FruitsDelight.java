package dev.xkmc.fruitsdelight.init;

import com.mojang.logging.LogUtils;
import com.simibubi.create.Create;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.fruitsdelight.compat.botanypot.BotanyGen;
import dev.xkmc.fruitsdelight.compat.create.CreateCompat;
import dev.xkmc.fruitsdelight.init.data.*;
import dev.xkmc.fruitsdelight.init.food.FDCauldrons;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.plants.*;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.fruitsdelight.init.registrate.FDMiscs;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.init.events.EffectSyncEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(FruitsDelight.MODID)
@Mod.EventBusSubscriber(modid = FruitsDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FruitsDelight {

	public static final String MODID = "fruitsdelight";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final RegistryEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("fruits_delight", "Fruits Delight",
					e -> e.icon(() -> FDTrees.LYCHEE.getFruit().getDefaultInstance()));

	public FruitsDelight() {
		FDTrees.register();
		FDBushes.register();
		FDMelons.register();
		FDPineapple.register();
		FDItems.register();
		FDEffects.register();
		FDFood.register();
		FDBlocks.register();
		FDCauldrons.register();
		FDMiscs.register();
		FDModConfig.init();

		if (ModList.get().isLoaded(Create.ID)) {
			CreateCompat.register();
		}

		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PlantDataEntry.run(PlantDataEntry::registerComposter);
			FDCauldrons.init();

			EffectSyncEvents.TRACKED.add(FDEffects.RAGE_AURA.get());
			EffectSyncEvents.TRACKED.add(FDEffects.HEAL_AURA.get());
		});
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		var reg = new FDDatapackRegistriesGen(output, pvd);
		gen.addProvider(server, reg);
		gen.addProvider(server, new BotanyGen(gen));
		gen.addProvider(server, new FDConfigGen(gen));
		gen.addProvider(server, new FDBiomeTagsProvider(output, pvd, helper));
	}

}
