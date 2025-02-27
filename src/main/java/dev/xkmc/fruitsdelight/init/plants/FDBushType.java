package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.DoubleFruitBushBlock;
import dev.xkmc.fruitsdelight.content.block.FruitBushBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import static net.minecraft.world.level.block.DoublePlantBlock.HALF;

public enum FDBushType {
	CROSS,
	BLOCK,
	TALL;

	public BlockEntry<? extends BushBlock> build(String name, FDBushes bush){
		return switch (this){
			case CROSS ->  FruitsDelight.REGISTRATE
					.block(name, p -> new FruitBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH), bush::getFruit, this))
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
						int age = state.getValue(FruitBushBlock.AGE);
						String id = ctx.getName();
						if (age == 1) {
							return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_0"))).build();
						}
						id += "_" + (age == 0 ? 0 : age - 1);
						var model = pvd.models().cross(id, pvd.modLoc("block/" + id)).renderType("cutout");
						return ConfiguredModel.builder().modelFile(model).build();
					}))
					.loot(bush::buildLoot)
					.tag(BlockTags.SWORD_EFFICIENT)
					.register();
			case BLOCK -> FruitsDelight.REGISTRATE
					.block(name, p -> new FruitBushBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA), bush::getFruit, this))
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
						int age = state.getValue(FruitBushBlock.AGE);
						String id = ctx.getName();
						String parent = "bush";
						if (age == 0) {
							id += "_small";
							parent += "_small";
						}
						if (age == 1) {
							return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + id + "_small"))).build();
						}
						if (age == 3) id += "_flowers";
						if (age == 4) id += "_fruits";
						var model = pvd.models().getBuilder(id)
								.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + parent)));
						model.texture("face", "block/" + id + "_face");
						model.texture("cross", "block/" + id + "_cross");
						return ConfiguredModel.builder().modelFile(model).build();
					}))
					.loot(bush::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
					.item().build()
					.register();
			case TALL -> FruitsDelight.REGISTRATE
					.block(name, p -> new DoubleFruitBushBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA), bush::getFruit, this))
					.blockstate((ctx, pvd) -> pvd.getVariantBuilder(ctx.get()).forAllStates(state -> {
						int age = state.getValue(FruitBushBlock.AGE);
						String modelId = ctx.getName();
						String parent = "tall_bush";
						String trunk = age <= 1 ? "_small_trunk" : "_trunk";
						String tex = "";
						if (age == 0) {
							modelId += "_small";
							parent += "_small";
						}
						if (age == 1) {
							modelId += "_mid";
							parent += "_mid";
						}
						if (age == 3) tex = "_flowers";
						if (age == 4) tex = "_fruits";
						modelId += tex;
						tex = ctx.getName() + tex;
						if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
							return ConfiguredModel.builder().modelFile(pvd.models().cubeColumn(modelId + "_upper",
											pvd.modLoc("block/" + tex + "_upper"),
											pvd.modLoc("block/" + tex + "_top"))
									.texture("particle", pvd.modLoc("block/" + tex + "_upper"))
							).build();
						} else {
							var model = pvd.models().getBuilder(modelId)
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("block/" + parent)));
							model.texture("upper", "block/" + tex + "_upper");
							model.texture("lower", "block/" + tex + "_lower");
							model.texture("trunk", "block/" + ctx.getName() + trunk);
							model.texture("top", "block/" + tex + "_top");
							return ConfiguredModel.builder().modelFile(model).build();
						}
					}))
					.loot(bush::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
					.item().build()
					.register();
		};
	}

}
