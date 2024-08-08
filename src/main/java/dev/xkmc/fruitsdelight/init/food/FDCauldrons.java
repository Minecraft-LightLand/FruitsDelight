package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.cauldrons.*;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.BiFunction;

public class FDCauldrons {

	public static final BlockEntry<FDCauldronBlock> LEMON;
	public static final BlockEntry<FruitCauldronBlock>[] FRUIT;
	public static final BlockEntry<JamCauldronBlock>[] JAM;
	public static final BlockEntry<JamCauldronBlock>[] JELLO;

	public static final ItemEntry<Item> FAKE_CAULDRON;

	static {
		LEMON = simple("lemonade_cauldron", FDCauldronBlock::new);
		int size = FruitType.values().length;
		FRUIT = new BlockEntry[size];
		JAM = new BlockEntry[size];
		JELLO = new BlockEntry[size];
		for (int i = 0; i < size; i++) {
			FruitType type = FruitType.values()[i];
			String name = type.name().toLowerCase(Locale.ROOT);
			FRUIT[i] = simple(name + "_cauldron", (p, s) -> new FruitCauldronBlock(p, type, s));
			JAM[i] = simple(name + "_jam_cauldron", (p, s) -> new JamCauldronBlock(p, type, s));
			JELLO[i] = simple(name + "_jello_cauldron", (p, s) -> new JamCauldronBlock(p, type, s));
		}
		FAKE_CAULDRON = FruitsDelight.REGISTRATE.item("dummy_cauldron", Item::new)
				.lang("Water Cauldron")
				.model((ctx, pvd) -> CauldronRenderHandler.gui(pvd.withExistingParent(ctx.getName(), "block/water_cauldron_full")))
				.color(() -> () -> CauldronRenderHandler::getItemColor)
				.removeTab(FruitsDelight.TAB.key())
				.register();
	}

	public static void register() {

	}

	public static void init() {

		CauldronRecipe.create(CauldronInteraction.WATER,
				FAKE_CAULDRON.get(),
				FDFood.LEMON_SLICE.item.get(),
				FDCauldronInteraction.of(state ->
						state.getValue(LayeredCauldronBlock.LEVEL) == 3 ?
								LEMON.get().defaultBlockState() : null),
				LEMON.get().asItem());

		var fruitProp = FruitCauldronBlock.LEVEL;
		var jamProp = JamCauldronBlock.LEVEL;
		int max = FruitCauldronBlock.MAX;

		for (FruitType type : FruitType.values()) {
			int level = 4 / type.jamCost;
			int index = type.ordinal();
			var fruit = FRUIT[index].get();
			var jam = JAM[index].get();
			var jello = JELLO[index].get();

			CauldronRecipe.create(CauldronInteraction.WATER,
					FAKE_CAULDRON.get(),
					type.getJam(), 3, FDCauldronInteraction.of(state ->
							state.getValue(LayeredCauldronBlock.LEVEL) == 3 ?
									fruit.defaultBlockState().setValue(fruitProp, 4) : null),
					fruit.asItem());

			CauldronRecipe.create(LEMON.get(), type.getFruit(), 12 / level, FDCauldronInteraction.withHeat(state ->
							fruit.defaultBlockState().setValue(fruitProp, level)),
					fruit.asItem());
			fruit.getInteractions().map().put(type.getFruit(), FDCauldronInteraction.withHeat(state ->
					state.getValue(fruitProp) == max ? null : state.setValue(fruitProp, Math.min(max, state.getValue(fruitProp) + level))));
			fruit.getInteractions().map().put(type.getJam(), FDCauldronInteraction.of(state ->
					state.getValue(fruitProp) == max ? null : state.setValue(fruitProp, Math.min(max, state.getValue(fruitProp) + 4))));
			CauldronRecipe.create(fruit, Items.SUGAR, 1, FDCauldronInteraction.withHeat(state ->
							state.getValue(fruitProp) == max ? jam.defaultBlockState() : null),
					jam.asItem());
			CauldronRecipe.create(jam, Items.SLIME_BALL, 1, FDCauldronInteraction.withHeat(state ->
							jello.defaultBlockState().setValue(jamProp, state.getValue(jamProp))),
					jello.asItem());
			CauldronRecipe.empty(jam, Items.GLASS_BOTTLE, 3, FDCauldronInteraction.of(state ->
									state.getValue(jamProp) == 1 ? Blocks.CAULDRON.defaultBlockState() :
											state.setValue(jamProp, state.getValue(jamProp) - 1),
							type.getJam().getDefaultInstance(), SoundEvents.BOTTLE_FILL),
					Items.CAULDRON);

			CauldronRecipe.empty(jello, Items.BOWL, 3, FDCauldronInteraction.of(state ->
									state.getValue(jamProp) == 1 ? Blocks.CAULDRON.defaultBlockState() :
											state.setValue(jamProp, state.getValue(jamProp) - 1),
							type.getJello().getDefaultInstance(), SoundEvents.BOTTLE_FILL),
					Items.CAULDRON);

			DispenserBlock.registerBehavior(type.getJam(), new CauldronDispenseBehavior());
		}
		DispenserBlock.registerBehavior(FDFood.LEMON_SLICE.item.get(), new CauldronDispenseBehavior());
	}

	private static <T extends FDCauldronBlock> BlockEntry<T> simple(String id, BiFunction<BlockBehaviour.Properties, String, T> factory) {
		return FruitsDelight.REGISTRATE.block(id, p -> factory.apply(
						BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON),
						FruitsDelight.MODID + "_" + id
				)).blockstate((ctx, pvd) -> ctx.get().build(ctx, pvd))
				.loot((pvd, block) -> pvd.dropOther(block, Items.CAULDRON))
				.color(() -> () -> CauldronRenderHandler::getBlockColor)
				.item().removeTab(FruitsDelight.TAB.key())
				.color(() -> () -> CauldronRenderHandler::getItemColor)
				.build()
				.lang(FDItems.toEnglishName(id))
				.tag(BlockTags.CAULDRONS).register();
	}

}
