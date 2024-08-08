package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class JellyBottleBlock extends Block {

	public static final VoxelShape SHAPE = Block.box(3, 0, 3, 13, 15, 13);

	public final FruitType fruit;

	public JellyBottleBlock(Properties prop, FruitType fruit) {
		super(prop);
		this.fruit = fruit;
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}
}
