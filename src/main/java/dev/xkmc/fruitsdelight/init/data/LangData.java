package dev.xkmc.fruitsdelight.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.Locale;

public enum LangData {
	CHANCE_EFFECT("tooltip.chance", "%1$s with %2$s%% chance", 2, ChatFormatting.GRAY),
	JELLY_CONTENT("tooltip.jelly_content", "Contains:", 0, ChatFormatting.YELLOW),
	ALLOW_JELLY("tooltip.allow_jelly", "Gain effects from jelly ingredients", 0, ChatFormatting.GRAY),
	JEI_CAULDRON("jei.cauldron", "Click Cauldron", 0, null),
	JEI_CAULDRON_HEAT("jei.cauldron_heat", "Click Heated Cauldron", 0, null),
	TOOLTIP_JELLY("tooltip.jelly_block", "Sticky, but not connecting to other sticky blocks.", 0, ChatFormatting.GRAY),
	TOOLTIP_JELLO("tooltip.jello_block", "Slippery. Sticks to sticky blocks and same jello/jelly blocks.", 0, ChatFormatting.GRAY),
	;

	private final String key, def;
	private final int arg;
	private final ChatFormatting format;


	LangData(String key, String def, int arg, @Nullable ChatFormatting format) {
		this.key = FruitsDelight.MODID + "." + key;
		this.def = def;
		this.arg = arg;
		this.format = format;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}

	public MutableComponent get(Object... args) {
		if (args.length != arg)
			throw new IllegalArgumentException("for " + name() + ": expect " + arg + " parameters, got " + args.length);
		MutableComponent ans = Component.translatable(key, args);
		if (format != null) {
			return ans.withStyle(format);
		}
		return ans;
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (LangData lang : LangData.values()) {
			pvd.add(lang.key, lang.def);
		}
	}


}
