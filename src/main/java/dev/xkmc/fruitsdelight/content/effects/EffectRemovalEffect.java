package dev.xkmc.fruitsdelight.content.effects;

import dev.xkmc.l2core.base.effects.api.InherentEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class EffectRemovalEffect extends InherentEffect {

	public final Lazy<Set<Holder<MobEffect>>> set;

	public EffectRemovalEffect(MobEffectCategory category, int color, List<Supplier<Holder<MobEffect>>> sup) {
		super(category, color);
		set = Lazy.of(() -> {
			Set<Holder<MobEffect>> ans = new HashSet<>();
			for (var e : sup) {
				ans.add(e.get());
			}
			return ans;
		});
	}

	@Override
	public void onEffectStarted(LivingEntity le, int amplifier) {
		for (var e : set.get()) {
			if (le.hasEffect(e))
				le.removeEffect(e);
		}
	}

}
