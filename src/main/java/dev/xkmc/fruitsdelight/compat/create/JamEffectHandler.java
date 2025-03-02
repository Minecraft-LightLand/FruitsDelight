package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import dev.xkmc.fruitsdelight.init.entries.FruitFluid;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class JamEffectHandler implements OpenPipeEffectHandler {

	@Override
	public void apply(Level level, AABB aabb, FluidStack fluid) {
		if (fluid.getFluid() instanceof FruitFluid fruit) {
			List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, aabb, LivingEntity::isAffectedByPotions);
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
