package dev.xkmc.fruitsdelight.init.entries;

import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.function.Consumer;

public class FruitFluidType extends FluidType {
	final ResourceLocation stillTexture;
	final ResourceLocation flowingTexture;
	final FruitType type;

	public FruitFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, FruitType type) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.type = type;
	}

	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {//TODO
		consumer.accept(new ClientFruitFluid(this));
	}

}
