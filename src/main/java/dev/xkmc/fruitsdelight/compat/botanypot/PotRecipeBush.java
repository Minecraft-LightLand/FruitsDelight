package dev.xkmc.fruitsdelight.compat.botanypot;

import dev.xkmc.fruitsdelight.content.block.BaseBushBlock;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public record PotRecipeBush(ArrayList<PotCondition> conditions, String type, PotItem seed, ArrayList<String> categories,
							int growthTicks, int lightLevel, PotDisplayBush display, ArrayList<PotResult> drops) {

	public static PotRecipeBush of(Item seed, BaseBushBlock display, int tick, int light, PotResult... drops) {
		return new PotRecipeBush(PotCondition.condition(), "botanypots:crop", new PotItem(seed),
				new ArrayList<>(List.of("dirt")),
				tick, light, PotDisplayBush.of(display), new ArrayList<>(List.of(drops)));
	}

}
