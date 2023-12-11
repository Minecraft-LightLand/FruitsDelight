package dev.xkmc.fruitsdelight.compat.jei;

import dev.xkmc.fruitsdelight.content.cauldrons.CauldronRecipe;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.LangData;
import dev.xkmc.l2library.util.Proxy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.common.registry.ModItems;

@JeiPlugin
public class FDJeiPlugin implements IModPlugin {

	public static FDJeiPlugin INSTANCE;

	public final ResourceLocation UID = new ResourceLocation(FruitsDelight.MODID, "main");

	public final CauldronRecipeCategory NO_HEAT = new CauldronRecipeCategory(LangData.JEI_CAULDRON, "cauldron");
	public final CauldronRecipeCategory HEAT = new CauldronRecipeCategory(LangData.JEI_CAULDRON_HEAT, "cauldron_heated");

	public IGuiHelper GUI_HELPER;

	public FDJeiPlugin() {
		INSTANCE = this;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(NO_HEAT.init(helper));
		registration.addRecipeCategories(HEAT.init(helper));
		GUI_HELPER = helper;
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		var level = Proxy.getClientWorld();
		assert level != null;
		registration.addRecipes(NO_HEAT.getRecipeType(), CauldronRecipe.LIST.stream().filter(e -> !e.requiresHeat()).toList());
		registration.addRecipes(HEAT.getRecipeType(), CauldronRecipe.LIST.stream().filter(CauldronRecipe::requiresHeat).toList());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(Items.CAULDRON.getDefaultInstance(), NO_HEAT.getRecipeType());
		registration.addRecipeCatalyst(Items.CAULDRON.getDefaultInstance(), HEAT.getRecipeType());
		registration.addRecipeCatalyst(Items.LAVA_BUCKET.getDefaultInstance(), HEAT.getRecipeType());
		registration.addRecipeCatalyst(Items.FLINT_AND_STEEL.getDefaultInstance(), HEAT.getRecipeType());
		registration.addRecipeCatalyst(ModItems.STOVE.get().getDefaultInstance(), HEAT.getRecipeType());
	}

}
