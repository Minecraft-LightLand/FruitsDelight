package dev.xkmc.fruitsdelight.init.data;

import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.concurrent.CompletableFuture;

public class FDGLMProvider extends GlobalLootModifierProvider {

	private static final CdcReg<IGlobalLootModifier> REG = CdcReg.of(FruitsDelight.REG, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);

	public static final CdcVal<ReplaceItemLootModifier> GLM = REG.reg("replace_item", ReplaceItemLootModifier.CODEC);

	public static void register() {

	}

	public FDGLMProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, FruitsDelight.MODID);
	}

	@Override
	protected void start() {
		add(FDTrees.DURIAN.getSapling().asItem(), 0.05,
				LootTableIdCondition.builder(BuiltInLootTables.SNIFFER_DIGGING.location()).build());
	}

	private void add(Item item, double chance, LootItemCondition... conds) {
		var rl = BuiltInRegistries.ITEM.getKey(item);
		add(rl.getPath(), new ReplaceItemLootModifier(chance, item, conds));
	}

}
