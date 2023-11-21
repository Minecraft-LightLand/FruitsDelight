package dev.xkmc.fruitsdelight.events;

import dev.xkmc.fruitsdelight.content.effects.EffectRemovalEffect;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FruitsDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectHandlers {

	@SubscribeEvent
	public static void onItemStartUse(LivingEntityUseItemEvent.Start event) {
		var ins = event.getEntity().getEffect(FDEffects.LOZENGE.get());
		if (ins != null && isEatOrDrink(event.getItem())) {
			event.setDuration(event.getDuration() >> (1 + ins.getAmplifier()));
		}
	}

	@SubscribeEvent
	public static void onEffectTest(MobEffectEvent.Applicable event) {
		for (var old : event.getEntity().getActiveEffects()) {
			if (old.getEffect() instanceof EffectRemovalEffect eff) {
				if (eff.set.get().contains(event.getEffectInstance().getEffect())) {
					event.setResult(Event.Result.DENY);
					return;
				}
			}
		}
	}

	public static boolean isEatOrDrink(ItemStack stack) {
		if (stack.isEdible()) return true;
		try {
			var anim = stack.getUseAnimation();
			if (anim == UseAnim.EAT || anim == UseAnim.DRINK) return true;
		} catch (Exception e) {

		}
		return false;
	}

	public static float getFriction(LivingEntity le, float ans) {
		if (le.hasEffect(FDEffects.ASTRINGENT.get())) {
			return Math.min(0.6f, ans);
		}
		return ans;
	}

}
