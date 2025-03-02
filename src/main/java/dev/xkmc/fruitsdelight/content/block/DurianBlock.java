package dev.xkmc.fruitsdelight.content.block;

import com.mojang.serialization.MapCodec;
import dev.xkmc.fruitsdelight.init.plants.Durian;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DurianBlock extends FallingBlock implements HarvestableBlock {

	public static final VoxelShape SHAPE = Shapes.or(
			box(3, 0, 3, 13, 13, 13),
			box(7, 13, 7, 9, 16, 9)
	);

	public DurianBlock(Properties pProperties) {
		super(pProperties);
	}

	protected void falling(FallingBlockEntity be) {
		be.setHurtsEntities(2, 40);
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return SHAPE;
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos blockPos) {
		return new HarvestResult(Blocks.AIR.defaultBlockState(), List.of(Durian.FRUIT.asStack()));
	}

	@Override
	protected MapCodec<? extends FallingBlock> codec() {
		return null;//TODO
	}
}
