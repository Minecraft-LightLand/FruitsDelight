package dev.xkmc.fruitsdelight.content.cauldrons;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class JellyCauldronBlock extends FDCauldronBlock {

	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 3);

	public final FruitType type;

	public JellyCauldronBlock(Properties properties, FruitType type, String name) {
		super(properties, name);
		this.type = type;
		registerDefaultState(defaultBlockState().setValue(LEVEL, 3));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
	}

	public <T extends FDCauldronBlock> void build(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider pvd) {
		pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
			int value = state.getValue(LEVEL);
			String name = value == 3 ? "" : "_level" + value;
			String parent = value == 3 ? "_full" : "_level" + value;
			String suffix = ctx.getName().split("_")[1];
			return ConfiguredModel.builder().modelFile(CauldronRenderHandler.guiAndTexture(pvd.models()
					.withExistingParent(ctx.getName() + name, "block/template_cauldron" + parent)
					.texture("content", pvd.modLoc("block/" + suffix)))).build();
		});
	}

}
