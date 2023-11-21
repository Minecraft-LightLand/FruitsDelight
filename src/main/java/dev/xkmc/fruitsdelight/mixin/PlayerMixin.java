package dev.xkmc.fruitsdelight.mixin;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@ModifyVariable(at = @At("LOAD"), method = "canEat", argsOnly = true)
	public boolean fruitsdelight$canEat(boolean alwaysEat) {
		return alwaysEat || hasEffect(FDEffects.APPETIZING.get());
	}

}
