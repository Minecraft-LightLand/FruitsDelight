package dev.xkmc.fruitsdelight.events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;

public class BlockEffectHandler {

	public static void handle(Block block, int id, BlockEffectToClient.Type type) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		Entity e = level.getEntity(id);
		if (e == null) return;
		type.cons.accept(block, e);
	}

}
