package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.entries.FruitFluid;
import dev.xkmc.fruitsdelight.init.entries.FruitFluidType;
import dev.xkmc.fruitsdelight.init.entries.VirtualFluidBuilder;
import dev.xkmc.fruitsdelight.init.food.FDJuice;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import java.util.Locale;

public class FDFluids {

	public static final FluidEntry<FruitFluid>[] JAM, JELLO, JUICE;

	static {

		int size = FruitType.values().length;
		JAM = new FluidEntry[size];
		JELLO = new FluidEntry[size];
		ResourceLocation jam = FruitsDelight.loc("block/jam");
		ResourceLocation jello = FruitsDelight.loc("block/jello");
		for (var e : FruitType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			JAM[e.ordinal()] = virtualFluid(name + "_jam", jam, jam,
					(a, b, c) -> new FruitFluidType(a.descriptionId(Util.makeDescriptionId("block", b)), b, c, e), p -> new FruitFluid(p, e))
					.register();
			JELLO[e.ordinal()] = virtualFluid(name + "_jello", jello, jello,
					(a, b, c) -> new FruitFluidType(a, b, c, e), p -> new FruitFluid(p, e))
					.lang("Melted " + FDItems.toEnglishName(name) + " Jello")
					.register();
		}
	}

	static {
		int size = FDJuice.values().length;
		JUICE = new FluidEntry[size];
		ResourceLocation still = ResourceLocation.withDefaultNamespace("block/water_still");
		ResourceLocation flow = ResourceLocation.withDefaultNamespace("block/water_flow");
		for (var e : FDJuice.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			JUICE[e.ordinal()] = virtualFluid(name, still, flow,
					(a, b, c) -> new FruitFluidType(a, b, c, e.fruit()), p -> new FruitFluid(p, e.fruit))
					.lang(FDItems.toEnglishName(name))
					.register();
		}
	}

	private static <T extends FruitFluid> FluidBuilder<T, L2Registrate>
	virtualFluid(String id, ResourceLocation still, ResourceLocation flow,
				 FluidBuilder.FluidTypeFactory typeFactory,
				 NonNullFunction<BaseFlowingFluid.Properties, T> factory) {
		return FruitsDelight.REGISTRATE.entry(id, (c) -> new VirtualFluidBuilder<>(
				FruitsDelight.REGISTRATE, FruitsDelight.REGISTRATE,
				id, c, still, flow, typeFactory, factory));
	}

	public static void register() {

	}

}
