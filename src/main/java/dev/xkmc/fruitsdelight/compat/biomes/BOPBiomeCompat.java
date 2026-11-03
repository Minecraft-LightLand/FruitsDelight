package dev.xkmc.fruitsdelight.compat.biomes;

import net.minecraft.resources.ResourceLocation;

public class BOPBiomeCompat {

	public static void register() {
		ModBiomeKeys.HOT_HUMID.list.add(e -> e
				.addOptional(bop("rainforest"))
				.addOptional(bop("rocky_rainforest"))
				.addOptional(bop("tropics"))
				.addOptional(bop("wetland"))
				.addOptional(bop("redwood_forest"))
				.addOptional(bop("floodplain"))
		);//Aspen Grade


		ModBiomeKeys.HOT_DRY.list.add(e -> e
				.addOptional(bop("lush_savanna"))
				.addOptional(bop("shrubland"))
				.addOptional(bop("scrubland"))
				.addOptional(bop("rocky_shrubland"))
				.addOptional(bop("dryland"))
		);

		ModBiomeKeys.WARM.list.add(e -> e
				.addOptional(bop(""))
				.addOptional(bop("lavender_forest"))
				.addOptional(bop("mediterranean_forest"))
				.addOptional(bop("old_growth_woodland"))
				.addOptional(bop("orchard"))
				.addOptional(bop("seasonal_orchard"))
				.addOptional(bop("maple_woods"))
				.addOptional(bop("woodland"))
				.addOptional(bop("seasonal_forest"))
				.addOptional(bop("forested_field"))
		);
		// lavender_field
		//Bamboo Grove | Cherry Blossom Grove

		ModBiomeKeys.COLD.list.add(e -> e
				.addOptional(bop("snowy_coniferous_forest"))
				.addOptional(bop("snowy_fir_clearing"))
				.addOptional(bop("snowy_maple_woods"))
				.addOptional(bop("snowblossom_grove"))
				.addOptional(bop("tundra"))
				.addOptional(bop("coniferous_forest"))
		);

		ModBiomeKeys.BEACH.list.add(e -> e
				.addOptional(bop("dune_beach"))
		);
		ModBiomeKeys.DESERT.list.add(e -> e
				.addOptional(bop("lush_desert"))
				.addOptional(bop("cold_desert"))
		);


	}

	private static ResourceLocation bop(String str) {
		return ResourceLocation.fromNamespaceAndPath("biomesoplenty", str);
	}

/*
	public static final ResourceKey<Biome> AURORAL_GARDEN = registerOverworld("auroral_garden");
	public static final ResourceKey<Biome> BAYOU = registerOverworld("bayou");
	public static final ResourceKey<Biome> BOG = registerOverworld("bog");
	public static final ResourceKey<Biome> CLOVER_PATCH = registerOverworld("clover_patch");
	public static final ResourceKey<Biome> CRAG = registerOverworld("crag");
	public static final ResourceKey<Biome> FIELD = registerOverworld("field");
	public static final ResourceKey<Biome> FIR_CLEARING = registerOverworld("fir_clearing");
	public static final ResourceKey<Biome> GLOWING_GROTTO = register("glowing_grotto");
	public static final ResourceKey<Biome> GRASSLAND = registerOverworld("grassland");
	public static final ResourceKey<Biome> HIGHLAND = registerOverworld("highland");
	public static final ResourceKey<Biome> MARSH = registerOverworld("marsh");
	public static final ResourceKey<Biome> MOOR = registerOverworld("moor");
	public static final ResourceKey<Biome> MUSKEG = registerOverworld("muskeg");
	public static final ResourceKey<Biome> MYSTIC_GROVE = registerOverworld("mystic_grove");
	public static final ResourceKey<Biome> OMINOUS_WOODS = registerOverworld("ominous_woods");
	public static final ResourceKey<Biome> ORIGIN_VALLEY = registerOverworld("origin_valley");
	public static final ResourceKey<Biome> PASTURE = registerOverworld("pasture");
	public static final ResourceKey<Biome> PRAIRIE = registerOverworld("prairie");
	public static final ResourceKey<Biome> PUMPKIN_PATCH = registerOverworld("pumpkin_patch");
	public static final ResourceKey<Biome> UNDERGROWTH = register("undergrowth");
	public static final ResourceKey<Biome> VISCERAL_HEAP = register("visceral_heap");
 */

}
