package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.PineappleRiceBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
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

public class FDBlocks {

	public static final BlockEntry<PineappleRiceBlock> PINEAPPLE_RICE;

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
	}

	private static <T extends FeastBlock> LootItemBlockStatePropertyCondition.Builder getServe(T block) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).
				setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(block.getServingsProperty(), block.getMaxServings()));
	}

	public static void register() {
	}

}
