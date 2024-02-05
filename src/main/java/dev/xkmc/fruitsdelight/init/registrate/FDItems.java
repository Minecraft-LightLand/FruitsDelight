package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.content.block.JellyBottleBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FoodType;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.food.RecordFood;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class FDItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with");

	public static BlockEntry<JellyBottleBlock>[] JELLY;
	public static ItemEntry<Item>[] JELLO;

	static {
		int fruits = FruitType.values().length;
		JELLY = new BlockEntry[fruits];
		JELLO = new ItemEntry[fruits];
		for (int j = 0; j < fruits; j++) {
			FruitType fruit = FruitType.values()[j];
			String name = fruit.name().toLowerCase(Locale.ROOT);
			FoodType food = FoodType.JELLY;
			JELLY[j] = FruitsDelight.REGISTRATE.block(name + "_jelly", p -> new JellyBottleBlock(
							BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion(), fruit))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getBuilder(ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/jam_bottle_block")))
							.texture("cap_top", pvd.modLoc("block/jam_bottle_cap_top"))
							.texture("cap_bottom", pvd.modLoc("block/jam_bottle_cap_bottom"))
							.texture("body", pvd.modLoc("block/jam_bottle_body"))
							.texture("content", pvd.modLoc("block/jam_content"))
							.renderType("cutout")))
					.item((b, p) -> food.build(p, new RecordFood(fruit, food), b))
					.transform(b -> b.model((ctx, pvd) -> pvd.generated(ctx,
									pvd.modLoc("item/jam_bottle"),
									pvd.modLoc("item/" + fruit.name().toLowerCase(Locale.ROOT) + "_jam")))
							.color(() -> () -> (stack, layer) -> layer == 0 ? -1 : fruit.color))
					.tag(food.tags)
					.build()
					.lang(FDItems.toEnglishName(name) + " Jam")
					.register();
		}
		for (int j = 0; j < fruits; j++) {
			FruitType fruit = FruitType.values()[j];
			String name = fruit.name().toLowerCase(Locale.ROOT);
			FoodType food = FoodType.JELLO;
			JELLO[j] = FruitsDelight.REGISTRATE.item(name + "_jello", p -> food.build(p, new RecordFood(fruit, food)))
					.transform(b -> food.model(b, 0, fruit)).lang(FDItems.toEnglishName(name) + " Jello").tag(food.tags)
					.register();
		}
	}

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static void register() {
	}

	public static void registerColor(RegisterColorHandlersEvent.Block event) {
		for (var e : JELLY) {
			event.register(FDItems::jellyColor, e.get());
		}
	}

	private static int jellyColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
		if (state.getBlock() instanceof JellyBottleBlock b) {
			return b.fruit.color;
		}
		return -1;
	}

}
