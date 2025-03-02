package dev.xkmc.fruitsdelight.init.data;

import com.simibubi.create.Create;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.fruitsdelight.compat.create.CreateRecipeGen;
import dev.xkmc.fruitsdelight.content.recipe.JamCraftShapelessBuilder;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDCrates;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FDJuice;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.plants.*;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.BiFunction;

public class RecipeGen {

	public static void genRecipes(RegistrateRecipeProvider pvd) {
		PlantDataEntry.gen(pvd, PlantDataEntry::genRecipe);
		FDCrates.genRecipes(pvd);
		{

			for (var e : FruitType.values()) {
				jam(pvd, e);
				storageBlock(pvd, FDItems.JELLO[e.ordinal()].get(),
						FDBlocks.JELLO[e.ordinal()].get(), Items.BOWL);

				storageBlock(pvd, FDItems.JAM[e.ordinal()].asItem(),
						FDBlocks.JAM[e.ordinal()].get(), Items.GLASS_BOTTLE);
			}

			for (var e : FDJuice.values()) {
				e.recipe(pvd);
			}

			{
				CuttingBoardRecipeBuilder.cuttingRecipe(FruitType.LEMON.getFruitTag(),
								Ingredient.of(CommonTags.TOOLS_KNIFE), FDFood.LEMON_SLICE.item.get(), 4, 1)
						.addResult(FDBushes.LEMON.getSeed())
						.build(pvd, FruitsDelight.loc("lemon_cutting"));

				CuttingBoardRecipeBuilder.cuttingRecipe(FruitType.ORANGE.getFruitTag(),
								Ingredient.of(CommonTags.TOOLS_KNIFE), FDFood.ORANGE_SLICE.item.get(), 4, 1)
						.build(pvd, FruitsDelight.loc("orange_cutting"));

				CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(FDBlocks.FIG_PUDDING.get()),
								Ingredient.of(CommonTags.TOOLS_KNIFE), FDFood.FIG_PUDDING_SLICE.item.get(), 4, 1)
						.build(pvd, FruitsDelight.loc("fig_pudding_cutting"));

				CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(FDTrees.DURIAN.getFruit()),
								Ingredient.of(ItemTags.AXES), FDFood.DURIAN_FLESH.item.get(), 6, 1)
						.addResult(Durian.UPPER)
						.addResult(Durian.LOWER)
						.addResult(FDTrees.DURIAN.getSapling())
						.build(pvd, FruitsDelight.loc("durian_cutting"));

				pvd.singleItemUnfinished(DataIngredient.items(Durian.LOWER.get()), RecipeCategory.MISC,
						() -> Items.BOWL, 1, 1).save(pvd,
						FruitsDelight.loc("durian_upper_to_bowl"));
				pvd.singleItemUnfinished(DataIngredient.items(Durian.UPPER.get()), RecipeCategory.MISC,
						() -> Items.BOWL, 1, 1).save(pvd,
						FruitsDelight.loc("durian_lower_to_bowl"));
			}

			{
				smoking(pvd, FDFood.BAKED_PEAR);
				smoking(pvd, FDFood.DRIED_PERSIMMON);
				smoking(pvd, FDFood.BAKED_DURIAN);
			}

			{

				unlock(pvd, new JamCraftShapelessBuilder(FDFood.HAWBERRY_ROLL.item, 1)::unlockedBy,
						FDFood.HAWBERRY_SHEET.item.get())
						.requires(FDFood.HAWBERRY_SHEET.item.get())
						.requires(TagGen.JAM)
						.save(pvd);

				unlock(pvd, new JamCraftShapelessBuilder(FDFood.MANGO_SALAD.item, 1)::unlockedBy,
						FDFood.MANGO_SALAD.getFruit())
						.requires(Items.BOWL)
						.requires(FDFood.MANGO_SALAD.getFruitTag())
						.requires(TagGen.JAM)
						.save(pvd);

				unlock(pvd, new JamCraftShapelessBuilder(FDFood.JAM_BREAD.item, 1)::unlockedBy,
						Items.BREAD)
						.requires(Items.BREAD)
						.requires(TagGen.JAM)
						.save(pvd);
			}

