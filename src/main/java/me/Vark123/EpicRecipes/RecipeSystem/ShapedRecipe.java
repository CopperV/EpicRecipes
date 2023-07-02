package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class ShapedRecipe extends ARecipe {
	
	private Map<Integer, String> recipe;

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult) {
		this(recipe, recipeId, group, mmResult, 1);
	}

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult, 
			int amount) {
		super(recipeId, group, mmResult, amount);
		this.recipe = recipe;
	}

	@Override
	public boolean canCraft(Inventory inv) {
		return false;
	}

	@Override
	public void craft(Inventory inv) {
		
	}

	@Override
	public ItemStack getCraftResult() {
		return null;
	}

}
