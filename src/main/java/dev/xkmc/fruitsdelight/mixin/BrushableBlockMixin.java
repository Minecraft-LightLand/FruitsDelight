package dev.xkmc.fruitsdelight.mixin;

import dev.xkmc.fruitsdelight.events.ClientEffectHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushableBlock.class)
public class BrushableBlockMixin {

	@Inject(at = @At("HEAD"), method = "animateTick")
	public void fruitsdelight$animateTick$durian(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (!level.isClientSide()) return;
		ClientEffectHandlers.suspiciousBlockAnimate(state, level, pos, random);
	}

}
