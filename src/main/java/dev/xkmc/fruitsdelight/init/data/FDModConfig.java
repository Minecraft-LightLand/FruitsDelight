package dev.xkmc.fruitsdelight.init.data;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class FDModConfig {

	public static class Client {

		Client(ForgeConfigSpec.Builder builder) {
		}

	}

	public static class Common {

		public final ForgeConfigSpec.DoubleValue fruitsGrowChance;
		public final ForgeConfigSpec.DoubleValue fruitsDropChance;
		public final ForgeConfigSpec.DoubleValue flowerDecayChance;
		public final ForgeConfigSpec.IntValue effectRange;

		Common(ForgeConfigSpec.Builder builder) {
			fruitsGrowChance = builder.comment("Chance for fruits to grow per random tick")
					.defineInRange("fruitsGrowChance", 0.1, 0, 1);
			fruitsDropChance = builder.comment("Chance for fruits to drop per random tick")
					.defineInRange("fruitsDropChance", 0.1, 0, 1);
			flowerDecayChance = builder.comment("Chance for flower to decay when fruits are picked up / dropped")
					.defineInRange("flowerDecayChance", 0.1, 0, 1);
			effectRange = builder.comment("Effect application range for ranged potion effects")
					.defineInRange("effectRange", 6, 0, 64);
		}

	}

	public static final ForgeConfigSpec CLIENT_SPEC;
	public static final Client CLIENT;

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Client, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = client.getRight();
		CLIENT = client.getLeft();

		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
	}


}
