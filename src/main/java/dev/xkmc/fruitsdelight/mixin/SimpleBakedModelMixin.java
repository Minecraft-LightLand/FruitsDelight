package dev.xkmc.fruitsdelight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.fruitsdelight.content.block.BaseLeavesBlock;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleBakedModel.class)
public class SimpleBakedModelMixin {

	@WrapOperation(method = "getRenderTypes*",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"))
	private Block fruitsDelight$getRenderTypes(BlockState instance, Operation<Block> original) {
		var ans = original.call(instance);
		if (ans instanceof BaseLeavesBlock) {
			return Blocks.AIR;
		}
		return ans;
	}

}
