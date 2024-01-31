package dev.xkmc.fruitsdelight.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

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
