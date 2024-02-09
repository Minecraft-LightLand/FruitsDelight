package dev.xkmc.fruitsdelight.content.item;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
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
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		super.onArmorTick(stack, level, player);
		EffectUtil.refreshEffect(player, new MobEffectInstance(FDEffects.ALIENATING.get(), 40
						, 0, true, true),
				EffectUtil.AddReason.SELF, player);
	}

}
