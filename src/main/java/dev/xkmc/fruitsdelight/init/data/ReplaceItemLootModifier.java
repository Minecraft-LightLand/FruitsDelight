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

public class ReplaceItemLootModifier extends LootModifier {

	public static final Codec<ReplaceItemLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i).and(i.group(
					Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance),
					ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(e -> e.item)))
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
	public Codec<ReplaceItemLootModifier> codec() {
		return CODEC;
	}

}
