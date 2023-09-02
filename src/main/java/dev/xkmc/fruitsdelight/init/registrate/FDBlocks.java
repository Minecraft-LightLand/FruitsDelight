package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.world.item.CreativeModeTab;

public class FDBlocks {

	public static final RegistryEntry<CreativeModeTab> TAB =
			FruitsDelight.REGISTRATE.buildModCreativeTab("fruits_delight", "Fruits Delight",
					e -> e.icon(() -> FDTrees.PEAR.getFruit().getDefaultInstance()));

	public static void register() {
		FDTrees.register();
		FDBushes.register();
	}

}
