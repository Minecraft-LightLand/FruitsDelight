package dev.xkmc.fruitsdelight.events;

import dev.xkmc.fruitsdelight.content.block.JamBlock;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public record BlockEffectToClient(
		Block block, int id, Type type
) implements SerialPacketBase<BlockEffectToClient> {

	public enum Type {
		JAM_SLIDE((b, e) -> {
			if (b instanceof JamBlock j) j.showJamSlideParticles(e);
		}),
		JAM_JUMP((b, e) -> {
			if (b instanceof JamBlock j) j.showJamJumpParticles(e);
		}),
		;

		public final BiConsumer<Block, Entity> cons;

		Type(BiConsumer<Block, Entity> cons) {
			this.cons = cons;
		}
	}

	@Override
	public void handle(Player player) {
		BlockEffectHandler.handle(block, id, type);
	}

}
