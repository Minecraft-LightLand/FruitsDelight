package dev.xkmc.fruitsdelight.init.plants;

import dev.xkmc.fruitsdelight.content.block.DoubleFruitBushBlock;
import dev.xkmc.fruitsdelight.content.block.FruitBushBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.l2library.repack.registrate.util.entry.BlockEntry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public enum FDBushType {
	CROSS,
	BLOCK,
	TALL;

	public BlockEntry<? extends BushBlock> build(String name, FDBushes bush) {
		return switch (this) {
			case CROSS -> FruitsDelight.REGISTRATE
					.block(name, p -> new FruitBushBlock(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH), bush::getFruit, this))
					.blockstate(bush::buildBushModel)
					.loot(bush::buildLoot)
					.register();
			case BLOCK -> FruitsDelight.REGISTRATE
					.block(name, p -> new FruitBushBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA), bush::getFruit, this))
					.blockstate(bush::buildBushModel)
					.loot(bush::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.item().build()
					.register();
			case TALL -> FruitsDelight.REGISTRATE
					.block(name, p -> new DoubleFruitBushBlock(BlockBehaviour.Properties.copy(Blocks.AZALEA), bush::getFruit, this))
					.blockstate(bush::buildBushModel)
					.loot(bush::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.item().build()
					.register();
		};
	}

}
