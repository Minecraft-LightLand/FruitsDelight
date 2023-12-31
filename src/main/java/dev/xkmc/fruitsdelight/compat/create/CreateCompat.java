package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.content.fluids.VirtualFluid;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.FluidBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.FluidEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Locale;

public class CreateCompat {

	public static void init() {
		OpenEndedPipe.registerEffectHandler(new JamEffectHandler());
	}

}
