package dev.xkmc.fruitsdelight.compat.create;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.EmptyingRecipe;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.mixer.CompactingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.registrate.FDBlocks;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import net.minecraft.resources.ResourceLocation;
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

			compacting(jamBlock.getId())
					.withFluidIngredients(FluidIngredient.fromFluid(jamFluid.get(), 1000))
					.output(jamBlock)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			compacting(jelloBlock.getId())
					.withFluidIngredients(FluidIngredient.fromFluid(jelloFluid.get(), 1000))
					.output(jelloBlock)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			filling(jamItem.getId())
					.withFluidIngredients(FluidIngredient.fromFluid(jamFluid.get(), 125))
					.withItemIngredients(Ingredient.of(Items.GLASS_BOTTLE))
					.output(jamItem)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			filling(jelloItem.getId())
					.withFluidIngredients(FluidIngredient.fromFluid(jelloFluid.get(), 125))
					.withItemIngredients(Ingredient.of(Items.BOWL))
					.output(jelloItem)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			emptying(jamItem.getId().withSuffix("_emptying"))
					.withItemIngredients(Ingredient.of(jamItem.get()))
					.output(Items.GLASS_BOTTLE)
					.output(jamFluid.get(), 125)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

			emptying(jelloItem.getId().withSuffix("_emptying"))
					.withItemIngredients(Ingredient.of(jelloItem.get()))
					.output(Items.BOWL)
					.output(jelloFluid.get(), 125)
					.build(ConditionalRecipeWrapper.mod(pvd, Create.ID));

		}
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
}
