package dev.xkmc.fruitsdelight.init.entries;

import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FruitFluid extends ForgeFlowingFluid {

	public final FruitType type;

	public FruitFluid(ForgeFlowingFluid.Properties properties, FruitType type) {
		super(properties);
		this.type = type;
	}

	public Fluid getSource() {
		return super.getSource();
	}

	public Fluid getFlowing() {
		return this;
	}

	public Item getBucket() {
		return Items.AIR;
	}

	protected BlockState createLegacyBlock(FluidState state) {
		return Blocks.AIR.defaultBlockState();
	}

	public boolean isSource(FluidState state) {
		return false;
	}

	public int getAmount(FluidState state) {
		return 0;
	}
}
