package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.entries.FruitFluid;
import dev.xkmc.fruitsdelight.init.entries.FruitFluidType;
import dev.xkmc.fruitsdelight.init.entries.VirtualFluidBuilder;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2library.base.L2Registrate;
import dev.xkmc.l2library.repack.registrate.builders.FluidBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.FluidEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Locale;

public class FDFluids {

	public static final FluidEntry<FruitFluid>[] JAM, JELLO;

	static {

		int size = FruitType.values().length;
		JAM = new FluidEntry[size];
		JELLO = new FluidEntry[size];
		ResourceLocation jelly = new ResourceLocation(FruitsDelight.MODID, "block/jelly");
		ResourceLocation jello = new ResourceLocation(FruitsDelight.MODID, "block/jello");
		for (var e : FruitType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			JAM[e.ordinal()] = virtualFluid(name + "_jam", jelly, jelly,
					(a, b, c) -> new FruitFluidType(a, b, c, e), p -> new FruitFluid(p, e))
					.lang(FDItems.toEnglishName(name) + " Jam")
					.register();
			JELLO[e.ordinal()] = virtualFluid(name + "_jello", jello, jello,
					(a, b, c) -> new FruitFluidType(a, b, c, e), p -> new FruitFluid(p, e))
					.lang("Melted " + FDItems.toEnglishName(name) + " Jello")
					.register();
		}
	}

	private static <T extends FruitFluid> FluidBuilder<T, L2Registrate>
	virtualFluid(String id, ResourceLocation still, ResourceLocation flow,
				 FluidBuilder.FluidTypeFactory typeFactory,
				 NonNullFunction<ForgeFlowingFluid.Properties, T> factory) {
		return FruitsDelight.REGISTRATE.entry(id, (c) -> new VirtualFluidBuilder<>(
				FruitsDelight.REGISTRATE, FruitsDelight.REGISTRATE,
				id, c, still, flow, typeFactory, factory));
	}

	public static void register() {

	}

}
