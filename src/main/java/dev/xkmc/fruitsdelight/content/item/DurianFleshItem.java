package dev.xkmc.fruitsdelight.content.item;

import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.food.IFDFood;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DurianFleshItem extends FDFoodItem {

	public DurianFleshItem(Properties props, @Nullable IFDFood food) {
		super(props, food);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (target instanceof Sniffer sniffer) {
			if (!player.level().isClientSide() && sniffer.getBrain().hasMemoryValue(MemoryModuleType.SNIFF_COOLDOWN)) {
				sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFF_COOLDOWN);
				stack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(LangData.TOOLTIP_DURIAN_SEED.get());
		super.appendHoverText(stack, level, list, flag);
	}

}
