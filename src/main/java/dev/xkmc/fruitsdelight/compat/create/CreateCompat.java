package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.content.fluids.OpenEndedPipe;

public class CreateCompat {

	public static void init() {
		OpenEndedPipe.registerEffectHandler(new JamEffectHandler());
	}

}
