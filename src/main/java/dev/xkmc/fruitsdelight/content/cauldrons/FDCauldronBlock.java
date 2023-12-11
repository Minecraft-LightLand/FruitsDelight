package dev.xkmc.fruitsdelight.content.cauldrons;

import dev.xkmc.l2library.repack.registrate.providers.DataGenContext;
import dev.xkmc.l2library.repack.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class FDCauldronBlock extends AbstractCauldronBlock {

	private final Map<Item, CauldronInteraction> interactions;

	private FDCauldronBlock(Properties properties, Map<Item, CauldronInteraction> interactions) {
		super(properties, interactions);
		this.interactions = interactions;
	}

	public Map<Item, CauldronInteraction> getInteractions() {
		return interactions;
	}

	public FDCauldronBlock(Properties properties) {
		this(properties, CauldronInteraction.newInteractionMap());
	}

	protected double getContentHeight(BlockState state) {
		return 0.9375D;
	}

	public boolean isFull(BlockState state) {
		return true;
	}

	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (entity instanceof ItemEntity item && this.isEntityInsideContent(state, pos, entity)) {
			var action = interactions.get(item.getItem().getItem());
			if (action instanceof FDCauldronInteraction act &&
					act.pred().test(item.getItem()) &&
					act.perform(state, level, pos)) {
				if (!level.isClientSide) {
					ItemStack remain = item.getItem().getCraftingRemainingItem();
					item.getItem().shrink(1);
					if (item.getItem().isEmpty()) {
						item.discard();
					}
					if (!act.result().isEmpty()) {
						Block.popResource(level, pos, act.result().getItem().getDefaultInstance());
						level.playSound(null, entity, act.sound(), SoundSource.BLOCKS, 1.0F, 1.0F);
					}
					if (!remain.isEmpty()) {
						Block.popResource(level, pos, remain);
					}
				}
			}
		}
	}

	public <T extends FDCauldronBlock> void build(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().withExistingParent(ctx.getName(), "block/template_cauldron_full")
				.texture("bottom", "minecraft:block/cauldron_bottom")
				.texture("inside", "minecraft:block/cauldron_inner")
				.texture("particle", "minecraft:block/cauldron_side")
				.texture("side", "minecraft:block/cauldron_side")
				.texture("top", "minecraft:block/cauldron_top")
				.texture("content", "minecraft:block/water_still"));
	}

}
