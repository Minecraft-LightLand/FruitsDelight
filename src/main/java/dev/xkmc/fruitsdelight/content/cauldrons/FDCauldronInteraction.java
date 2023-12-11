package dev.xkmc.fruitsdelight.content.cauldrons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Predicate;

public record FDCauldronInteraction(SimpleInteraction action, ItemStack result,
									SoundEvent sound, Predicate<ItemStack> pred,
									boolean requiresHeat)
		implements CauldronInteraction {

	public static FDCauldronInteraction of(SimpleInteraction action) {
		return of(action, ItemStack.EMPTY, SoundEvents.GENERIC_SPLASH);
	}

	public static FDCauldronInteraction withHeat(SimpleInteraction action) {
		return new FDCauldronInteraction(action, ItemStack.EMPTY, SoundEvents.GENERIC_SPLASH, stack -> true, true);

	}

	public static FDCauldronInteraction of(SimpleInteraction action, ItemStack result, SoundEvent sound) {
		return of(action, result, sound, stack -> true);
	}

	public static FDCauldronInteraction of(SimpleInteraction action, ItemStack result, SoundEvent sound, Predicate<ItemStack> pred) {
		return new FDCauldronInteraction(action, result, sound, pred, false);
	}

	@Override
	public InteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
		if (perform(state, level, pos, stack)) {
			if (!level.isClientSide) {
				ItemStack remain = stack.getCraftingRemainingItem();
				if (!player.getAbilities().instabuild)
					stack.shrink(1);
				if (!result.isEmpty()) {
					player.getInventory().placeItemBackInInventory(result.copy());
					level.playSound(player, player, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				if (!remain.isEmpty()) {
					player.getInventory().placeItemBackInInventory(remain);
				}
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	public boolean perform(BlockState state, Level level, BlockPos pos, ItemStack stack) {
		if (!pred.test(stack)) return false;
		if (requiresHeat) {
			if (!level.getBlockState(pos.below()).is(ModTags.HEAT_SOURCES)) {
				return false;
			}
		}
		BlockState next = action.perform(state);
		if (next == null) return false;
		if (!level.isClientSide())
			level.setBlockAndUpdate(pos, next);
		return true;
	}

}
