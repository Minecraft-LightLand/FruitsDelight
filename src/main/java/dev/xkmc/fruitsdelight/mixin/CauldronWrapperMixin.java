package dev.xkmc.fruitsdelight.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.CauldronFluidContent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.wrappers.CauldronWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CauldronWrapper.class, remap = false)
public abstract class CauldronWrapperMixin {

	@Shadow
	@Final
	private Level level;

	@Shadow
	@Final
	private BlockPos pos;

	@Inject(method = "getContent", at = @At("HEAD"), cancellable = true)
	private void fruitsdelight$safeGetContent(BlockState state, CallbackInfoReturnable<CauldronFluidContent> cir) {
		CauldronFluidContent content = CauldronFluidContent.getForBlock(state.getBlock());
		if (content == null) {
			CauldronFluidContent empty = CauldronFluidContent.getForBlock(Blocks.CAULDRON);
			if (empty != null) {
				cir.setReturnValue(empty);
			}
		}
	}

	@Inject(method = "getTankCapacity", at = @At("HEAD"), cancellable = true)
	private void fruitsdelight$safeGetTankCapacity(int tank, CallbackInfoReturnable<Integer> cir) {
		BlockState state = this.level.getBlockState(this.pos);
		if (CauldronFluidContent.getForBlock(state.getBlock()) == null) {
			cir.setReturnValue(0);
		}
	}

	@Inject(method = "getFluidInTank", at = @At("HEAD"), cancellable = true)
	private void fruitsdelight$safeGetFluidInTank(int tank, CallbackInfoReturnable<FluidStack> cir) {
		BlockState state = this.level.getBlockState(this.pos);
		if (CauldronFluidContent.getForBlock(state.getBlock()) == null) {
			cir.setReturnValue(FluidStack.EMPTY);
		}
	}

	@Inject(method = "fill", at = @At("HEAD"), cancellable = true)
	private void fruitsdelight$safeFill(FluidStack resource, IFluidHandler.FluidAction action, CallbackInfoReturnable<Integer> cir) {
		BlockState state = this.level.getBlockState(this.pos);
		if (CauldronFluidContent.getForBlock(state.getBlock()) == null) {
			cir.setReturnValue(0);
		}
	}

	@Inject(method = "drain(ILnet/neoforged/neoforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/neoforged/neoforge/fluids/FluidStack;", at = @At("HEAD"), cancellable = true)
	private void fruitsdelight$safeDrain(int maxDrain, IFluidHandler.FluidAction action, CallbackInfoReturnable<FluidStack> cir) {
		BlockState state = this.level.getBlockState(this.pos);
		if (CauldronFluidContent.getForBlock(state.getBlock()) == null) {
			cir.setReturnValue(FluidStack.EMPTY);
		}
	}

	@Inject(method = "drain(Lnet/neoforged/neoforge/fluids/FluidStack;Lnet/neoforged/neoforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/neoforged/neoforge/fluids/FluidStack;", at = @At("HEAD"), cancellable = true)
	private void fruitsdelight$safeDrainStack(FluidStack resource, IFluidHandler.FluidAction action, CallbackInfoReturnable<FluidStack> cir) {
		BlockState state = this.level.getBlockState(this.pos);
		if (CauldronFluidContent.getForBlock(state.getBlock()) == null) {
			cir.setReturnValue(FluidStack.EMPTY);
		}
	}
}
