package dev.xkmc.fruitsdelight.content.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2harvester.api.HarvestResult;
import dev.xkmc.l2harvester.api.HarvestableBlock;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class PassableLeavesBlock extends BaseLeavesBlock implements BonemealableBlock, HarvestableBlock {

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
	protected InteractionResult doClick(Level level, BlockPos pos, BlockState state) {
		if (state.getValue(STATE) == State.FRUITS) {
			if (level instanceof ServerLevel sl) {
				dropFruit(state, sl, pos, level.getRandom());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public @Nullable HarvestResult getHarvestResult(Level level, BlockState state, BlockPos pos) {
		if (state.getValue(PERSISTENT)) return null;
		if (state.getValue(STATE) != State.FRUITS) return null;
		return new HarvestResult(state.setValue(STATE, State.LEAVES), getDrops(state, (ServerLevel) level, pos, null));
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
				boolean grow = random.nextDouble() < FDModConfig.SERVER.fruitsGrowChance.get();
				if (CommonHooks.canCropGrow(level, pos, state, grow)) {
					level.setBlockAndUpdate(pos, state.setValue(STATE, State.FRUITS));
					var next = findNextFlowerTarget(level, pos,
							e -> !e.getValue(PERSISTENT) && e.getValue(STATE) == State.LEAVES);
					if (next != null) {
						var ns = level.getBlockState(next);
						level.setBlockAndUpdate(next, ns.setValue(STATE, State.FLOWERS));
					}
					CommonHooks.fireCropGrowPost(level, pos, state);
					return;
				}
			}
			if (st == State.FRUITS) {
				if (random.nextDouble() < FDModConfig.SERVER.fruitsDropChance.get()) {
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

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return state.getValue(PERSISTENT) || state.getValue(STATE) == State.FLOWERS;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource r, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource r, BlockPos pos, BlockState state) {
		level.setBlockAndUpdate(pos, state.cycle(STATE));
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
		var helper = new LootHelper(pvd);
		var cond = helper.silk().or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR)));
		var leaves = LootItem.lootTableItem(block).when(cond);
		var fruits = LootItem.lootTableItem(fruit)
				.when(helper.enumState(block, STATE, State.FRUITS))
				.apply(helper.fortuneCount(1));
		var saplings = LootItem.lootTableItem(sapling)
				.when(helper.fortuneChance(20, 16, 12, 10));
		var drops = AlternativesEntry.alternatives(leaves, fruits, saplings);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

}
