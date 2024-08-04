package dev.xkmc.fruitsdelight.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public abstract class BaseLeavesBlock extends LeavesBlock {

	public BaseLeavesBlock(Properties props) {
		super(props);
	}

	protected boolean canPassThrough(@Nullable Entity e) {
		if (e == null) return false;
		if (e instanceof ItemEntity) return true;
		if (e instanceof FallingBlockEntity) return true;
		return false;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (state.getValue(PERSISTENT)) return InteractionResult.PASS;
		return doClick(level, pos, state);
	}

	@Override
	public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
		if (pState.getValue(PERSISTENT)) return;
		doClick(pLevel, pPos, pState);
	}

	protected abstract InteractionResult doClick(Level level, BlockPos pos, BlockState state);

	@Nullable
	protected BlockPos findNextFlowerTarget(Level level, BlockPos pos, Predicate<BlockState> pred) {
		if (FDModConfig.SERVER.flowerDecayChance.get() > level.getRandom().nextDouble())
			return null;
		var builder = SimpleWeightedRandomList.<BlockPos>builder();
		for (var e : Direction.values()) {
			var ipos = pos.relative(e);
			var istate = level.getBlockState(ipos);
			if (!istate.is(this)) continue;
			if (!pred.test(istate)) continue;
			builder.add(ipos, istate.getValue(DISTANCE) + 2);
		}
		return builder.build().getRandomValue(level.random).orElse(null);
	}

	@Deprecated
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		if (ctx instanceof EntityCollisionContext ectx && canPassThrough(ectx.getEntity())) {
			return Shapes.empty();
		}
		return super.getCollisionShape(state, level, pos, ctx);
	}

	public abstract void buildLeavesModel(DataGenContext<Block, ? extends BaseLeavesBlock> ctx, RegistrateBlockstateProvider pvd, String name);

	public abstract void buildLoot(RegistrateBlockLootTables pvd, Block block, Block sapling, Item fruit);

	public abstract BlockState flowerState();

}
