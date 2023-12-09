package dev.xkmc.fruitsdelight.compat.sereneseasons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.fruitsdelight.init.food.FDBushes;
import dev.xkmc.fruitsdelight.init.food.FDMelons;
import dev.xkmc.fruitsdelight.init.food.FDPineapple;
import dev.xkmc.fruitsdelight.init.food.FDTrees;
import net.minecraft.world.level.block.Block;

public class SeasonCompat {

	private static final Multimap<Seasons, FDTrees> TREE = new ImmutableMultimap.Builder<Seasons, FDTrees>()
			.putAll(Seasons.SPRING, FDTrees.PEAR, FDTrees.PEACH)
			.putAll(Seasons.SUMMER, FDTrees.LYCHEE, FDTrees.MANGO, FDTrees.MANGOSTEEN)
			.putAll(Seasons.AUTUMN, FDTrees.ORANGE, FDTrees.APPLE)
			.putAll(Seasons.WINTER, FDTrees.HAWBERRY, FDTrees.PERSIMMON)
			.build();

	private static final Multimap<Seasons, FDBushes> BUSH = new ImmutableMultimap.Builder<Seasons, FDBushes>()
			.putAll(Seasons.SPRING, FDBushes.BLUEBERRY)
			.putAll(Seasons.SUMMER, FDBushes.LEMON)
			.putAll(Seasons.AUTUMN, FDBushes.CRANBERRY)
			.build();

	private static final Multimap<Seasons, FDMelons> MELON = new ImmutableMultimap.Builder<Seasons, FDMelons>()
			.putAll(Seasons.SUMMER, FDMelons.HAMIMELON)
			.build();

	private static final Multimap<Seasons, FDPineapple> PINEAPPLE = new ImmutableMultimap.Builder<Seasons, FDPineapple>()
			.putAll(Seasons.SUMMER, FDPineapple.PINEAPPLE)
			.build();

	public static void genItem(RegistrateItemTagsProvider pvd) {
		for (var s : Seasons.values()) {
			var item = pvd.addTag(s.item);
			for (var t : TREE.get(s)) {
				item.add(t.getSapling().asItem());
			}
			for (var t : BUSH.get(s)) {
				item.add(t.getSeed());
			}
			for (var t : MELON.get(s)) {
				item.add(t.getSeed());
			}
			for (var t : PINEAPPLE.get(s)) {
				item.add(t.getSapling());
			}
		}
	}

	public static void genBlock(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		for (var s : Seasons.values()) {
			var block = pvd.addTag(s.block);
			for (var t : TREE.get(s)) {
				block.add(t.getSapling());
				block.add(t.getLeaves());
			}
			for (var t : BUSH.get(s)) {
				block.add(t.getBush());
			}
			for (var t : MELON.get(s)) {
				block.add(t.getStem());
			}
			for (var t : PINEAPPLE.get(s)) {
				block.add(t.getPlant());
			}
		}
	}

}
