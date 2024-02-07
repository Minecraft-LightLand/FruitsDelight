package dev.xkmc.fruitsdelight.compat.botanypot;

import dev.xkmc.fruitsdelight.content.block.BaseBushBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;

import java.util.ArrayList;

public record PotDisplayBush(String type, ArrayList<Phase> phases) {

	public static PotDisplayBush of(BushBlock block) {
		ArrayList<Phase> list = new ArrayList<>();
		for (int i = 0; i <= 4; i++) {
			list.add(new Phase(block, new Prop(i)));
		}
		return new PotDisplayBush("botanypots:transitional", list);
	}

	record Phase(Block block, Prop properties) {

	}

	record Prop(int age) {

	}

}
