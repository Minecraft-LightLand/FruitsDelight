package dev.xkmc.fruitsdelight.events;

import dev.xkmc.fruitsdelight.content.block.JellyBlock;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;

@SerialClass
public class BlockEffectToClient extends SerialPacketBase {

	public enum Type {
		JELLY_SLIDE((b, e) -> {
			if (b instanceof JellyBlock j) j.showJellySlideParticles(e);
		}),
		JELLY_JUMP((b, e) -> {
			if (b instanceof JellyBlock j) j.showJellyJumpParticles(e);
		}),
		;

		public final BiConsumer<Block, Entity> cons;

		Type(BiConsumer<Block, Entity> cons) {
			this.cons = cons;
		}
	}

	@SerialClass.SerialField
	public Block block;
	@SerialClass.SerialField
	public int id;
	@SerialClass.SerialField
	public Type type;

	@Deprecated
	public BlockEffectToClient() {

	}

	public BlockEffectToClient(Block block, int id, Type type) {
		this.block = block;
		this.id = id;
		this.type = type;
	}

	@Override
	public void handle(NetworkEvent.Context context) {
		BlockEffectHandler.handle(block, id, type);
	}

}
