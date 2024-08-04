package dev.xkmc.fruitsdelight.content.item;

import dev.xkmc.fruitsdelight.init.food.IFDFood;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public interface IFDFoodItem {

	@Contract
	IFDFood food();

}
