package dev.xkmc.fruitsdelight.compat.biomes;

import dev.xkmc.fruitsdelight.init.data.FDBiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Locale;
import java.util.function.Function;

public enum ModBiomeKeys {
	HOT_DRY, HOT_HUMID, WARM, COLD,
	BEACH, DESERT;

	public TagKey<Biome> asTag() {
		return FDBiomeTagsProvider.asTag(name().toLowerCase(Locale.ROOT));
	}

	public static void generate(Function<TagKey<Biome>, TagsProvider.TagAppender<Biome>> pvd) {
		for (var e : ModBiomeKeys.values()) {
			pvd.apply(e.asTag());
		}
	}

}
