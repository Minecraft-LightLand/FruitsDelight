package dev.xkmc.fruitsdelight.init.data;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class FDGLMProvider extends GlobalLootModifierProvider {

	public static final RegistryEntry<Codec<ReplaceItemLootModifier>> GLM = FruitsDelight.REGISTRATE.simple("replace_item",
			ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> ReplaceItemLootModifier.CODEC);

	public static final RegistryEntry<Codec<AddItemLootModifier>> ADD_ITEM = FruitsDelight.REGISTRATE.simple("add_item",
			ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> AddItemLootModifier.CODEC);

	/**
	 * Vanilla chests that already contain apples, saplings, potatoes, carrots or wheat seeds.
	 */
	private static final ResourceLocation[] CHESTS = {
			BuiltInLootTables.IGLOO_CHEST,
			BuiltInLootTables.PILLAGER_OUTPOST,
			BuiltInLootTables.SHIPWRECK_SUPPLY,
			BuiltInLootTables.SPAWN_BONUS_CHEST,
			BuiltInLootTables.STRONGHOLD_CORRIDOR,
			BuiltInLootTables.STRONGHOLD_CROSSING,
			BuiltInLootTables.VILLAGE_FISHER,
			BuiltInLootTables.VILLAGE_PLAINS_HOUSE,
			BuiltInLootTables.VILLAGE_SAVANNA_HOUSE,
			BuiltInLootTables.VILLAGE_SNOWY_HOUSE,
			BuiltInLootTables.VILLAGE_TAIGA_HOUSE,
			BuiltInLootTables.VILLAGE_WEAPONSMITH,
	};

	public static void register() {

	}

	public FDGLMProvider(PackOutput gen) {
		super(gen, FruitsDelight.MODID);
	}

	@Override
	protected void start() {
		add(FDTrees.DURIAN.getSapling().asItem(), 0.05,
				LootTableIdCondition.builder(BuiltInLootTables.SNIFFER_DIGGING).build());

		List<Item> items = new ArrayList<>();
		for (var tree : FDTrees.values()) {
			items.add(tree.getSapling().asItem());
		}
		for (var bush : FDBushes.values()) {
			items.add(bush.getFruit());
		}
		add("chest_fruits_and_saplings", new AddItemLootModifier(0.05, items, anyChest()));
	}

	private static LootItemCondition anyChest() {
		LootItemCondition.Builder builder = LootTableIdCondition.builder(CHESTS[0]);
		for (int i = 1; i < CHESTS.length; i++) {
			builder = builder.or(LootTableIdCondition.builder(CHESTS[i]));
		}
		return builder.build();
	}

	private void add(Item item, double chance, LootItemCondition... conds) {
		var rl = ForgeRegistries.ITEMS.getKey(item);
		assert rl != null;
		add(rl.getPath(), new ReplaceItemLootModifier(chance, item, conds));
	}

}
