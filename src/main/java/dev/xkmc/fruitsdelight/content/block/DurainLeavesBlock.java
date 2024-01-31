package dev.xkmc.fruitsdelight.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
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
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;

import java.util.Locale;

public class DurainLeavesBlock extends BaseLeavesBlock {

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

	public DurainLeavesBlock(Properties props) {
		super(props);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return state.getValue(LEAF) != Leaf.BARE || level.getBlockState(pos.above()).is(this);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
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
		//TODO
		return false;
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
						if (random.nextDouble() < FDModConfig.COMMON.flowerDecayChance.get()) {
							level.setBlockAndUpdate(pos, state.setValue(LEAF, Leaf.LEAF));
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
				.texture("all", "block/" + name + "_flowers");
		var fruits = pvd.models().withExistingParent(name + "_flowers", "block/cross")
				.texture("all", "block/" + name + "_flowers");//TODO
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
		var saplings = LootItem.lootTableItem(sapling)
				.when(LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(LEAF, Leaf.BARE)).invert())
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
						1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f));
		var drops = AlternativesEntry.alternatives(leaves, saplings);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

}
