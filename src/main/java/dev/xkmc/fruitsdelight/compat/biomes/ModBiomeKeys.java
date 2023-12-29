package dev.xkmc.fruitsdelight.compat.biomes;

import dev.xkmc.fruitsdelight.init.data.FDBiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public enum ModBiomeKeys {
	HOT_DRY, HOT_HUMID, WARM, COLD,
	BEACH, DESERT;

	public final List<Consumer<TagsProvider.TagAppender<Biome>>> list = new ArrayList<>();

	public TagKey<Biome> asTag() {
		return FDBiomeTagsProvider.asTag("compat/" + name().toLowerCase(Locale.ROOT));
	}

	public static void generate(Function<TagKey<Biome>, TagsProvider.TagAppender<Biome>> pvd) {
		BOPBiomeCompat.register();
		TerralithBiomeCompat.register();
		for (var e : ModBiomeKeys.values()) {
			var tag = pvd.apply(e.asTag());
			for (var x : e.list) {
				x.accept(tag);
			}
		}
	}

}
