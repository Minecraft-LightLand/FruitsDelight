package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FDGLMProvider extends GlobalLootModifierProvider {

	private static final CdcReg<IGlobalLootModifier> REG = CdcReg.of(FruitsDelight.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);

	public static final CdcVal<ReplaceItemLootModifier> GLM = REG.reg("replace_item", ReplaceItemLootModifier.CODEC);
	public static final CdcVal<AddItemLootModifier> ADD_ITEM = REG.reg("add_item", AddItemLootModifier.CODEC);

	/**
	 * Vanilla chests that already contain apples, saplings, potatoes, carrots or wheat seeds.
	 */
	private static final ResourceKey<LootTable>[] CHESTS = new ResourceKey[]{
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

	public FDGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, FruitsDelight.MODID);
	}

	@Override
	protected void start() {
		add(FDTrees.DURIAN.getSapling().asItem(), 0.05,
				LootTableIdCondition.builder(BuiltInLootTables.SNIFFER_DIGGING.location()).build());

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
		LootItemCondition.Builder builder = LootTableIdCondition.builder(CHESTS[0].location());
		for (int i = 1; i < CHESTS.length; i++) {
			builder = builder.or(LootTableIdCondition.builder(CHESTS[i].location()));
		}
		return builder.build();
	}

	private void add(Item item, double chance, LootItemCondition... conds) {
		var rl = BuiltInRegistries.ITEM.getKey(item);
		add(rl.getPath(), new ReplaceItemLootModifier(chance, item, conds));
	}

}
