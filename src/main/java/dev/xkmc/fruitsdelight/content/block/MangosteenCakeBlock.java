package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.food.FDFood;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MangosteenCakeBlock extends BaseCakeBlock {

	public static final int MAX_BITES = 3;
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, MAX_BITES);
	protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D),
			Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D)};

	public MangosteenCakeBlock(Properties properties) {
		super(properties, BITES, MAX_BITES);
	}

	public VoxelShape getShape(BlockState p_51222_, BlockGetter p_51223_, BlockPos p_51224_, CollisionContext p_51225_) {
		return SHAPE_BY_BITE[p_51222_.getValue(BITES)];
	}

	@Override
	protected void eat(Player player) {
		player.getFoodData().eat(3, 0.6F);
		var food = FDFood.MANGOSTEEN_CAKE.item.get().getFoodProperties();
		if (food == null || player.level.isClientSide) return;
		for (var e : food.getEffects()) {
			var ins = e.getFirst();
			var f = e.getSecond();
			if (ins == null) continue;
			if (player.level.getRandom().nextDouble() < f) {
				player.addEffect(new MobEffectInstance(ins.getEffect(), ins.getAmplifier(), ins.getDuration() / 3));
			}
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, BITES);
	}

}