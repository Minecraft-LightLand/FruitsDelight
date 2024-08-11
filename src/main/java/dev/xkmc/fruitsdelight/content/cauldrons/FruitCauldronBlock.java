package dev.xkmc.fruitsdelight.content.cauldrons;

import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.fluids.CauldronFluidContent;

public class FruitCauldronBlock extends FDCauldronBlock {

	public static final int MAX = 12;

	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, MAX);

	public final FruitType type;

	public FruitCauldronBlock(Properties properties, FruitType type, String name) {
		super(properties, name);
		this.type = type;
		registerDefaultState(defaultBlockState().setValue(LEVEL, MAX));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
	}

}
