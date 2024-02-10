package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import dev.xkmc.fruitsdelight.init.plants.Durian;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2library.repack.registrate.providers.loot.RegistrateBlockLootTables;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeHooks;

import java.util.Locale;

public class DurianLeavesBlock extends BaseLeavesBlock {

	public enum Leaf implements StringRepresentable {
		LEAF, BUDDING, BARE;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public enum Fruit implements StringRepresentable {
		NONE, FLOWERS, FRUITS;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public static final EnumProperty<Fruit> FRUIT = EnumProperty.create("fruit", Fruit.class);
	public static final EnumProperty<Leaf> LEAF = EnumProperty.create("leaf", Leaf.class);

	public DurianLeavesBlock(Properties props) {
		super(props);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(LEAF) != Leaf.BARE || level.getBlockState(pos.above()).is(this);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(PERSISTENT)) return InteractionResult.PASS;
		if (state.getValue(FRUIT) == Fruit.FRUITS) {
			if (level instanceof ServerLevel sl) {
				dropFruit(state, sl, pos, level.getRandom());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LEAF, FRUIT);
	}

	protected boolean doDropFruit(BlockState state, ServerLevel level, BlockPos pos) {
		BlockState curSt = state;
		BlockPos curPos = pos;
		while (!curSt.getMaterial().isReplaceable()) {
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

	protected void dropFruit(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (doDropFruit(state, level, pos)) {
			if (state.getValue(LEAF) == Leaf.BARE) {
				level.removeBlock(pos, false);
			} else {
				level.setBlockAndUpdate(pos, state.setValue(FRUIT, Fruit.NONE));
			}
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
				boolean grow = random.nextDouble() < FDModConfig.COMMON.fruitsGrowChance.get();
				if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
					level.setBlockAndUpdate(pos, state.setValue(FRUIT, Fruit.FRUITS));
					ForgeHooks.onCropsGrowPost(level, pos, state);
					return;
				}
			}
			if (st == Fruit.FRUITS) {
				if (random.nextDouble() < FDModConfig.COMMON.fruitsDropChance.get()) {
					dropFruit(state, level, pos, random);
					return;
				}
			}

			Leaf leaf = state.getValue(LEAF);
			if (st == Fruit.NONE && leaf == Leaf.BUDDING) {
				boolean grow = random.nextDouble() < FDModConfig.COMMON.fruitsGrowChance.get();
				if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
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
		var fruits = pvd.models().getBuilder(name + "_fruits")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/durian_base")))
				.texture("top", pvd.modLoc("block/durian_top"))
				.texture("side", pvd.modLoc("block/durian_side"))
				.renderType("cutout");
		pvd.getMultipartBuilder(ctx.get())
				.part().modelFile(leaves).addModel().condition(LEAF, Leaf.LEAF, Leaf.BUDDING).end()
				.part().modelFile(flowers).addModel().condition(FRUIT, Fruit.FLOWERS).end()
				.part().modelFile(fruits).addModel().condition(FRUIT, Fruit.FRUITS).end();
	}

	public void buildLoot(RegistrateBlockLootTables pvd, Block block, Block sapling, Item fruit) {
		var leaves = LootItem.lootTableItem(block)
				.when(LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(LEAF, Leaf.BARE)).invert())
				.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
						.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
								MinMaxBounds.Ints.atLeast(1)))));
		var fruits = LootItem.lootTableItem(fruit)
				.when(LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(FRUIT, Fruit.FRUITS)));
		var drops = AlternativesEntry.alternatives(leaves, fruits);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

}
