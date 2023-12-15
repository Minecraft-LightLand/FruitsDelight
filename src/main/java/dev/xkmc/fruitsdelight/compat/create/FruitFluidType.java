package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.AllFluids;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

public class FruitFluidType extends AllFluids.TintedFluidType {

	private final FruitType type;

	public FruitFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, FruitType type) {
		super(properties, stillTexture, flowingTexture);
		this.type = type;
	}

	@Override
	protected int getTintColor(FluidStack fluidStack) {
		return type.color;
	}

	@Override
	protected int getTintColor(FluidState fluidState, BlockAndTintGetter blockAndTintGetter, BlockPos blockPos) {
		return type.color;
	}

}
