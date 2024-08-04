package dev.xkmc.fruitsdelight.init.plants;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.fruitsdelight.content.block.DoubleFruitBushBlock;
import dev.xkmc.fruitsdelight.content.block.FruitBushBlock;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public enum FDBushType {
	CROSS,
	BLOCK,
	TALL;

	public BlockEntry<? extends BushBlock> build(String name, FDBushes bush){
		return switch (this){
			case CROSS ->  FruitsDelight.REGISTRATE
					.block(name, p -> new FruitBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH), bush::getFruit, this))
					.blockstate(bush::buildBushModel)
					.loot(bush::buildLoot)
					.tag(BlockTags.SWORD_EFFICIENT)
					.register();
			case BLOCK -> FruitsDelight.REGISTRATE
					.block(name, p -> new FruitBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AZALEA), bush::getFruit, this))
					.blockstate(bush::buildBushModel)
					.loot(bush::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
					.item().build()
					.register();
			case TALL -> FruitsDelight.REGISTRATE
					.block(name, p -> new DoubleFruitBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.AZALEA), bush::getFruit, this))
					.blockstate(bush::buildBushModel)
					.loot(bush::buildLoot)
					.tag(BlockTags.MINEABLE_WITH_AXE, BlockTags.SWORD_EFFICIENT)
					.item().build()
					.register();
		};
	}

}
