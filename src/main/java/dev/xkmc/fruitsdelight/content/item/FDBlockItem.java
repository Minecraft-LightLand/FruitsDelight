package dev.xkmc.fruitsdelight.content.item;

import dev.xkmc.fruitsdelight.init.food.FoodType;
import dev.xkmc.fruitsdelight.init.food.IFDFood;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.Configuration;

import java.util.List;

import static dev.xkmc.fruitsdelight.content.item.FDFoodItem.getFoodEffects;

public class FDBlockItem extends BlockItem {


	@Nullable
	public final IFDFood food;

	private final UseAnim anim;

	public FDBlockItem(Block block, Properties properties, @Nullable IFDFood food) {
		this(block, properties, food, UseAnim.EAT);
	}

	public FDBlockItem(Block block, Properties props, @Nullable IFDFood food, UseAnim anim) {
		super(block, props);
		this.food = food;
		this.anim = anim;
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
	public InteractionResult place(BlockPlaceContext ctx) {
		if (ctx.getPlayer() != null && ctx.getPlayer().isShiftKeyDown())
			return InteractionResult.PASS;
		return super.place(ctx);
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		if (Configuration.FOOD_EFFECT_TOOLTIP.get())
			getFoodEffects(stack, list);
	}

}
