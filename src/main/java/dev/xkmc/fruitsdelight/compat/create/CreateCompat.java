package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import dev.xkmc.fruitsdelight.init.entries.FruitFluid;

public class CreateCompat {

	public static void init() {
		OpenPipeEffectHandler.REGISTRY.registerProvider(fluid -> fluid instanceof FruitFluid ? new JamEffectHandler() : null);
	}

}
