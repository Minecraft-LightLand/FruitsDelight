package dev.xkmc.fruitsdelight.content.cauldrons;

import dev.xkmc.fruitsdelight.init.food.FDCauldrons;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import org.jetbrains.annotations.Nullable;

public class CauldronRenderHandler {

	public static int getBlockColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int layer) {
		if (state.getBlock() instanceof JamCauldronBlock block) {
			return block.type.color;
		}
		int lemon = merge(-1, FruitType.LEMON.color, 0.3f);
		if (state.is(FDCauldrons.LEMON.get())) return lemon;
		if (state.getBlock() instanceof FruitCauldronBlock block) {
			int lv = state.getValue(FruitCauldronBlock.LEVEL);
			return merge(lemon, block.type.color, lv / 12f);
		}
		return 0xff000000 | (level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : 4159204);
	}

	private static int merge(int x, int y, float f) {
		int xr = (x >> 16) & 0xff;
		int xb = (x >> 8) & 0xff;
		int xg = x & 0xff;
		int yr = (y >> 16) & 0xff;
		int yb = (y >> 8) & 0xff;
		int yg = y & 0xff;
		int zr = Math.round(xr * (1 - f) + yr * f);
		int zb = Math.round(xb * (1 - f) + yb * f);
		int zg = Math.round(xg * (1 - f) + yg * f);
		return 0xff000000 | zr << 16 | zb << 8 | zg;
	}

	public static int getItemColor(ItemStack stack, int i) {
		BlockState block = stack.getItem() instanceof BlockItem bi ? bi.getBlock().defaultBlockState() :
				Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
		return getBlockColor(block, null, null, i);
	}

	public static <T extends ModelBuilder<T>> ModelBuilder<T> gui(T builder) {
		return builder.guiLight(BlockModel.GuiLight.SIDE)
				.transforms().transform(ItemDisplayContext.GUI)
				.rotation(30, 225, 0)
				.scale(0.625f)
				.end().end();
	}

	public static BlockModelBuilder guiAndTexture(BlockModelBuilder builder) {
		return gui(builder)
				.texture("bottom", "minecraft:block/cauldron_bottom")
				.texture("inside", "minecraft:block/cauldron_inner")
				.texture("particle", "minecraft:block/cauldron_side")
				.texture("side", "minecraft:block/cauldron_side")
				.texture("top", "minecraft:block/cauldron_top");
	}

}
