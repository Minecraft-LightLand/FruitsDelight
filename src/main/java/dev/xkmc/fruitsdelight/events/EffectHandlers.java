package dev.xkmc.fruitsdelight.events;

import dev.xkmc.fruitsdelight.content.effects.EffectRemovalEffect;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = FruitsDelight.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EffectHandlers {

	@SubscribeEvent
	public static void onEntitySize(EntityEvent.Size event) {
		if (event.getEntity() instanceof Player le && le.isAddedToLevel()) {
			if (le.isShiftKeyDown() && le.hasEffect(FDEffects.SHRINKING.holder())) {
				event.setNewSize(event.getNewSize().scale(0.5f));
			}
		}
	}

	@SubscribeEvent
	public static void onEffectEnd(MobEffectEvent.Expired event) {
		var ins = event.getEffectInstance();
		if (ins == null) return;
		if (ins.getEffect().value() == FDEffects.SHRINKING.get())
			event.getEntity().refreshDimensions();
	}

	@SubscribeEvent
	public static void onEffectEnd(MobEffectEvent.Remove event) {
		var ins = event.getEffectInstance();
		if (ins == null) return;
		if (ins.getEffect().value() == FDEffects.SHRINKING.get())
			event.getEntity().refreshDimensions();
	}

	@SubscribeEvent
	public static void onItemStartUse(LivingEntityUseItemEvent.Start event) {
		var ins = event.getEntity().getEffect(FDEffects.LOZENGE.holder());
		if (ins != null && isEatOrDrink(event.getEntity(), event.getItem())) {
			event.setDuration(event.getDuration() >> (1 + ins.getAmplifier()));
		}
	}

	@SubscribeEvent
	public static void onEffectTest(MobEffectEvent.Applicable event) {
		var e = event.getEffectInstance();
		if (e == null) return;
		for (var old : event.getEntity().getActiveEffects()) {
			if (old.getEffect() instanceof EffectRemovalEffect eff) {
				if (eff.set.get().contains(e.getEffect())) {
					event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Projectile proj) {
			if (proj.getOwner() instanceof LivingEntity le) {
				if (le.hasEffect(FDEffects.LEAF_PIERCING.holder())) {
					proj.addTag(FDEffects.LEAF_PIERCING.key().location().toString());
				}
			}
		}
	}

	public static boolean isEatOrDrink(LivingEntity le, ItemStack stack) {
		if (stack.getFoodProperties(le) == null) return true;
		try {
			var anim = stack.getUseAnimation();
			if (anim == UseAnim.EAT || anim == UseAnim.DRINK) return true;
		} catch (Exception ignored) {

		}
		return false;
	}

	public static float getFriction(LivingEntity le, float ans) {
		if (le.hasEffect(FDEffects.ASTRINGENT)) {
			return Math.min(0.6f, ans);
		}
		return ans;
	}

	public static float getBoatFriction(Boat boat, float ans) {
		for (var e : boat.getPassengers()) {
			if (e instanceof LivingEntity le && le.hasEffect(FDEffects.SLIDING)) {
				return Math.max(0.98f, ans);
			}
		}
		return ans;
	}

}
