package dev.xkmc.fruitsdelight.mixin;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

	@Shadow
	public abstract boolean is(TagKey<Block> tag);

	@Inject(at = @At("HEAD"), cancellable = true,
			method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;")
	public void fruitsdelight$getCollisionShape$passableLeave(BlockGetter level, BlockPos pos, CollisionContext ctx, CallbackInfoReturnable<VoxelShape> cir) {
		if (!is(BlockTags.LEAVES)) return;
		if (ctx instanceof EntityCollisionContext ectx &&
				ectx.getEntity() instanceof Projectile proj &&
				proj.getTags().contains(FDEffects.LEAF_PIERCING.getId().toString())) {
			cir.setReturnValue(Shapes.empty());
		}
	}

}
