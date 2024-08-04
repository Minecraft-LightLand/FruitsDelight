package dev.xkmc.fruitsdelight.init.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class ReplaceItemLootModifier extends LootModifier {

	public static final MapCodec<ReplaceItemLootModifier> CODEC = RecordCodecBuilder.mapCodec(i -> codecStart(i).and(i.group(
					Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance),
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item)))
			.apply(i, ReplaceItemLootModifier::new));

	private final double chance;
	private final Item item;

	private ReplaceItemLootModifier(LootItemCondition[] conditionsIn, double chance, Item item) {
		super(conditionsIn);
		this.chance = chance;
		this.item = item;
	}

	public ReplaceItemLootModifier(double chance, Item item, LootItemCondition... conditionsIn) {
		super(conditionsIn);
		this.chance = chance;
		this.item = item;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
		if (chance > context.getRandom().nextDouble()) {
			list.clear();
			list.add(item.getDefaultInstance());
		}

		return list;
	}

	@Override
	public MapCodec<ReplaceItemLootModifier> codec() {
		return CODEC;
	}

}
