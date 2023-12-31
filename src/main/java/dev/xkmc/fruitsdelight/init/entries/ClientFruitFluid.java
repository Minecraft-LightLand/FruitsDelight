package dev.xkmc.fruitsdelight.init.entries;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public record ClientFruitFluid(FruitFluidType type) implements IClientFluidTypeExtensions {

	public ResourceLocation getStillTexture() {
		return type.stillTexture;
	}

	public ResourceLocation getFlowingTexture() {
		return type.flowingTexture;
	}

	@Override
	public int getTintColor() {
		return type.type.color;
	}

}

