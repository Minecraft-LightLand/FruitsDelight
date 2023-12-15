package dev.xkmc.fruitsdelight.content.item;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.food.FoodType;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.food.IFDFood;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FDFoodItem extends Item {

	public static final String ROOT = "JellyEffectRoot";

	private static List<FruitType> getFruits(ItemStack stack) {
		List<FruitType> ans = new ArrayList<>();
		if (stack.getTag() != null && stack.getTag().contains(ROOT)) {
			var strs = stack.getTag().getList(ROOT, Tag.TAG_STRING);
			for (int i = 0; i < strs.size(); i++) {
				String str = strs.getString(i);
				try {
					ans.add(FruitType.valueOf(str));
				} catch (Exception ignored) {
				}
			}
		}
		return ans;
	}

	private static Component getTooltip(MobEffectInstance eff) {
		MutableComponent ans = Component.translatable(eff.getDescriptionId());
		MobEffect mobeffect = eff.getEffect();
		if (eff.getAmplifier() > 0) {
			ans = Component.translatable("potion.withAmplifier", ans,
					Component.translatable("potion.potency." + eff.getAmplifier()));
		}

		if (eff.getDuration() > 20) {
			ans = Component.translatable("potion.withDuration", ans,
					MobEffectUtil.formatDuration(eff, 1));
		}

		return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
	}

	public static void getFoodEffects(ItemStack stack, List<Component> list) {
		var food = stack.getFoodProperties(null);
		if (food == null) return;
		getFoodEffects(food, list);
	}

	public static void getFoodEffects(FoodProperties food, List<Component> list) {
		for (var eff : food.getEffects()) {
			int chance = Math.round(eff.getSecond() * 100);
			if (eff.getFirst() == null) continue; //I hate stupid modders
			Component ans = getTooltip(eff.getFirst());
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
		var ans = item.getDefaultInstance();
		ListTag list = new ListTag();
		list.add(StringTag.valueOf(e.name()));
		ans.getOrCreateTag().put(ROOT, list);
		return ans;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity consumer) {
		ItemStack itemStack = getCraftingRemainingItem(stack);
		super.finishUsingItem(stack, worldIn, consumer);
		if (itemStack.isEmpty()) {
			return stack;
		}
		if (stack.isEmpty()) {
			return itemStack;
		}
		if (consumer instanceof Player player && !player.getAbilities().instabuild) {
			if (!player.getInventory().add(itemStack)) {
				player.drop(itemStack, false);
			}
		}

		return stack;
	}

	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		var list = getFruits(stack);
		if (!list.isEmpty()) {
			var old = super.getFoodProperties(stack, entity);
			if (old == null) return null;
			var builder = new FoodProperties.Builder();
			builder.nutrition(old.getNutrition());
			builder.saturationMod(old.getSaturationModifier());
			if (old.canAlwaysEat()) builder.alwaysEat();
			if (old.isFastFood()) builder.fast();
			if (old.isMeat()) builder.meat();
			if (food == null) return null;
			Map<FruitType, Integer> map = new LinkedHashMap<>();
			map.put(food.fruit(), food.getType().effectLevel);
			int lv = FoodType.JELLY.effectLevel;
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

	@Nullable
	public final IFDFood food;

	private final UseAnim anim;

	public FDFoodItem(Properties props, @Nullable IFDFood food, UseAnim anim) {
		super(props);
		this.food = food;
		this.anim = anim;
	}

	public FDFoodItem(Properties props, @Nullable IFDFood food) {
		this(props, food, UseAnim.EAT);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return anim;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		if (food != null && food.getType() == FoodType.JELLY)
			return SoundEvents.HONEY_DRINK;
		return SoundEvents.GENERIC_DRINK;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		var types = getFruits(stack);
		if (!types.isEmpty()) {
			list.add(LangData.JELLY_CONTENT.get());
			for (var type : types) {
				list.add(type.getJelly().getDescription().copy().withStyle(ChatFormatting.GRAY));
			}
		} else if (stack.is(TagGen.ALLOW_JELLY)) {
			list.add(LangData.ALLOW_JELLY.get());
		}
		getFoodEffects(stack, list);
	}

	public void fillItemCategory(int size, CreativeModeTabModifier tab) {
		for (FruitType fruit : FruitType.values()) {
			ItemStack stack = new ItemStack(this);
			ListTag list = new ListTag();
			for (int i = 0; i < size; i++) {
				list.add(StringTag.valueOf(fruit.name()));
			}
			stack.getOrCreateTag().put(FDFoodItem.ROOT, list);
			tab.accept(stack);
		}
	}

}
