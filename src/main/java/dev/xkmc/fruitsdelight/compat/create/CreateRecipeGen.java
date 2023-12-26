package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDMelons;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.List;

public class CreateRecipeGen {
	public static void onRecipeGen(RegistrateRecipeProvider pvd) {
		for (var e : FruitType.values()) {
			var jamFluid = CreateCompat.JAM[e.ordinal()];
			var jelloFluid = CreateCompat.JELLO[e.ordinal()];
			var jamBlock = FDBlocks.JELLY[e.ordinal()];
			var jelloBlock = FDBlocks.JELLO[e.ordinal()];
			var jamItem = FDItems.JELLY[e.ordinal()];
			var jelloItem = FDItems.JELLO[e.ordinal()];
			int cost = e.jellyCost;

			var b = mixing(jamFluid.getId());
			b.withFluidIngredients(FluidIngredient.fromFluid(Fluids.WATER, 100));
			List<Ingredient> list = new ArrayList<>(List.of(
					Ingredient.of(FDFood.LEMON_SLICE.get()), Ingredient.of(Items.SUGAR)));
			for (int i = 0; i < cost; i++)
				list.add(Ingredient.of(e.fruit.get()));
			b.withItemIngredients(list.toArray(Ingredient[]::new));
			b.output(jamFluid.get(), 125);
			b.requiresHeat(HeatCondition.HEATED);
			b.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			mixing(jelloFluid.getId())
					.withFluidIngredients(FluidIngredient.fromFluid(jamFluid.get(), 125))
					.withItemIngredients(Ingredient.of(Items.SLIME_BALL))
					.requiresHeat(HeatCondition.HEATED)
					.output(jelloFluid.get(), 125)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			filling(jamItem.getId().withSuffix("_bread"))
					.withFluidIngredients(FluidIngredient.fromFluid(jamFluid.get(), 125))
					.withItemIngredients(Ingredient.of(Items.BREAD))
					.output(FDFoodItem.setContent(FDFood.JELLY_BREAD.get(), e))
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			fluidRecipes(pvd, jamBlock, jamFluid, jamItem, Items.GLASS_BOTTLE);
			fluidRecipes(pvd, jelloBlock, jelloFluid, jelloItem, Items.BOWL);

		}

		// cut
		{
			cutting(FDFood.LEMON_SLICE.item.getId())
					.withItemIngredients(Ingredient.of(FDBushes.LEMON.getFruit()))
					.output(FDFood.LEMON_SLICE.get(), 4)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			cutting(FDFood.ORANGE_SLICE.item.getId())
					.withItemIngredients(Ingredient.of(FDTrees.ORANGE.getFruit()))
					.output(FDFood.ORANGE_SLICE.get(), 4)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			cutting(new ResourceLocation(FruitsDelight.MODID, "hamimelon_slice"))
					.withItemIngredients(Ingredient.of(FDMelons.HAMIMELON.getMelonBlock()))
					.output(FDMelons.HAMIMELON.getSlice(), 9)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			cutting(new ResourceLocation(FruitsDelight.MODID, "pineapple_slice"))
					.withItemIngredients(Ingredient.of(FDPineapple.PINEAPPLE.getWholeFruit()))
					.output(FDPineapple.PINEAPPLE.getSlice(), 8)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));
		}
	}

	private static void fluidRecipes(RegistrateRecipeProvider pvd,
									 BlockEntry<?> block,
									 FluidEntry<?> fluid,
									 ItemEntry<?> item,
									 Item container) {

		compacting(block.getId())
				.withFluidIngredients(FluidIngredient.fromFluid(fluid.get(), 1000))
				.output(block)
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		filling(item.getId())
				.withFluidIngredients(FluidIngredient.fromFluid(fluid.get(), 125))
				.withItemIngredients(Ingredient.of(container))
				.output(item)
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		emptying(item.getId().withSuffix("_emptying"))
				.withItemIngredients(Ingredient.of(item.get()))
				.output(container)
				.output(fluid.get(), 125)
				.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));
	}

	private static ProcessingRecipeBuilder<MixingRecipe> mixing(ResourceLocation id) {
		ProcessingRecipeSerializer<MixingRecipe> ser = AllRecipeTypes.MIXING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<CompactingRecipe> compacting(ResourceLocation id) {
		ProcessingRecipeSerializer<CompactingRecipe> ser = AllRecipeTypes.COMPACTING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<FillingRecipe> filling(ResourceLocation id) {
		ProcessingRecipeSerializer<FillingRecipe> ser = AllRecipeTypes.FILLING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<EmptyingRecipe> emptying(ResourceLocation id) {
		ProcessingRecipeSerializer<EmptyingRecipe> ser = AllRecipeTypes.EMPTYING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

	private static ProcessingRecipeBuilder<CuttingRecipe> cutting(ResourceLocation id) {
		ProcessingRecipeSerializer<CuttingRecipe> ser = AllRecipeTypes.CUTTING.getSerializer();
		return new ProcessingRecipeBuilder<>(ser.getFactory(), id);
	}

}
