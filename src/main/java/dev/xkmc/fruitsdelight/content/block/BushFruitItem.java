package dev.xkmc.fruitsdelight.content.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BushFruitItem extends ItemNameBlockItem {

	public BushFruitItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void registerBlocks(Map<Block, Item> map, Item item) {
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		BlockState ans = super.getPlacementState(context);
		if (ans == null) return null;
		return ans.setValue(FruitBushBlock.AGE, 0);
	}

	public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
		if (this.allowedIn(pCategory)) {
			pItems.add(new ItemStack(this));
		}
	}

}
