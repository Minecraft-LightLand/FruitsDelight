package dev.xkmc.fruitsdelight.content.cauldrons;

import dev.xkmc.fruitsdelight.mixin.AbstractCauldronBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CauldronDispenseBehavior extends DefaultDispenseItemBehavior {

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		ServerLevel level = source.level();
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof AbstractCauldronBlock cauldronBlock) {
			var interactions = ((AbstractCauldronBlockAccessor) cauldronBlock).getInteractions();
			if (interactions.map().get(stack.getItem()) instanceof FDCauldronInteraction action) {
				if (action.perform(state, level, pos, stack)) {
					ItemStack remain = stack.getCraftingRemainingItem();
					stack.shrink(1);
					if (!action.result().isEmpty()) {
						Block.popResource(level, pos, action.result().getItem().getDefaultInstance());
					}
					if (!remain.isEmpty()) {
						Block.popResource(level, pos, remain);
					}
				}
			}
		}
		return stack;
	}

}
