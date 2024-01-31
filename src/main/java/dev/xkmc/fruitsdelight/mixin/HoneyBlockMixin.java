package dev.xkmc.fruitsdelight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.fruitsdelight.content.block.JellyBlock;
import dev.xkmc.l2library.util.code.Wrappers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"), method = "fallOn")
	public void fruitsdelight$fallOn(Level level, Entity entity, byte event, Operation<Void> original) {
		HoneyBlock self = Wrappers.cast(this);
		if (self instanceof JellyBlock jelly) {
			jelly.showJellyJumpParticles(entity);
		} else {
			original.call(level, entity, event);
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"), method = "maybeDoSlideEffects")
	public void fruitsdelight$maybeDoSlideEffect(Level level, Entity entity, byte event, Operation<Void> original) {
		HoneyBlock self = Wrappers.cast(this);
		if (self instanceof JellyBlock jelly) {
			jelly.showJellySlideParticles(entity);
		} else {
			original.call(level, entity, event);
		}
	}


}
