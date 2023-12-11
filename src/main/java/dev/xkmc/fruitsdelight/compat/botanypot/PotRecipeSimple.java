package dev.xkmc.fruitsdelight.compat.botanypot;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public record PotRecipeSimple(ArrayList<PotCondition> conditions, String type, PotItem seed,
							  ArrayList<String> categories,
							  int growthTicks, PotDisplaySimple display, ArrayList<PotResult> drops) {

	public static PotRecipeSimple of(Item seed, Block display, int tick, List<String> soils, PotResult... drops) {
		return new PotRecipeSimple(PotCondition.condition(), "botanypots:crop", new PotItem(seed),
				new ArrayList<>(soils),
				tick, new PotDisplaySimple(display), new ArrayList<>(List.of(drops)));
	}

	public static PotRecipeSimple of(Item seed, Block display, int tick, PotResult... drops) {
		return of(seed, display, tick, List.of("dirt"), drops);
	}

}
