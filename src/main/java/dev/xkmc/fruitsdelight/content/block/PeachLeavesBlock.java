package dev.xkmc.fruitsdelight.content.block;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.ForgeHooks;

import java.util.*;

public class PeachLeavesBlock extends PassableLeavesBlock {

	public static final BooleanProperty FERTILE = BooleanProperty.create("fertile");

	public PeachLeavesBlock(Properties props) {
		super(props);
		registerDefaultState(defaultBlockState().setValue(FERTILE, true));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FERTILE);
		super.createBlockStateDefinition(builder);
	}

	private List<BlockPos> scanLeaves(Level level, BlockPos pos) {
		List<BlockPos> ans = new ArrayList<>();
		BlockState state = level.getBlockState(pos);
		if (!state.is(this)) return ans;
		int dist = state.getValue(DISTANCE);
		if (dist > 1) return ans;
		pos = pos.below();
		BlockState log = level.getBlockState(pos);
		if (!log.is(BlockTags.LOGS)) return ans;
		Set<BlockPos> visited = new HashSet<>();
		Queue<Pair<Integer, BlockPos>> queue = new ArrayDeque<>();
		queue.add(Pair.of(-1, pos));
		while (!queue.isEmpty()) {
			var e = queue.poll();
			for (var dir : Direction.values()) {
				var npos = e.getSecond().relative(dir);
				if (visited.contains(npos)) continue;
				visited.add(npos);
				var nst = level.getBlockState(npos);
				if (!nst.is(this)) continue;
				if (nst.getValue(PERSISTENT)) continue;
				int ndis = nst.getValue(DISTANCE);
				if (ndis < e.getFirst()) continue;
				queue.add(Pair.of(ndis, npos));
				ans.add(npos);
			}
		}
		return ans;
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		if (state.getValue(PERSISTENT)) return false;
		if (state.getValue(STATE) == State.FRUITS) return true;
		if (state.getValue(FERTILE) && state.getValue(DISTANCE) == 1) return true;
		return state.getValue(DISTANCE) == 7;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.getValue(PERSISTENT) && !decaying(state)) {
			State st = state.getValue(STATE);
			if (st == State.LEAVES) {
				if (state.getValue(FERTILE) && state.getValue(DISTANCE) == 1) {
					boolean grow = random.nextDouble() < FDModConfig.COMMON.peachGrowChance.get();
					if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
						var list = scanLeaves(level, pos);
						for (var e : list) {
							BlockState est = level.getBlockState(e);
							est = est.setValue(STATE, State.FLOWERS);
							level.setBlockAndUpdate(e, est);
						}
						ForgeHooks.onCropsGrowPost(level, pos, state);
						return;
					}
				}
			}
			if (st == State.FLOWERS) {
				boolean grow = random.nextDouble() < FDModConfig.COMMON.peachGrowChance.get();
				if (ForgeHooks.onCropsGrowPre(level, pos, state, grow)) {
					var list = scanLeaves(level, pos);
					for (var e : list) {
						BlockState est = level.getBlockState(e);
						if (est.getValue(STATE) == State.FRUITS) {
							doDropFruit(est, level, e);
						}
						if (random.nextDouble() < FDModConfig.COMMON.peachFruitChance.get())
							est = est.setValue(STATE, State.FRUITS);
						else est = est.setValue(STATE, State.LEAVES);
						if (est.getValue(DISTANCE) > 1 || e.equals(pos) &&
								random.nextDouble() < FDModConfig.COMMON.flowerDecayChance.get()) {
							est = est.setValue(FERTILE, false);
						}
						level.setBlockAndUpdate(e, est);
					}
					ForgeHooks.onCropsGrowPost(level, pos, state);

				}
				return;
			}
			if (st == State.FRUITS) {
				if (random.nextDouble() < FDModConfig.COMMON.fruitsDropChance.get()) {
					dropFruit(state, level, pos, random);
				}
				return;
			}
		}
		super.randomTick(state, level, pos, random);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean client) {
		return state.getValue(PERSISTENT);
	}

	public void buildLeavesModel(DataGenContext<Block, ? extends BaseLeavesBlock> ctx, RegistrateBlockstateProvider pvd, String name) {
		pvd.getVariantBuilder(ctx.get())
				.forAllStatesExcept(state -> buildModel(pvd, name, state),
						DISTANCE, PERSISTENT, WATERLOGGED, FERTILE);
	}

}
