package dev.xkmc.fruitsdelight.compat.jei;

import dev.xkmc.fruitsdelight.content.cauldrons.CauldronRecipe;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.fruitsdelight.init.food.FDCauldrons;
import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CauldronRecipeCategory extends BaseRecipeCategory<CauldronRecipe, CauldronRecipeCategory> {

	protected static final ResourceLocation BG = FruitsDelight.loc("textures/jei/background.png");

	private final LangData lang;


	public CauldronRecipeCategory(LangData lang, String id) {
		super(FruitsDelight.loc(id), CauldronRecipe.class);
		this.lang = lang;
	}

	public CauldronRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 18, 108, 18);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, FDCauldrons.FAKE_CAULDRON.asStack());
		return this;
	}

	@Override
	public Component getTitle() {
		return lang.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, CauldronRecipe recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(recipe.getBlockInput());
		builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addItemStack(recipe.getItemInput());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 1).addItemStack(recipe.getBlockOutput());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 91, 1).addItemStack(recipe.getItemOutput());
	}

}
