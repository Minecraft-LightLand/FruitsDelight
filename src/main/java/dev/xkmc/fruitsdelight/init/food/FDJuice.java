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
import dev.xkmc.l2library.serial.ingredients.PotionIngredient;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.List;
import java.util.Locale;

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
			new EffectEntry(FDEffects.HEAL_AURA::get, 200),
			new EffectEntry(() -> MobEffects.CONFUSION, 100)),

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

	public Item getFruit() {
		return fruit.fruit.get();
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
		int count = fruit.jellyCost;
		if (type.category.cook) {
			var e = CookingPotRecipeBuilder.cookingPotRecipe(item, 1, 200, 0.1f, Items.GLASS_BOTTLE);
			type.list.forEach(e::addIngredient);
			e.addIngredient(getFruit(), count);
			e.build(pvd);
		} else {
			var e = RecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, item)::unlockedBy, getFruit());
			if (type.category.waterCraft) {
				e.requires(new PotionIngredient(Potions.WATER));
			}
			type.list.forEach(e::requires);
			e.requires(getFruit(), count);
			e.save(pvd);
		}
	}

	public enum Type {
		JUICE(Category.PLAIN, Ingredient.of(Items.GLASS_BOTTLE)),
		SWEETENED(Category.RINSE, Ingredient.of(Items.SUGAR)),
		BOILED(Category.BOIL),
		SOUP(Category.SOUP),
		TEA(Category.BOIL, Ingredient.of(Items.SUGAR), Ingredient.of(ItemTags.LEAVES)),
		ICED(Category.COLD_COOK, Ingredient.of(Items.SUGAR), Ingredient.of(Items.ICE)),
		CHERRY(Category.BOIL, Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHERRY_LEAVES));

		public final Category category;
		public final List<Ingredient> list;

		Type(Category category, Ingredient... list) {
			this.category = category;
			this.list = List.of(list);
		}


	}

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
