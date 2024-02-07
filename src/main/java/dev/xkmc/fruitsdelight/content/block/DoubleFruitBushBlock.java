package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.plants.FDBushType;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class DoubleFruitBushBlock extends DoubleBushBlock {

	private static final VoxelShape TALL_SMALL = Shapes.or(
			Block.box(4.0D, 3.0D, 4.0D, 12.0D, 11.0D, 12.0D),
			Block.box(7.0D, 0.0D, 7.0D, 9.0D, 3.0D, 9.0D)
	);

	private static final VoxelShape TALL_MID = Shapes.or(
			Block.box(0.0D, 6.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(7.0D, 0.0D, 7.0D, 9.0D, 6.0D, 9.0D)
	);

	private static final VoxelShape TALL_LOWER = Shapes.or(
			Block.box(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.box(6.0D, 0.0D, 6.0D, 10.0D, 12.0D, 10.0D)
	);

	private static final VoxelShape TALL_UPPER =
			Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);


	private final Supplier<Item> item;
	private final FDBushType type;

	public DoubleFruitBushBlock(Properties properties, Supplier<Item> item, FDBushType type) {
		super(properties);
		this.item = item;
		this.type = type;
		registerDefaultState(defaultBlockState().setValue(AGE, 2));
	}

	@Override
	public int getDoubleBlockStart() {
		return 2;
	}

	@Deprecated
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(AGE) != MAX_AGE && player.getItemInHand(hand).is(Items.BONE_MEAL)) {
			return InteractionResult.PASS;
		}
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			state = level.getBlockState(pos.below());
			pos = pos.below();
		}
		if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER && state.getValue(AGE) == MAX_AGE) {
			if (!level.isClientSide()) {
				int j = 1 + level.random.nextInt(2);
				popResource(level, pos, new ItemStack(item.get(), j));
				level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS,
						1.0F, 0.8F + level.random.nextFloat() * 0.4F);
				setGrowth(level, pos, 2, 2);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return super.use(state, level, pos, player, hand, result);
		}
	}

	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int age = state.getValue(AGE);
		boolean lower = state.getValue(HALF) == DoubleBlockHalf.LOWER;
		return age == 0 ? TALL_SMALL : age == 1 ? TALL_MID :
				lower ? TALL_LOWER : TALL_UPPER;

	}

}
