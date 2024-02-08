package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.food.FDFood;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.tag.ForgeTags;

public class FigPuddingBlock extends BaseCakeBlock {

	public static final int MAX_BITES = 3;
	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, MAX_BITES);
	protected static final VoxelShape[] SHAPE_BY_BITE = new VoxelShape[]{
			Block.box(3, 0, 3, 13, 6, 13),
			Block.box(3, 0, 3, 13, 6, 13),
			Block.box(3, 0, 3, 13, 6, 13),
			Block.box(3, 0, 3, 13, 6, 13)};

	public FigPuddingBlock(Properties properties) {
		super(properties, BITES, MAX_BITES, false);
	}

	public VoxelShape getShape(BlockState p_51222_, BlockGetter p_51223_, BlockPos p_51224_, CollisionContext p_51225_) {
		return SHAPE_BY_BITE[p_51222_.getValue(BITES)];
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(ForgeTags.TOOLS_KNIVES)) {
			int i = state.getValue(bite);
			if (i > 0) {
				level.setBlockAndUpdate(pos, state.setValue(bite, i - 1));
			} else {
				level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
				level.destroyBlock(pos, true);
			}
			popResource(level, pos, FDFood.FIG_PUDDING_SLICE.get().getDefaultInstance());
		}
		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	protected void eat(Player player) {
		player.eat(player.level(), FDFood.FIG_PUDDING_SLICE.item.asStack());
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, BITES);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING,
				pContext.getHorizontalDirection().getOpposite());
	}

}