package dev.xkmc.fruitsdelight.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class SizeEffect extends MobEffect {

	public SizeEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int amp) {
		super.addAttributeModifiers(entity, map, amp);
		entity.refreshDimensions();
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amp) {
		super.removeAttributeModifiers(entity, map, amp);
		entity.refreshDimensions();
	}

}
