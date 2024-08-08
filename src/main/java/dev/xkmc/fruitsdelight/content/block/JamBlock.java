package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.events.BlockEffectToClient;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class JamBlock extends HoneyBlock {

	public final FruitType fruit;

	public JamBlock(Properties properties, FruitType fruit) {
		super(properties);
		this.fruit = fruit;
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return true;
	}

	@Override
	public boolean canStickTo(BlockState state, BlockState other) {
		if (other.getBlock() instanceof JelloBlock jello) {
			return jello.fruit == fruit;
		}
		if (other.isStickyBlock() && other.getBlock() != this)
			return false;
		return super.canStickTo(state, other);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.TOOLTIP_JAM.get());
	}

	public void showJamSlideParticles(Entity entity) {
		if (entity.level().isClientSide())
			showParticles(entity, 5);
		else FruitsDelight.HANDLER.toTrackingPlayers(new BlockEffectToClient(this, entity.getId(),
				BlockEffectToClient.Type.JAM_SLIDE), entity);
	}

	public void showJamJumpParticles(Entity entity) {
		if (entity.level().isClientSide())
			showParticles(entity, 10);
		else FruitsDelight.HANDLER.toTrackingPlayers(new BlockEffectToClient(this, entity.getId(),
				BlockEffectToClient.Type.JAM_JUMP), entity);
	}

	private void showParticles(Entity entity, int count) {
		if (entity.level().isClientSide) {
			BlockState state = defaultBlockState();
			for (int i = 0; i < count; ++i) {
				entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state),
						entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
			}
		}
	}

}
