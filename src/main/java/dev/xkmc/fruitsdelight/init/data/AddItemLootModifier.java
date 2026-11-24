package dev.xkmc.fruitsdelight.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddItemLootModifier extends LootModifier {

	public static final Codec<AddItemLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i).and(i.group(
					Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance),
					ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("items").forGetter(e -> e.items)))
			.apply(i, AddItemLootModifier::new));

	private final double chance;
	private final List<Item> items;

	private AddItemLootModifier(LootItemCondition[] conditionsIn, double chance, List<Item> items) {
		super(conditionsIn);
		this.chance = chance;
		this.items = items;
	}

	public AddItemLootModifier(double chance, List<Item> items, LootItemCondition... conditionsIn) {
		super(conditionsIn);
		this.chance = chance;
		this.items = items;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		double expected = items.size() * chance;
		int count = (int) expected;
		if (context.getRandom().nextDouble() < expected - count) {
			count++;
		}
		List<Item> pool = new ArrayList<>(items);
		count = Math.min(count, pool.size());
		for (int i = 0; i < count; i++) {
			int index = context.getRandom().nextInt(pool.size());
			list.add(pool.remove(index).getDefaultInstance());
		}
		return list;
	}

	@Override
	public Codec<AddItemLootModifier> codec() {
		return CODEC;
	}

}
