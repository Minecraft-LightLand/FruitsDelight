package dev.xkmc.fruitsdelight.compat.botanypot;

import dev.xkmc.l2core.serial.config.RecordDataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.List;

public record PotRecipeSimple(
		String type, PotItem seed, ArrayList<String> categories,
		int growthTicks, PotDisplaySimple display, ArrayList<PotResult> drops
) implements RecordDataProvider.ConditionalRecord {

	public static PotRecipeSimple of(Item seed, Block display, int tick, List<String> soils, PotResult... drops) {
		return new PotRecipeSimple("botanypots:crop", new PotItem(seed),
				new ArrayList<>(soils),
				tick, new PotDisplaySimple(display), new ArrayList<>(List.of(drops)));
	}

	public static PotRecipeSimple of(Item seed, Block display, int tick, PotResult... drops) {
		return of(seed, display, tick, List.of("dirt"), drops);
	}
	
	public List<ICondition> conditions() {
		return List.of(new ModLoadedCondition("botanypots"));
	}


}
