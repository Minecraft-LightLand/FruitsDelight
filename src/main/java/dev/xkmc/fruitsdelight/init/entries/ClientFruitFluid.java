package dev.xkmc.fruitsdelight.init.entries;

import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

public record ClientFruitFluid(
		FruitType type,
		ResourceLocation stillTexture,
		ResourceLocation flowingTexture
) implements IClientFluidTypeExtensions {

	public ResourceLocation getStillTexture() {
		return stillTexture;
	}

	public ResourceLocation getFlowingTexture() {
		return flowingTexture;
	}

	@Override
	public int getTintColor() {
		return type.color;
	}

}

