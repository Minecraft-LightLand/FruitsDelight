package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JelloBlock extends SlimeBlock {

	public final FruitType fruit;

	public JelloBlock(Properties properties, FruitType fruit) {
		super(properties);
		this.fruit = fruit;
	}

	@Override
	public boolean isSlimeBlock(BlockState state) {
		return true;
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return true;
	}

	@Override
	public boolean canStickTo(BlockState state, BlockState other) {
		if (other.getBlock() instanceof JellyBlock jelly) {
			return jelly.fruit == fruit;
		}
		if (other.getBlock() instanceof JelloBlock jello) {
			return jello == this;
		}
		return false;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
		list.add(LangData.TOOLTIP_JELLO.get());
	}

}
