package dev.xkmc.fruitsdelight.content.cauldrons;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface SimpleInteraction {

	@Nullable
	BlockState perform(BlockState state);

}
