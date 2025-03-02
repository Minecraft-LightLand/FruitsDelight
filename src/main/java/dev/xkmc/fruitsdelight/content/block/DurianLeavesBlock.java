package dev.xkmc.fruitsdelight.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import dev.xkmc.fruitsdelight.init.plants.Durian;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class DurianLeavesBlock extends BaseLeavesBlock implements HarvestableBlock {

	public enum Leaf implements StringRepresentable {
		LEAF, BUDDING, BARE;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public enum Fruit implements StringRepresentable {
		NONE, FLOWERS, SMALL, FRUITS;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public static final EnumProperty<Fruit> FRUIT = EnumProperty.create("fruit", Fruit.class);
	public static final EnumProperty<Leaf> LEAF = EnumProperty.create("leaf", Leaf.class);

	private static final VoxelShape BID = Block.box(3, 3, 3, 13, 16, 13);

	public DurianLeavesBlock(Properties props) {
		super(props);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(LEAF) == Leaf.BARE ? BID : super.getShape(state, level, pos, ctx);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(LEAF) != Leaf.BARE || level.getBlockState(pos.above()).is(this);
	}

	@Override
	protected InteractionResult doClick(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(FRUIT) == Fruit.FRUITS) {
			if (level instanceof ServerLevel sl) {
				dropFruit(state, sl, pos);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		if (state.getValue(PERSISTENT)) return null;
		if (state.getValue(FRUIT) != Fruit.FRUITS) return null;
		return new HarvestResult((l, p) -> postDropFruit(state, (ServerLevel) l, p),
				List.of(Durian.FRUIT.asStack()));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LEAF, FRUIT);
	}

	protected boolean doDropFruit(BlockState state, ServerLevel level, BlockPos pos) {
		BlockState curSt = state;
		BlockPos curPos = pos;
		while (!curSt.canBeReplaced()) {
			if (!(curSt.getBlock() instanceof BaseLeavesBlock)) {
				return false;
			}
			curPos = curPos.below();
			curSt = level.getBlockState(curPos);
		}
		FallingBlockEntity.fall(level, pos, Durian.FRUIT.getDefaultState());
		return true;
	}

	protected boolean createFlower(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.isOutsideBuildHeight(pos.below())) return false;
		BlockState below = level.getBlockState(pos.below());
		if (below.isAir()) {
			BlockState next = state.setValue(WATERLOGGED, false)
					.setValue(LEAF, Leaf.BARE).setValue(FRUIT, Fruit.FLOWERS);
			level.setBlockAndUpdate(pos.below(), next);
			return true;
		}
		if (below.is(this) && below.getValue(FRUIT) == Fruit.NONE) {
			level.setBlockAndUpdate(pos.below(), below.setValue(FRUIT, Fruit.FLOWERS));
			return true;
		}
		return false;
	}

	protected void dropFruit(BlockState state, ServerLevel level, BlockPos pos) {
		if (doDropFruit(state, level, pos)) {
			postDropFruit(state, level, pos);
		}
	}

	protected void postDropFruit(BlockState state, ServerLevel level, BlockPos pos) {
		if (state.getValue(LEAF) == Leaf.BARE) {
			level.removeBlock(pos, false);
		} else {
			level.setBlockAndUpdate(pos, state.setValue(FRUIT, Fruit.NONE));
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		if (state.getValue(PERSISTENT)) return false;
		if (state.getValue(LEAF) == Leaf.BUDDING) return true;
		if (state.getValue(FRUIT) != Fruit.NONE) return true;
		return super.isRandomlyTicking(state);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(PERSISTENT) && !decaying(state)) {
			Fruit st = state.getValue(FRUIT);
			if (st == Fruit.FLOWERS) {
				boolean grow = random.nextDouble() < FDModConfig.SERVER.fruitsGrowChance.get();
				if (CommonHooks.canCropGrow(level, pos, state, grow)) {
					level.setBlockAndUpdate(pos, state.setValue(FRUIT, Fruit.SMALL));
					CommonHooks.fireCropGrowPost(level, pos, state);
					return;
				}
			}
			if (st == Fruit.SMALL) {
				boolean grow = random.nextDouble() < FDModConfig.SERVER.fruitsGrowChance.get();
				if (CommonHooks.canCropGrow(level, pos, state, grow)) {
					if (FDModConfig.SERVER.fruitsDropChance.get() < FDModConfig.SERVER.fruitsGrowChance.get()) {
						level.setBlockAndUpdate(pos, state.setValue(FRUIT, Fruit.FRUITS));
					} else {
						dropFruit(state, level, pos);
					}
					CommonHooks.fireCropGrowPost(level, pos, state);
					return;
				}
			}
			if (st == Fruit.FRUITS) {
				if (random.nextDouble() < FDModConfig.SERVER.fruitsDropChance.get()) {
					dropFruit(state, level, pos);
					return;
				}
			}

			Leaf leaf = state.getValue(LEAF);
			if (st == Fruit.NONE && leaf == Leaf.BUDDING) {
				boolean grow = random.nextDouble() < FDModConfig.SERVER.fruitsGrowChance.get();
				if (CommonHooks.canCropGrow(level, pos, state, grow)) {
					if (createFlower(state, level, pos, random)) {
						level.setBlockAndUpdate(pos, state.setValue(LEAF, Leaf.LEAF));
						var next = findNextFlowerTarget(level, pos,
								e -> !e.getValue(PERSISTENT) && e.getValue(LEAF) == Leaf.LEAF);
						if (next != null) {
							var ns = level.getBlockState(next);
							level.setBlockAndUpdate(next, ns.setValue(LEAF, Leaf.BUDDING));
						}
						return;
					}
				}

			}
		}
		super.randomTick(state, level, pos, random);
	}

	@Override
	public BlockState flowerState() {
		return defaultBlockState().setValue(LEAF, Leaf.BUDDING);
	}

	public void buildLeavesModel(DataGenContext<Block, ? extends BaseLeavesBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		var leaves = pvd.models().withExistingParent(name + "_leaves", "block/leaves")
				.texture("all", "block/" + name + "_leaves");
		var flowers = pvd.models().withExistingParent(name + "_flowers", "block/cross")
				.texture("cross", "block/" + name + "_flowers")
				.renderType("cutout");
		var small = pvd.models().getBuilder(name + "_small")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/durian_small_base")))
				.texture("top", pvd.modLoc("block/durian_top_small"))
				.texture("side", pvd.modLoc("block/durian_side_small"))
				.renderType("cutout");
		var fruits = pvd.models().getBuilder(name + "_fruits")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/durian_base")))
				.texture("top", pvd.modLoc("block/durian_top"))
				.texture("side", pvd.modLoc("block/durian_side"))
				.renderType("cutout");
		pvd.getMultipartBuilder(ctx.get())
				.part().modelFile(leaves).addModel().condition(LEAF, Leaf.LEAF, Leaf.BUDDING).end()
				.part().modelFile(flowers).addModel().condition(FRUIT, Fruit.FLOWERS).end()
				.part().modelFile(small).addModel().condition(FRUIT, Fruit.SMALL).end()
				.part().modelFile(fruits).addModel().condition(FRUIT, Fruit.FRUITS).end();
	}

	public void buildLoot(RegistrateBlockLootTables pvd, Block block, Block sapling, Item fruit) {
		var helper = new LootHelper(pvd);
		var cond = helper.silk().or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR)));
		var leaves = LootItem.lootTableItem(block)
				.when(helper.enumState(block, LEAF, Leaf.BARE).invert())
				.when(cond);
		var fruits = LootItem.lootTableItem(fruit)
				.when(helper.enumState(block, FRUIT, Fruit.FRUITS));
		var drops = AlternativesEntry.alternatives(leaves, fruits);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

}
