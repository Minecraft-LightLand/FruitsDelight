package dev.xkmc.fruitsdelight.mixin;

import dev.xkmc.fruitsdelight.events.ClientEffectHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.min01.archaeology.block.BrushableBlock")
public class BrushableBlockMixin {

	@Inject(at = @At("HEAD"), method = "animateTick", remap = true)
	public void fruitsdelight$animateTick$durian(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (!level.isClientSide()) return;
		ClientEffectHandlers.suspiciousBlockAnimate(state, level, pos, random);
	}

}
