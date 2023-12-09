package dev.xkmc.fruitsdelight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.fruitsdelight.events.EffectHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F", remap = false), method = "travel")
	public float fruitsdelight$getfriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity, @NotNull Operation<Float> op) {
		float ans = op.call(state, level, pos, entity);
		if (entity instanceof LivingEntity le) {
			return EffectHandlers.getFriction(le, ans);
		}
		return ans;
	}

}
