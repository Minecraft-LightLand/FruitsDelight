package dev.xkmc.fruitsdelight.init;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.providers.ProviderType;
import dev.ghen.thirst.Thirst;
import dev.xkmc.fruitsdelight.compat.botanypot.BotanyGen;
import dev.xkmc.fruitsdelight.compat.thirst.ThirstCompat;
import dev.xkmc.fruitsdelight.events.BlockEffectToClient;
import dev.xkmc.fruitsdelight.init.data.*;
import dev.xkmc.fruitsdelight.init.food.FDCauldrons;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FDJuice;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.plants.*;
import dev.xkmc.fruitsdelight.init.registrate.*;
import dev.xkmc.l2core.init.L2TagGen;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.Reg;
import dev.xkmc.l2serial.network.PacketHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

@Mod(FruitsDelight.MODID)
@EventBusSubscriber(modid = FruitsDelight.MODID, bus = EventBusSubscriber.Bus.MOD)
public class FruitsDelight {

	public static final String MODID = "fruitsdelight";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final Reg REG = new Reg(MODID);
	public static final L2Registrate REGISTRATE = new L2Registrate(MODID);

	public static final PacketHandler HANDLER = new PacketHandler(MODID, 1,
			e -> e.create(BlockEffectToClient.class, PacketHandler.NetDir.PLAY_TO_CLIENT)
	);

	public static final SimpleEntry<CreativeModeTab> TAB =
			REGISTRATE.buildModCreativeTab("fruits_delight", "Fruits Delight",
					e -> e.icon(() -> FDTrees.LYCHEE.getFruit().getDefaultInstance()));

	public FruitsDelight() {
		FDTrees.register();
		FDBushes.register();
		FDMelons.register();
		FDPineapple.register();
		FDItems.register();
		FDEffects.register();
		FDJuice.register();
		FDFood.register();
		FDBlocks.register();
		FDCauldrons.register();
		FDMiscs.register();
		FDModConfig.init();
		FDGLMProvider.register();
		FDFluids.register();

	}

	public static ResourceLocation loc(String id) {
		return ResourceLocation.fromNamespaceAndPath(MODID, id);
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {

			if (FDModConfig.COMMON.enableCauldronRecipe.get())
				FDCauldrons.init();

			//TODO if (ModList.get().isLoaded(Create.ID)) CreateCompat.init();

			if (FDModConfig.COMMON.enableThirstCompat.get() && ModList.get().isLoaded(Thirst.ID)) ThirstCompat.init();
		});
	}

	@SubscribeEvent
	public static void onCauldronRegistration(RegisterCauldronFluidContentEvent event) {
		for (var e : FruitType.values()) {
			event.register(FDCauldrons.JAM[e.ordinal()].get(), FDFluids.JAM[e.ordinal()].get(), 1000, null);
			event.register(FDCauldrons.JELLO[e.ordinal()].get(), FDFluids.JELLO[e.ordinal()].get(), 1000, null);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void gatherData(GatherDataEvent event) {
		REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, TagGen::onBlockTagGen);
		REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, TagGen::onItemTagGen);
		REGISTRATE.addDataGenerator(L2TagGen.EFF_TAGS, TagGen::onEffectTagGen);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipes);
		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, PlantDataEntry::registerComposter);
		var init = REGISTRATE.getDataGenInitializer();
		init.add(Registries.CONFIGURED_FEATURE, ctx -> PlantDataEntry.gen(ctx, PlantDataEntry::registerConfigs));
		init.add(Registries.PLACED_FEATURE, ctx -> PlantDataEntry.gen(ctx, PlantDataEntry::registerPlacements));
		init.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, FDDatapackRegistriesGen::registerBiomeModifiers);


		boolean server = event.includeServer();
		var gen = event.getGenerator();
		PackOutput output = gen.getPackOutput();
		var pvd = event.getLookupProvider();
		var helper = event.getExistingFileHelper();
		gen.addProvider(server, new BotanyGen(output, pvd));
		gen.addProvider(server, new FDConfigGen(gen, pvd));
		gen.addProvider(server, new FDBiomeTagsProvider(output, pvd, helper));
		gen.addProvider(server, new FDGLMProvider(output, pvd));
	}

}
