package dev.xkmc.fruitsdelight.mixin;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalMixin extends AgeableMob {

	protected AnimalMixin(EntityType<? extends AgeableMob> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Inject(at = @At("HEAD"), method = "canFallInLove", cancellable = true)
	public void fruitsdelight$canFallInLove$durian(CallbackInfoReturnable<Boolean> cir) {
		if (hasEffect(FDEffects.DISGUSTED)) {
			cir.setReturnValue(false);
		}
	}

}
