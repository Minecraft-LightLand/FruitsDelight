package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.xkmc.fruitsdelight.content.cauldrons.*;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;

public class FDCauldrons {

	public static final BlockEntry<FDCauldronBlock> LEMON;
	public static final BlockEntry<FruitCauldronBlock>[] FRUIT;
	public static final BlockEntry<JellyCauldronBlock>[] JELLY;
	public static final BlockEntry<JellyCauldronBlock>[] JELLO;

	public static final ItemEntry<Item> FAKE_CAULDRON;

	static {
		LEMON = simple("lemonade_cauldron", FDCauldronBlock::new);
		int size = FruitType.values().length;
		FRUIT = new BlockEntry[size];
		JELLY = new BlockEntry[size];
		JELLO = new BlockEntry[size];
		for (int i = 0; i < size; i++) {
			FruitType type = FruitType.values()[i];
			String name = type.name().toLowerCase(Locale.ROOT);
			FRUIT[i] = simple(name + "_cauldron", properties -> new FruitCauldronBlock(properties, type));
			JELLY[i] = simple(name + "_jelly_cauldron", properties -> new JellyCauldronBlock(properties, type));
			JELLO[i] = simple(name + "_jello_cauldron", properties -> new JellyCauldronBlock(properties, type));
		}
		FAKE_CAULDRON = FruitsDelight.REGISTRATE.item("dummy_cauldron", Item::new)
				.lang("Water Cauldron")
				.model((ctx, pvd) -> CauldronRenderHandler.gui(pvd.withExistingParent(ctx.getName(), "block/water_cauldron_full")))
				.color(() -> () -> CauldronRenderHandler::getItemColor)
				.removeTab(FruitsDelight.TAB.getKey())
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
		var jellyProp = JellyCauldronBlock.LEVEL;
		int max = FruitCauldronBlock.MAX;

		for (FruitType type : FruitType.values()) {
			int level = 4 / type.jellyCost;
			int index = type.ordinal();
			var fruit = FRUIT[index].get();
			var jelly = JELLY[index].get();
			var jello = JELLO[index].get();

			CauldronRecipe.create(CauldronInteraction.WATER,
					FAKE_CAULDRON.get(),
					type.getJelly(), 3, FDCauldronInteraction.of(state ->
							state.getValue(LayeredCauldronBlock.LEVEL) == 3 ?
									fruit.defaultBlockState().setValue(fruitProp, 4) : null),
					fruit.asItem());

			CauldronRecipe.create(LEMON.get(), type.fruit.get(), 12 / level, FDCauldronInteraction.withHeat(state ->
							fruit.defaultBlockState().setValue(fruitProp, level)),
					fruit.asItem());
			fruit.getInteractions().put(type.fruit.get(), FDCauldronInteraction.withHeat(state ->
					state.getValue(fruitProp) == max ? null : state.setValue(fruitProp, Math.min(max, state.getValue(fruitProp) + level))));
			fruit.getInteractions().put(type.getJelly(), FDCauldronInteraction.of(state ->
					state.getValue(fruitProp) == max ? null : state.setValue(fruitProp, Math.min(max, state.getValue(fruitProp) + 4))));
			CauldronRecipe.create(fruit, Items.SUGAR, 1, FDCauldronInteraction.withHeat(state ->
							state.getValue(fruitProp) == max ? jelly.defaultBlockState() : null),
					jelly.asItem());
			CauldronRecipe.create(jelly, Items.SLIME_BALL, 1, FDCauldronInteraction.withHeat(state ->
							jello.defaultBlockState().setValue(jellyProp, state.getValue(jellyProp))),
					jello.asItem());
			CauldronRecipe.empty(jelly, Items.GLASS_BOTTLE, 3, FDCauldronInteraction.of(state ->
									state.getValue(jellyProp) == 1 ? Blocks.CAULDRON.defaultBlockState() :
											state.setValue(jellyProp, state.getValue(jellyProp) - 1),
							type.getJelly().getDefaultInstance(), SoundEvents.BOTTLE_FILL),
					Items.CAULDRON);

			CauldronRecipe.empty(jello, Items.BOWL, 3, FDCauldronInteraction.of(state ->
									state.getValue(jellyProp) == 1 ? Blocks.CAULDRON.defaultBlockState() :
											state.setValue(jellyProp, state.getValue(jellyProp) - 1),
							type.getJello().getDefaultInstance(), SoundEvents.BOTTLE_FILL),
					Items.CAULDRON);

			DispenserBlock.registerBehavior(type.getJelly(), new CauldronDispenseBehavior());
		}
		DispenserBlock.registerBehavior(FDFood.LEMON_SLICE.item.get(), new CauldronDispenseBehavior());
	}

	private static <T extends FDCauldronBlock> BlockEntry<T> simple(String id, NonNullFunction<BlockBehaviour.Properties, T> factory) {
		return FruitsDelight.REGISTRATE.block(id, p -> factory.apply(BlockBehaviour.Properties.copy(Blocks.WATER_CAULDRON)))
				.blockstate((ctx, pvd) -> ctx.get().build(ctx, pvd))
				.loot((pvd, block) -> pvd.dropOther(block, Items.CAULDRON))
				.color(() -> () -> CauldronRenderHandler::getBlockColor)
				.item().removeTab(FruitsDelight.TAB.getKey())
				.color(() -> () -> CauldronRenderHandler::getItemColor)
				.build()
				.tag(BlockTags.CAULDRONS).register();
	}

}
