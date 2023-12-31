package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.content.fluids.OpenEndedPipe;
import dev.xkmc.fruitsdelight.init.entries.FruitFluid;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class JamEffectHandler implements OpenEndedPipe.IEffectHandler {

	@Override
	public boolean canApplyEffects(OpenEndedPipe openEndedPipe, FluidStack fluidStack) {
		return fluidStack.getFluid() instanceof FruitFluid;
	}

	@Override
	public void applyEffects(OpenEndedPipe pipe, FluidStack fluid) {
		if (fluid.getFluid() instanceof FruitFluid fruit) {
			List<LivingEntity> entities = pipe.getWorld().getEntitiesOfClass(LivingEntity.class, pipe.getAOE(), LivingEntity::isAffectedByPotions);
			for (LivingEntity entity : entities) {
				for (var eff : fruit.type.eff) {
					MobEffectInstance effectInstance = eff.getEffect(10);
					MobEffect effect = effectInstance.getEffect();
					if (effect.isInstantenous()) {
						effect.applyInstantenousEffect(null, null, entity,
								effectInstance.getAmplifier(), 1);
					} else {
						entity.addEffect(new MobEffectInstance(effectInstance));
					}
				}
			}
		}
	}

}
