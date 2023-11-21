package dev.xkmc.fruitsdelight.content.effects;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.util.Lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class EffectRemovalEffect extends InherentEffect {

	public final Lazy<Set<MobEffect>> set;

	public EffectRemovalEffect(MobEffectCategory category, int color, List<Supplier<MobEffect>> sup) {
		super(category, color);
		set = Lazy.of(() -> {
			Set<MobEffect> ans = new HashSet<>();
			for (var e : sup) {
				ans.add(e.get());
			}
			return ans;
		});
	}

	@Override
	public void addAttributeModifiers(LivingEntity le, AttributeMap pAttributeMap, int pAmplifier) {
		for (var e : set.get()) {
			if (le.hasEffect(e))
				le.removeEffect(e);
		}
	}

}
