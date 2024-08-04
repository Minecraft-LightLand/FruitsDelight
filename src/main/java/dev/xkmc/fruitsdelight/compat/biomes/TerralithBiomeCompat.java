package dev.xkmc.fruitsdelight.compat.biomes;

import net.minecraft.resources.ResourceLocation;

public class TerralithBiomeCompat {

	public static void register() {
		ModBiomeKeys.HOT_HUMID.list.add(e -> e
				.addOptional(tl("orchid_swamp"))
				.addOptional(tl("amethyst_rainforest"))
				.addOptional(tl("jungle_mountains"))
				.addOptional(tl("tropical_jungle"))
		);

		ModBiomeKeys.HOT_DRY.list.add(e -> e
				.addOptional(tl("ashen_savanna"))
				.addOptional(tl("hot_shrubland"))
				.addOptional(tl("savanna_slopes"))
				.addOptional(tl("shrubland"))
				.addOptional(tl("fractured_savanna"))
		);

		ModBiomeKeys.WARM.list.add(e -> e
				.addOptional(tl("blooming_plateau"))
				.addOptional(tl("blooming_valley"))
				.addOptional(tl("lavender_forest"))
				.addOptional(tl("lavender_valley"))
				.addOptional(tl("lush_valley.json"))
				.addOptional(tl("moonlight_grove"))
				.addOptional(tl("moonlight_valley"))
		);

		ModBiomeKeys.COLD.list.add(e -> e
				.addOptional(tl("alpha_islands_winter"))
				.addOptional(tl("alpine_grove"))
				.addOptional(tl("alpine_highlands"))
				.addOptional(tl("birch_taiga"))
				.addOptional(tl("cloud_forest"))
				.addOptional(tl("forested_highlands"))
				.addOptional(tl("haze_mountain"))
				.addOptional(tl("highlands"))
				.addOptional(tl("siberian_grove"))
				.addOptional(tl("siberian_taiga"))
				.addOptional(tl("snowy_cherry_grove"))
				.addOptional(tl("snowy_maple_forest"))
				.addOptional(tl("wintry_forest"))
				.addOptional(tl("wintry_lowlands"))
		);

		ModBiomeKeys.DESERT.list.add(e -> e
				.addOptional(tl("desert_canyon"))
				.addOptional(tl("desert_oasis"))
				.addOptional(tl("desert_spires"))
				.addOptional(tl("lush_desert"))
				.addOptional(tl("sandstone_valley"))
		);

	}

	private static ResourceLocation tl(String str) {
		return ResourceLocation.fromNamespaceAndPath("terralith", str);
	}


}
