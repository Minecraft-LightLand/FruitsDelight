package dev.xkmc.fruitsdelight.events;

import dev.xkmc.fruitsdelight.content.effects.EffectRemovalEffect;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = FruitsDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EffectHandlers {

	@SubscribeEvent
	public static void onEntitySize(EntityEvent.Size event) {
		if (event.getEntity() instanceof Player le && le.isAddedToWorld()) {
			if (le.isShiftKeyDown() && le.hasEffect(FDEffects.SHRINKING.get())) {
				event.setNewSize(event.getNewSize().scale(0.5f), true);
			}
		}
	}

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

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinLevelEvent event) {
		if (event.getEntity() instanceof Projectile proj) {
			if (proj.getOwner() instanceof LivingEntity le) {
				if (le.hasEffect(FDEffects.LEAF_PIERCING.get())) {
					proj.addTag(FDEffects.LEAF_PIERCING.getId().toString());
				}
			}
		}
	}



	public static boolean isEatOrDrink(ItemStack stack) {
		if (stack.isEdible()) return true;
		try {
			var anim = stack.getUseAnimation();
			if (anim == UseAnim.EAT || anim == UseAnim.DRINK) return true;
		} catch (Exception ignored) {

		}
		return false;
	}

	public static float getFriction(LivingEntity le, float ans) {
		if (le.hasEffect(FDEffects.ASTRINGENT.get())) {
			return Math.min(0.6f, ans);
		}
		return ans;
	}

	public static float getBoatFriction(Boat boat, float ans) {
		for (var e : boat.getPassengers()) {
			if (e instanceof LivingEntity le && le.hasEffect(FDEffects.SLIDING.get())) {
				return Math.max(0.98f, ans);
			}
		}
		return ans;
	}

}
