package dev.xkmc.fruitsdelight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

	@WrapOperation(at = @At(value = "INVOKE", target =
			"Lnet/minecraft/world/level/block/state/BlockState;getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
	), method = "tick", require = 0)
	public VoxelShape fruitsdelight$tick$getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, Operation<VoxelShape> op) {
		return state.getCollisionShape(level, pos, CollisionContext.of(Wrappers.cast(this)));
	}

}
