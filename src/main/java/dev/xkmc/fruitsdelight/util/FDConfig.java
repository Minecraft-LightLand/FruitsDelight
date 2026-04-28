package dev.xkmc.fruitsdelight.util;

import vectorwing.farmersdelight.common.Configuration;

public class FDConfig {

	public static boolean addTooltip() {
		try {
			return Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get();
		} catch (Throwable ignored) {
		}
		return true;
	}

}
