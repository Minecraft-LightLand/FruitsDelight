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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.ForgeHooks;

import java.util.Locale;

public class PassableLeavesBlock extends BaseLeavesBlock {

	public enum State implements StringRepresentable {
		LEAVES, FLOWERS, FRUITS;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}

	public static final EnumProperty<State> STATE = EnumProperty.create("type", State.class);

	public PassableLeavesBlock(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (state.getValue(PERSISTENT)) return InteractionResult.PASS;
		if (state.getValue(STATE) == State.FRUITS) {
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
		builder.add(STATE);
	}

	protected void doDropFruit(BlockState state, ServerLevel level, BlockPos pos) {
		dropResources(state, level, pos);
	}

	protected void dropFruit(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		doDropFruit(state, level, pos);
		level.setBlockAndUpdate(pos, state.setValue(STATE, State.LEAVES));
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		if (state.getValue(PERSISTENT)) return false;
		if (state.getValue(STATE) != State.LEAVES) return true;
		return super.isRandomlyTicking(state);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(PERSISTENT) && !decaying(state)) {
			State st = state.getValue(STATE);
			if (st == State.FLOWERS) {
				boolean grow = random.nextDouble() < FDModConfig.COMMON.fruitsGrowChance.get();
				if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
					level.setBlockAndUpdate(pos, state.setValue(STATE, State.FRUITS));
					var next = findNextFlowerTarget(level, pos,
							e -> !e.getValue(PERSISTENT) && e.getValue(STATE) == State.LEAVES);
					if (next != null) {
						var ns = level.getBlockState(next);
						level.setBlockAndUpdate(next, ns.setValue(STATE, State.FLOWERS));
					}
					ForgeHooks.onCropsGrowPost(level, pos, state);
					return;
				}
			}
			if (st == State.FRUITS) {
				if (random.nextDouble() < FDModConfig.COMMON.fruitsDropChance.get()) {
					dropFruit(state, level, pos, random);
					return;
				}
			}
		}
		super.randomTick(state, level, pos, random);
	}

	@Override
	public BlockState flowerState() {
		return defaultBlockState().setValue(PassableLeavesBlock.STATE, PassableLeavesBlock.State.FLOWERS);
	}

	protected ConfiguredModel[] buildModel(RegistrateBlockstateProvider pvd, String treeName, BlockState state) {
		String name = treeName + "_" +
				state.getValue(PassableLeavesBlock.STATE).getSerializedName();
		return ConfiguredModel.builder()
				.modelFile(pvd.models().withExistingParent(name, "block/leaves")
						.texture("all", "block/" + name)).build();
	}

	public void buildLeavesModel(DataGenContext<Block, ? extends BaseLeavesBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> buildModel(pvd, name, state),
						LeavesBlock.DISTANCE, LeavesBlock.PERSISTENT, LeavesBlock.WATERLOGGED);
	}

	public void buildLoot(RegistrateBlockLootTables pvd, Block block, Block sapling, Item fruit) {
		var leaves = LootItem.lootTableItem(block)
				.when(MatchTool.toolMatches(ItemPredicate.Builder.item()
						.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
								MinMaxBounds.Ints.atLeast(1)))));
		var fruits = LootItem.lootTableItem(fruit)
				.when(LootItemBlockStatePropertyCondition
						.hasBlockStateProperties(block)
						.setProperties(StatePropertiesPredicate.Builder.properties()
								.hasProperty(PassableLeavesBlock.STATE, PassableLeavesBlock.State.FRUITS)))
				.apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1));
		var saplings = LootItem.lootTableItem(sapling)
				.when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE,
						1 / 20f, 1 / 16f, 1 / 12f, 1 / 10f));
		var drops = AlternativesEntry.alternatives(leaves, fruits, saplings);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

}
