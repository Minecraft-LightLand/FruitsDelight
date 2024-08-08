package dev.xkmc.fruitsdelight.compat.botanypot;

import dev.xkmc.l2core.serial.config.RecordDataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BushBlock;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

import java.util.ArrayList;
import java.util.List;

public record PotRecipeBush(
		String type, PotItem seed, ArrayList<String> categories,
		int growthTicks, int lightLevel, PotDisplayBush display, ArrayList<PotResult> drops
) implements RecordDataProvider.ConditionalRecord {

	public static PotRecipeBush of(Item seed, BushBlock display, int tick, int light, PotResult... drops) {
		return new PotRecipeBush("botanypots:crop", new PotItem(seed),
				new ArrayList<>(List.of("dirt")),
				tick, light, PotDisplayBush.of(display), new ArrayList<>(List.of(drops)));
	}

	public List<ICondition> conditions() {
		return List.of(new ModLoadedCondition("botanypots"));
	}

}
