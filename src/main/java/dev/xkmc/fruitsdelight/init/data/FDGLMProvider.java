package dev.xkmc.fruitsdelight.init.data;

import com.mojang.serialization.Codec;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class FDGLMProvider extends GlobalLootModifierProvider {

	public static final RegistryEntry<Codec<ReplaceItemLootModifier>> GLM = FruitsDelight.REGISTRATE.simple("replace_item",
			ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> ReplaceItemLootModifier.CODEC);

	public static void register() {

	}

	public FDGLMProvider(DataGenerator gen) {
		super(gen, FruitsDelight.MODID);
	}

	@Override
	protected void start() {
		//add(FDTrees.DURIAN.getSapling().asItem(), 0.05, LootTableIdCondition.builder(BuiltInLootTables.SNIFFER_DIGGING).build());
	}

	private void add(Item item, double chance, LootItemCondition... conds) {
		var rl = ForgeRegistries.ITEMS.getKey(item);
		assert rl != null;
		add(rl.getPath(), new ReplaceItemLootModifier(chance, item, conds));
	}

}
