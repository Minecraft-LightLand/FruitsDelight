package dev.xkmc.fruitsdelight.content.cauldrons;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

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

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (!level.getBlockState(pos.below()).is(ModTags.HEAT_SOURCES))
			return;
		{
			double x;
			double y;
			double z;
			if (random.nextFloat() < 0.5F) {
				x = pos.getX() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
				y = pos.getY() + 0.95;
				z = pos.getZ() + 0.5 + (random.nextDouble() * 0.6 - 0.3);
				level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
			}

			if (random.nextFloat() < 0.05F) {
				x = pos.getX() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
				y = pos.getY() + 0.5;
				z = pos.getZ() + 0.5 + (random.nextDouble() * 0.4 - 0.2);
				double motionY = random.nextBoolean() ? 0.015 : 0.005;
				level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0, motionY, 0.0);
			}
		}
		{
			SoundEvent boilSound = this instanceof JellyCauldronBlock ? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get() : ModSounds.BLOCK_COOKING_POT_BOIL.get();
			double x = pos.getX() + 0.5;
			double y = pos.getY();
			double z = pos.getZ() + 0.5;
			if (random.nextInt(10) == 0) {
				level.playLocalSound(x, y, z, boilSound, SoundSource.BLOCKS, 0.5F, random.nextFloat() * 0.2F + 0.9F, false);
			}
		}
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
					act.perform(state, level, pos, item.getItem())) {
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
