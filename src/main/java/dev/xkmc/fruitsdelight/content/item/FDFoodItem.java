package dev.xkmc.fruitsdelight.content.item;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.food.FoodType;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.food.IFDFood;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FDFoodItem extends Item implements IFDFoodItem {

	private static List<FruitType> getFruits(ItemStack stack) {
		return FDItems.FRUITS.getOrDefault(stack, List.of());
	}

	private static Component getTooltip(MobEffectInstance eff) {
		MutableComponent ans = Component.translatable(eff.getDescriptionId());
		MobEffect mobeffect = eff.getEffect().value();
		if (eff.getAmplifier() > 0) {
			ans = Component.translatable("potion.withAmplifier", ans,
					Component.translatable("potion.potency." + eff.getAmplifier()));
		}

		if (eff.getDuration() > 20) {
			ans = Component.translatable("potion.withDuration", ans,
					MobEffectUtil.formatDuration(eff, 1, 20));
		}

		return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
	}

	public static void getFoodEffects(ItemStack stack, List<Component> list) {
		var food = stack.getFoodProperties(null);
		if (food == null) return;
		getFoodEffects(food, list);
	}

	public static void getFoodEffects(FoodProperties food, List<Component> list) {
		for (var eff : food.effects()) {
			int chance = Math.round(eff.probability() * 100);
			Component ans = getTooltip(eff.effect());
			if (chance == 100) {
				list.add(ans);
			} else {
				list.add(LangData.CHANCE_EFFECT.get(ans, chance));
			}
		}
	}

	public static int color(ItemStack stack, int layer) {
		var list = getFruits(stack);
		if (layer == 0 || list.isEmpty()) return -1;
		return list.get(layer % list.size()).color;
	}

	public static ItemStack setContent(FDFoodItem item, FruitType e) {
		return FDItems.FRUITS.set(item.getDefaultInstance(), List.of(e));
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var list = getFruits(stack);
		if (!list.isEmpty()) {
			var old = super.getFoodProperties(stack, entity);
			if (old == null) return null;
			var builder = new FoodProperties.Builder();
			builder.nutrition(old.nutrition());
			builder.saturationModifier(old.saturation());
			if (old.canAlwaysEat()) builder.alwaysEdible();
			if (old.eatSeconds() < 1) builder.fast();
			if (food == null) return null;
			Map<FruitType, Integer> map = new LinkedHashMap<>();
			map.put(food.fruit(), food.getType().effectLevel);
			int lv = FoodType.JAM.effectLevel;
			for (var type : list) {
				map.compute(type, (k, v) -> v == null ? lv : v + lv);
			}
			for (var ent : map.entrySet()) {
				for (var e : ent.getKey().eff) {
					builder.effect(() -> e.getEffect(ent.getValue()), e.getChance(ent.getValue()));
				}
			}
			for (var e : food.getEffects()) {
				builder.effect(e::getEffect, e.chance());
			}
			return builder.build();
		}
		return super.getFoodProperties(stack, entity);
	}

	public final IFDFood food;

	private final UseAnim anim;

	public FDFoodItem(Properties props, IFDFood food, UseAnim anim) {
		super(props);
		this.food = food;
		this.anim = anim;
	}

	public FDFoodItem(Properties props, IFDFood food) {
		this(props, food, UseAnim.EAT);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return anim;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		if (food != null && food.getType() == FoodType.JAM)
			return SoundEvents.HONEY_DRINK;
		return SoundEvents.GENERIC_DRINK;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		var types = getFruits(stack);
		if (!types.isEmpty()) {
			list.add(LangData.JAM_CONTENT.get());
			for (var type : types) {
				list.add(type.getJam().getDescription().copy().withStyle(ChatFormatting.GRAY));
			}
		} else if (stack.is(TagGen.ALLOW_JAM)) {
			list.add(LangData.ALLOW_JAM.get());
		}
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			getFoodEffects(stack, list);
	}

	public void fillItemCategory(int size, CreativeModeTabModifier tab) {
		for (FruitType fruit : FruitType.values()) {
			ItemStack stack = new ItemStack(this);
			List<FruitType> list = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				list.add(fruit);
			}
			tab.accept(FDItems.FRUITS.set(stack, list));
		}
	}

	@Override
	public IFDFood food() {
		return food;
	}

}
