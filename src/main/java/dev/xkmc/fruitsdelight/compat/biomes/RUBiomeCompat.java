package dev.xkmc.fruitsdelight.compat.biomes;

import net.minecraft.resources.ResourceLocation;

public class RUBiomeCompat {

    public static void register(){
        ModBiomeKeys.HOT_HUMID.list.add(e -> e
                .addOptional(ru("bayou"))
                .addOptional(ru("eucalyptus_forest"))
                .addOptional(ru("fen"))
                .addOptional(ru("fungal_fen"))
                .addOptional(ru("magnolia_woodland"))
                .addOptional(ru("marsh"))
                .addOptional(ru("mauve_hills"))
                .addOptional(ru("old_growth_bayou"))
                .addOptional(ru("rainforest"))
                .addOptional(ru("sparse_rainforest"))
                .addOptional(ru("tropics"))
                .addOptional(ru("wisteria_grove"))
                .addOptional(ru("flower_fields"))
        );

        ModBiomeKeys.HOT_DRY.list.add(e -> e
                .addOptional(ru("baobab_savanna"))
                .addOptional(ru("dry_bushland"))
                .addOptional(ru("outback"))
        );

        ModBiomeKeys.WARM.list.add(e -> e
                .addOptional(ru("autumnal_maple_forest"))
                .addOptional(ru("maple_forest"))
                .addOptional(ru("old_growth_forest"))
                .addOptional(ru("orchard"))
                .addOptional(ru("poppy_fields"))
                .addOptional(ru("silver_birch_forest"))
                .addOptional(ru("willow_forest"))
                .addOptional(ru("windswept_maple_forest"))
                .addOptional(ru("shrubland"))
                .addOptional(ru("highland_fields"))
                .addOptional(ru("grassland"))
        );

        ModBiomeKeys.COLD.list.add(e -> e
                .addOptional(ru("boreal_taiga"))
                .addOptional(ru("cold_boreal_taiga"))
                .addOptional(ru("frozen_pine_taiga"))
                .addOptional(ru("tundra"))
                .addOptional(ru("icy_heights"))
                .addOptional(ru("old_growth_boreal_taiga"))
                .addOptional(ru("old_growth_golden_boreal_taiga"))
                .addOptional(ru("pine_slopes"))
                .addOptional(ru("pine_taiga"))
                .addOptional(ru("spires"))
        );

        ModBiomeKeys.BEACH.list.add(e -> e
                .addOptional(ru("grassy_beach"))
        );

        ModBiomeKeys.DESERT.list.add(e -> e
                .addOptional(ru("joshua_desert"))
                .addOptional(ru("saguaro_desert"))
        );
    }

    private static ResourceLocation ru(String str){ return ResourceLocation.fromNamespaceAndPath("regions_unexplored",str);}

}
