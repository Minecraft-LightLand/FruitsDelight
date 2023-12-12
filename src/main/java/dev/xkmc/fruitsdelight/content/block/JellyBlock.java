package dev.xkmc.fruitsdelight.content.block;

import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JellyBlock extends HoneyBlock {

	public final FruitType fruit;

	public JellyBlock(Properties properties, FruitType fruit) {
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
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.TOOLTIP_JELLY.get());
	}

}
