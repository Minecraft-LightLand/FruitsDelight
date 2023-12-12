package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.JelloBlock;
import dev.xkmc.fruitsdelight.content.block.PineappleRiceBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraftforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.Locale;

public class FDBlocks {

	public static final BlockEntry<PineappleRiceBlock> PINEAPPLE_RICE;

	public static final BlockEntry<JelloBlock>[] JELLO;

	static {

	}

	static {

		PINEAPPLE_RICE = FruitsDelight.REGISTRATE.block("pineapple_fried_rice",
						p -> new PineappleRiceBlock(BlockBehaviour.Properties.copy(Blocks.YELLOW_WOOL),
								FDFood.BOWL_OF_PINEAPPLE_FRIED_RICE.item::get, true))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state -> {
					int serve = state.getValue(FeastBlock.SERVINGS);
					String suffix = serve == 4 ? "" : ("_" + (4 - serve));
					return pvd.models().getBuilder(ctx.getName() + suffix)
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/fried_rice_base" + suffix)))
							.texture("food", "block/" + ctx.getName());
				}))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.loot((pvd, block) -> pvd.add(block, LootTable.lootTable()
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem())
								.when(ExplosionCondition.survivesExplosion())
								.when(getServe(block))))
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.BOWL))
								.when(ExplosionCondition.survivesExplosion())
								.when(InvertedLootItemCondition.invert(getServe(block))))))
				.register();

		int size = FruitType.values().length;
		JELLO = new BlockEntry[size];
		for (int i = 0; i < size; i++) {
			FruitType type = FruitType.values()[i];
			String name = type.name().toLowerCase(Locale.ROOT);
			JELLO[i] = FruitsDelight.REGISTRATE.block(name + "_jello_block", p ->
							new JelloBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK)))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.withExistingParent(ctx.getName(), "block/leaves")
							.texture("all", pvd.modLoc("block/jello"))
							.renderType("translucent")))
					.color(() -> () -> (s, l, p, x) -> type.color | 0xff000000)
					.item()
					.color(() -> () -> (s, x) -> type.color | 0xff000000)
					.build()
					.register();
		}
	}

	private static <T extends FeastBlock> LootItemBlockStatePropertyCondition.Builder getServe(T block) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).
				setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(block.getServingsProperty(), block.getMaxServings()));
	}

	public static void register() {
	}

}
