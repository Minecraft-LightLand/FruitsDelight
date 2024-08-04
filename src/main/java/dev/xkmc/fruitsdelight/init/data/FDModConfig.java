package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class FDModConfig {

	public static class Common extends ConfigInit {

		public final ModConfigSpec.BooleanValue enableCauldronRecipe;
		public final ModConfigSpec.BooleanValue enableThirstCompat;

		Common(Builder builder) {
			markPlain();

			enableCauldronRecipe = builder.text("Enable cauldron jam and jello recipe")
					.define("enableCauldronRecipe", true);
			enableThirstCompat = builder.text("Enable Thirst Compat")
					.define("enableThirstCompat", true);
		}

	}

	public static class Server extends ConfigInit {

		public final ModConfigSpec.DoubleValue fruitsGrowChance;
		public final ModConfigSpec.DoubleValue fruitsDropChance;
		public final ModConfigSpec.DoubleValue flowerDecayChance;
		public final ModConfigSpec.DoubleValue peachGrowChance;
		public final ModConfigSpec.DoubleValue peachFruitChance;
		public final ModConfigSpec.DoubleValue peachDecayChance;
		public final ModConfigSpec.IntValue rageEffectRange;
		public final ModConfigSpec.IntValue healEffectRange;
		public final ModConfigSpec.IntValue alienatingEffectRange;

		Server(Builder builder) {
			markPlain();
			fruitsGrowChance = builder.text("Chance for fruits to grow per random tick")
					.defineInRange("fruitsGrowChance", 0.1, 0, 1);
			fruitsDropChance = builder.text("Chance for fruits to drop per random tick")
					.defineInRange("fruitsDropChance", 0.1, 0, 1);
			flowerDecayChance = builder.text("Chance for flower to decay when fruits are picked up / dropped")
					.defineInRange("flowerDecayChance", 0.1, 0, 1);

			peachGrowChance = builder.text("Chance for peach to grow per random tick")
					.defineInRange("peachGrowChance", 0.1, 0, 1);
			peachFruitChance = builder.text("Chance for peach to grow fruit when stop flowering")
					.defineInRange("peachFruitChance", 0.3, 0, 1);
			peachDecayChance = builder.text("Chance for peach to decay when fruiting")
					.defineInRange("peachDecayChance", 0.1, 0, 1);
			rageEffectRange = builder.text("Effect application range for Rage Aura effect")
					.defineInRange("rageEffectRange", 24, 0, 64);
			healEffectRange = builder.text("Effect application range for Heal Aura effect")
					.defineInRange("healEffectRange", 6, 0, 64);
			alienatingEffectRange = builder.text("Effect application range for Alienating effect")
					.defineInRange("alienatingEffectRange", 12, 0, 64);
		}

	}

	public static final Common COMMON = FruitsDelight.REGISTRATE.registerUnsynced(Common::new);
	public static final Server SERVER = FruitsDelight.REGISTRATE.registerSynced(Server::new);

	public static void init() {
	}


}
