package dev.xkmc.fruitsdelight.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import dev.xkmc.fruitsdelight.events.FoodDataAccessor;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin implements FoodDataAccessor {

	@Shadow
	private int foodLevel;
	@Unique
	public Player fruitsdelight$player = null;

	@Override
	public void fruitsdelight$setPlayer(@NotNull Player player) {
		fruitsdelight$player = player;
	}

	@Inject(at = @At("HEAD"), method = "eat(IF)V")
	public void fruitsdelight$eat(int food, float satMod, CallbackInfo ci, @Local LocalFloatRef sat) {
		int diff = food + foodLevel - 20;
		if (diff <= 0) return;
		if (fruitsdelight$player == null || !fruitsdelight$player.hasEffect(FDEffects.DIGESTING.get())) return;
		sat.set(sat.get() + diff / 2f / food);
	}

}
