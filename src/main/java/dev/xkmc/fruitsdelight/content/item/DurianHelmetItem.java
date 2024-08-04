package dev.xkmc.fruitsdelight.content.item;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DurianHelmetItem extends Item {

	public DurianHelmetItem(Properties prop) {
		super(prop);
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (slotId != 39) return;
		if (entity instanceof LivingEntity player)
			EffectUtil.refreshEffect(player, new MobEffectInstance(FDEffects.ALIENATING, 40
					, 0, true, true), player);
	}

}
