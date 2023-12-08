package dev.xkmc.fruitsdelight.compat.botanypot;

import net.minecraft.world.item.Item;

public record PotResult(double chance, int minRolls, int maxRolls, PotItem output) {

	public static PotResult of(double chance, int min, int max, Item output) {
		return new PotResult(chance, min, max, new PotItem(output));
	}

}
