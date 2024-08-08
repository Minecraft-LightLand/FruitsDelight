package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.*;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDCrates;
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
import net.neoforged.neoforge.client.model.generators.ModelFile;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.Locale;

public class FDBlocks {

	public static final BlockEntry<PineappleRiceBlock> PINEAPPLE_RICE;
	public static final BlockEntry<MangosteenCakeBlock> MANGOSTEEN_CAKE;
	public static final BlockEntry<FigPuddingBlock> FIG_PUDDING;

	public static final BlockEntry<JamBlock>[] JAM;
	public static final BlockEntry<JelloBlock>[] JELLO;

	static {

		PINEAPPLE_RICE = FruitsDelight.REGISTRATE.block("pineapple_fried_rice",
						p -> new PineappleRiceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_WOOL),
								FDFood.BOWL_OF_PINEAPPLE_FRIED_RICE.item, true))
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

		MANGOSTEEN_CAKE = FruitsDelight.REGISTRATE.block("mangosteen_cake",
						p -> new MangosteenCakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state -> {
					int serve = state.getValue(MangosteenCakeBlock.BITES);
					String suffix = "_" + serve;
					return pvd.models().getBuilder(ctx.getName() + suffix)
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/mangosteen_cake_base" + suffix)))
							.texture("cake", "block/" + ctx.getName() + "_cake")
							.texture("base", "block/" + ctx.getName() + "_base");
				}))
				.loot((pvd, block) -> pvd.add(block, LootTable.lootTable()
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem())
								.when(ExplosionCondition.survivesExplosion())
								.when(getServe(block))))
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.BOWL))
								.when(ExplosionCondition.survivesExplosion())
								.when(InvertedLootItemCondition.invert(getServe(block))))))
				.register();

		FIG_PUDDING = FruitsDelight.REGISTRATE.block("fig_pudding",
						p -> new FigPuddingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_WOOL)))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state -> {
					int serve = state.getValue(FigPuddingBlock.BITES);
					String suffix = "_" + serve;
					return pvd.models().getBuilder(ctx.getName() + suffix)
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/fig_pudding_base" + suffix)))
							.texture("top", "block/" + ctx.getName() + "_top")
							.texture("bottom", "block/" + ctx.getName() + "_bottom")
							.renderType("cutout");
				}))
				.item().model((ctx, pvd) -> pvd.generated(ctx)).build()
				.loot((pvd, block) -> pvd.add(block, LootTable.lootTable()
						.withPool(LootPool.lootPool().add(LootItem.lootTableItem(block.asItem())
								.when(ExplosionCondition.survivesExplosion())
								.when(getServe(block))))))
				.register();

		FDCrates.register();

		int size = FruitType.values().length;

		JAM = new BlockEntry[size];
		for (int i = 0; i < size; i++) {
			FruitType type = FruitType.values()[i];
			String name = type.name().toLowerCase(Locale.ROOT);
			JAM[i] = FruitsDelight.REGISTRATE.block(name + "_jam_block", p ->
							new JamBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK), type))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.withExistingParent(ctx.getName(), pvd.modLoc("block/tinted"))
							.texture("all", pvd.modLoc("block/jam"))
							.renderType("translucent")))
					.lang(FDItems.toEnglishName(name) + " Jam Block")
					.color(() -> () -> (s, l, p, x) -> type.color)
					.item()
					.color(() -> () -> (s, x) -> type.color)
					.build()
					.register();
		}

		JELLO = new BlockEntry[size];
		for (int i = 0; i < size; i++) {
			FruitType type = FruitType.values()[i];
			String name = type.name().toLowerCase(Locale.ROOT);
			JELLO[i] = FruitsDelight.REGISTRATE.block(name + "_jello_block", p ->
							new JelloBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK), type))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.withExistingParent(ctx.getName(), pvd.modLoc("block/tinted"))
							.texture("all", pvd.modLoc("block/jello"))
							.renderType("translucent")))
					.color(() -> () -> (s, l, p, x) -> type.color)
					.item()
					.color(() -> () -> (s, x) -> type.color)
					.build()
					.register();
		}
	}

	private static <T extends FeastBlock> LootItemBlockStatePropertyCondition.Builder getServe(T block) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).
				setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(block.getServingsProperty(), block.getMaxServings()));
	}

	private static <T extends BaseCakeBlock> LootItemBlockStatePropertyCondition.Builder getServe(T block) {
		return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).
				setProperties(StatePropertiesPredicate.Builder.properties()
						.hasProperty(block.bite, block.maxBite));
	}

	public static void register() {
	}

}
