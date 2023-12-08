package dev.xkmc.fruitsdelight.compat.botanypot;

import java.util.ArrayList;
import java.util.List;

public record PotCondition(String type, String modid) {

	public static ArrayList<PotCondition> condition() {
		return new ArrayList<>(List.of(new PotCondition("forge:mod_loaded", "botanypots")));
	}

}
