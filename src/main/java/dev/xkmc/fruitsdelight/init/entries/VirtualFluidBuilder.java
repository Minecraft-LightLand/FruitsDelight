
package dev.xkmc.fruitsdelight.init.entries;

import dev.xkmc.l2library.repack.registrate.AbstractRegistrate;
import dev.xkmc.l2library.repack.registrate.builders.BuilderCallback;
import dev.xkmc.l2library.repack.registrate.builders.FluidBuilder;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullFunction;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class VirtualFluidBuilder<T extends ForgeFlowingFluid, P> extends FluidBuilder<T, P> {

	public VirtualFluidBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback,
							   ResourceLocation stillTexture, ResourceLocation flowingTexture,
							   FluidBuilder.FluidTypeFactory typeFactory,
							   NonNullFunction<ForgeFlowingFluid.Properties, T> factory) {
		super(owner, parent, name, callback, stillTexture, flowingTexture, typeFactory, factory);
		this.source(factory);
	}

	public NonNullSupplier<T> asSupplier() {
		return this::getEntry;
	}

}