			{

				unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FDFood.HAWBERRY_SHEET.item)::unlockedBy,
						FDFood.HAWBERRY_SHEET.getFruit())
						.requires(FDFood.HAWBERRY_SHEET.getFruitTag(), 3)
						.requires(Items.SUGAR)
						.save(pvd);


				unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FDFood.HAWBERRY_STICK.item)::unlockedBy,
						FDFood.HAWBERRY_STICK.getFruit())
						.requires(FDFood.HAWBERRY_SHEET.getFruitTag(), 3)
						.requires(Items.STICK)
						.requires(Items.SUGAR, 2)
						.save(pvd);

				unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FDFood.HAMIMELON_SHAVED_ICE.item)::unlockedBy,
						FDFood.HAMIMELON_SHAVED_ICE.getFruit())
						.requires(Items.GLASS_BOTTLE)
						.requires(FDFood.HAMIMELON_SHAVED_ICE.getFruitTag(), 2)
						.requires(CommonTags.FOODS_MILK)
						.requires(Items.SUGAR)
						.requires(Items.ICE)
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDFood.PERSIMMON_COOKIE.item, 8)::unlockedBy,
						FDFood.PERSIMMON_COOKIE.getFruit())
						.pattern("ABA")
						.define('A', Items.WHEAT)
						.define('B', FDFood.DRIED_PERSIMMON.item.get())
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDFood.CRANBERRY_COOKIE.item, 8)::unlockedBy,
						FDFood.CRANBERRY_COOKIE.getFruit())
						.pattern("ABA")
						.define('A', Items.WHEAT)
						.define('B', FDFood.CRANBERRY_COOKIE.getFruitTag())
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDFood.LEMON_COOKIE.item, 8)::unlockedBy,
						FDFood.LEMON_COOKIE.getFruit())
						.pattern(" C ").pattern("ABA")
						.define('C', CommonTags.FOODS_MILK)
						.define('A', Items.WHEAT)
						.define('B', FDFood.LEMON_COOKIE.getFruitTag())
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDFood.BAYBERRY_COOKIE.item, 8)::unlockedBy,
						FDFood.BAYBERRY_COOKIE.getFruit())
						.pattern("ABA")
						.define('A', Items.WHEAT)
						.define('B', FDFood.BAYBERRY_COOKIE.getFruitTag())
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDFood.HAMIMELON_POPSICLE.item.get(), 1)::unlockedBy,
						FDFood.HAMIMELON_POPSICLE.getFruit())
						.pattern(" MM").pattern("IMM").pattern("SI ")
						.define('I', Items.ICE)
						.define('M', FDFood.HAMIMELON_POPSICLE.getFruitTag())
						.define('S', Items.STICK)
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDFood.KIWI_POPSICLE.item.get(), 1)::unlockedBy,
						FDFood.KIWI_POPSICLE.getFruit())
						.pattern(" MM").pattern("IMM").pattern("SI ")
						.define('I', Items.ICE)
						.define('M', FDFood.KIWI_POPSICLE.getFruitTag())
						.define('S', Items.STICK)
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDBlocks.FIG_PUDDING.get(), 1)::unlockedBy,
						FDFood.FIG_PUDDING_SLICE.getFruit())
						.pattern("MSM").pattern("EJE").pattern("FDF")
						.define('D', CommonTags.FOODS_DOUGH)
						.define('F', FDFood.FIG_PUDDING_SLICE.getFruitTag())
						.define('E', Items.EGG)
						.define('M', CommonTags.FOODS_MILK)
						.define('J', FruitType.ORANGE.getJam())
						.define('S', Items.SWEET_BERRIES)
						.save(pvd);

				unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FDBlocks.FIG_PUDDING.get(), 1)::unlockedBy,
						FDFood.FIG_PUDDING_SLICE.getFruit())
						.pattern("FF").pattern("FF")
						.define('F', FDFood.FIG_PUDDING_SLICE.get())
						.save(pvd, FDBlocks.FIG_PUDDING.getId().withSuffix("_from_slice"));

			}

			{

				CookingPotRecipeBuilder.cookingPotRecipe(FDBlocks.PINEAPPLE_RICE.get(), 1, 200, 0.1f, Items.BOWL)
						.addIngredient(FDPineapple.PINEAPPLE.getWholeFruit())
						.addIngredient(Ingredient.of(CommonTags.CROPS_RICE), 3)
						.addIngredient(CommonTags.FOODS_CABBAGE)
						.addIngredient(Tags.Items.EGGS)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.MANGO_MILKSHAKE.item, 1, 200, 0.1f, Items.GLASS_BOTTLE)
						.addIngredient(FDFood.MANGO_MILKSHAKE.getFruitTag())
						.addIngredient(CommonTags.FOODS_MILK)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.BLUEBERRY_CUSTARD.item, 1, 200, 0.1f, Items.GLASS_BOTTLE)
						.addIngredient(FDFood.BLUEBERRY_CUSTARD.getFruitTag(), 2)
						.addIngredient(CommonTags.FOODS_MILK)
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.PINEAPPLE_PIE.item, 2, 200, 0.1f)
						.addIngredient(FDFood.PINEAPPLE_PIE.getFruitTag(), 2)
						.addIngredient(ModItems.PIE_CRUST.get())
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.DURIAN_PIE.item, 2, 200, 0.1f)
						.addIngredient(FDFood.DURIAN_PIE.getFruitTag(), 2)
						.addIngredient(ModItems.PIE_CRUST.get())
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.LEMON_TART.item, 2, 200, 0.1f)
						.addIngredient(FDFood.LEMON_TART.getFruitTag())
						.addIngredient(ModItems.PIE_CRUST.get())
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.addIngredient(CommonTags.FOODS_MILK)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.FIG_TART.item, 2, 200, 0.1f)
						.addIngredient(FDFood.FIG_TART.getFruitTag())
						.addIngredient(ModItems.PIE_CRUST.get())
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.addIngredient(CommonTags.FOODS_MILK)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.BLUEBERRY_MUFFIN.item, 2, 200, 0.1f)
						.addIngredient(FDFood.BLUEBERRY_MUFFIN.getFruitTag(), 2)
						.addIngredient(CommonTags.FOODS_DOUGH)
						.addIngredient(CommonTags.FOODS_MILK)
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.CRANBERRY_MUFFIN.item, 2, 200, 0.1f)
						.addIngredient(FDFood.CRANBERRY_MUFFIN.getFruitTag(), 2)
						.addIngredient(CommonTags.FOODS_DOUGH)
						.addIngredient(CommonTags.FOODS_MILK)
						.addIngredient(Tags.Items.EGGS)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.ORANGE_CHICKEN.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(CommonTags.FOODS_RAW_CHICKEN)
						.addIngredient(FDFood.ORANGE_SLICE.item.get(), 4)
						.addIngredient(Items.SUGAR)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.FIG_CHICKEN_STEW.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(CommonTags.FOODS_RAW_CHICKEN)
						.addIngredient(FDFood.FIG_CHICKEN_STEW.getFruitTag(), 2)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.ORANGE_MARINATED_PORK.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(CommonTags.FOODS_RAW_PORK)
						.addIngredient(FDFood.ORANGE_SLICE.item.get(), 4)
						.addIngredient(CommonTags.FOODS_CABBAGE)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.PEAR_WITH_ROCK_SUGAR.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(Items.SUGAR, 4)
						.addIngredient(FDFood.PEAR_WITH_ROCK_SUGAR.getFruitTag(), 2)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.MANGOSTEEN_CAKE.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(Items.WHEAT, 2)
						.addIngredient(Items.SUGAR, 2)
						.addIngredient(FDFood.MANGOSTEEN_CAKE.getFruitTag(), 2)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.LYCHEE_CHICKEN.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(CommonTags.FOODS_RAW_CHICKEN)
						.addIngredient(FDFood.LYCHEE_CHICKEN.getFruitTag(), 4)
						.addIngredient(CommonTags.FOODS_CABBAGE)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);

				CookingPotRecipeBuilder.cookingPotRecipe(FDFood.PINEAPPLE_MARINATED_PORK.item, 1, 200, 0.1f, Items.BOWL)
						.addIngredient(CommonTags.FOODS_RAW_PORK)
						.addIngredient(FDFood.PINEAPPLE_MARINATED_PORK.getFruitTag(), 4)
						.addIngredient(Items.CARROT)
						.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
						.build(pvd);
			}

		}

		 if (ModList.get().isLoaded(Create.ID)) CreateRecipeGen.onRecipeGen(pvd);


	}

	private static void storageBlock(RegistrateRecipeProvider pvd, Item item, Block block, Item cont) {
		unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,
				block)::unlockedBy, item)
				.requires(item, 8)
				.save(pvd);
		unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,
				item, 8)::unlockedBy, block.asItem())
				.requires(block).requires(cont, 8)
				.save(pvd);
	}

	private static void jam(RegistrateRecipeProvider pvd, FruitType type) {
		CookingPotRecipeBuilder.cookingPotRecipe(type.getJam(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
				.addIngredient(type.getFruitTag(), type.jamCost)
				.addIngredient(Items.SUGAR)
				.addIngredient(FDFood.LEMON_SLICE.item.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.build(pvd);
	}

	private static void smoking(RegistrateRecipeProvider pvd, FDFood food) {
		pvd.smoking(DataIngredient.ingredient(food.getFruitTag(), food.getFruit()), RecipeCategory.FOOD, food.item, 0.1f);
		pvd.campfire(DataIngredient.ingredient(food.getFruitTag(), food.getFruit()), RecipeCategory.FOOD, food.item, 0.1f);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}


}
