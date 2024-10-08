package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.RecipeGen;
import dev.xkmc.fruitsdelight.init.entries.FruitFluid;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.fruitsdelight.init.registrate.FDFluids;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.l2core.serial.ingredients.PotionIngredient;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum FDJuice implements IFDFood {

	HAMIMELON_JUICE(FruitType.HAMIMELON, Type.JUICE),
	HAWBERRY_TEA(FruitType.HAWBERRY, Type.TEA),
	ORANGE_JUICE(FruitType.ORANGE, Type.SWEETENED),
	LEMON_JUICE(FruitType.LEMON, Type.SWEETENED),
	PEAR_JUICE(FruitType.PEAR, Type.SWEETENED),
	MANGO_TEA(FruitType.MANGO, Type.TEA),
	PEACH_TEA(FruitType.PEACH, Type.TEA),
	LYCHEE_CHERRY_TEA(FruitType.LYCHEE, Type.CHERRY),
	MANGOSTEEN_TEA(FruitType.MANGOSTEEN, Type.TEA),
	BAYBERRY_SOUP(FruitType.BAYBERRY, Type.SOUP),
	KIWI_JUICE(FruitType.KIWI, Type.JUICE),
	BELLINI_COCKTAIL(FruitType.PEACH, Type.ICED,
			new EffectEntry(FDEffects.HEAL_AURA, 200),
			new EffectEntry(MobEffects.CONFUSION, 100)),

	;


	private final String name;
	public final FruitType fruit;
	public final Type type;
	public final ItemEntry<Item> item;
	public final EffectEntry[] effs;

	FDJuice(FruitType fruit, Type type, @Nullable String str, EffectEntry... effs) {
		this.fruit = fruit;
		this.type = type;
		FoodType food = FoodType.JUICE;
		this.name = name().toLowerCase(Locale.ROOT);
		this.item = FruitsDelight.REGISTRATE.item(name, p -> food.build(p, this))
				.transform(b -> food.model(b, 0, fruit))
				.lang(str != null ? str : FDItems.toEnglishName(name))
				.tag(food.tags)
				.register();
		this.effs = effs;
	}

	FDJuice(FruitType fruit, Type type, EffectEntry... effs) {
		this(fruit, type, null, effs);
	}

	public static void register() {

	}

	public Ingredient getFruitTag() {
		return fruit.getFruitTag();
	}

	public Item getFruit() {
		return fruit.getFruit();
	}

	public FDFoodItem get() {
		return (FDFoodItem) item.get();
	}

	public FruitFluid getFluid() {
		return FDFluids.JUICE[ordinal()].get();
	}

	@Override
	public FruitType fruit() {
		return fruit;
	}

	@Override
	public FoodType getType() {
		return FoodType.JUICE;
	}

	@Override
	public EffectEntry[] getEffects() {
		return effs;
	}

	public void recipe(RegistrateRecipeProvider pvd) {
		int count = fruit.jamCost;
		if (type.category.cook) {
			var e = CookingPotRecipeBuilder.cookingPotRecipe(item, 1, 200, 0.1f, Items.GLASS_BOTTLE);
			type.list.get().forEach(e::addIngredient);
			e.addIngredient(getFruitTag(), count);
			e.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS);
			e.build(pvd);
		} else {
			var e = RecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, item)::unlockedBy, getFruit());
			if (type.category.waterCraft) {
				e.requires(PotionIngredient.of(Potions.WATER));
			}
			type.list.get().forEach(e::requires);
			e.requires(getFruitTag(), count);
			e.save(pvd);
		}
	}

	/**
	 * For DataGen only
	 * */
	public enum Type {
		JUICE(Category.PLAIN, () -> List.of(Ingredient.of(Items.GLASS_BOTTLE))),
		SWEETENED(Category.RINSE, () -> List.of((Ingredient.of(Items.SUGAR)))),
		BOILED(Category.BOIL, List::of),
		SOUP(Category.SOUP, List::of),
		TEA(Category.BOIL, () -> List.of(Ingredient.of(Items.SUGAR), Ingredient.of(ItemTags.LEAVES))),
		ICED(Category.COLD_COOK, () -> List.of(Ingredient.of(Items.SUGAR), Ingredient.of(Items.ICE))),
		CHERRY(Category.BOIL, () -> List.of(Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHERRY_LEAVES)));

		public final Category category;
		public final Supplier<List<Ingredient>> list;

		Type(Category category, Supplier<List<Ingredient>> list) {
			this.category = category;
			this.list = list;
		}

	}

	/**
	 * For DataGen only
	 * */
	public enum Category {
		PLAIN(false, false, false, false),
		RINSE(false, false, true, true),
		BOIL(true, true, false, true),
		SOUP(true, true, true, true),
		COLD_COOK(true, false, false, false);

		public final boolean cook, heated, waterCraft, waterMix;

		Category(boolean cook, boolean heated, boolean waterCraft, boolean waterMix) {
			this.cook = cook;
			this.heated = heated;
			this.waterCraft = waterCraft;
			this.waterMix = waterMix;
		}

	}

}
