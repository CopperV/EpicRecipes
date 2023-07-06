package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public final class RecipeManager {

	private static final RecipeManager inst = new RecipeManager();
	
	private final Map<String, ARecipe> recipeContainer;
	
	private RecipeManager() {
		recipeContainer = new LinkedHashMap<>();
	}
	
	public static final RecipeManager get() {
		return inst;
	}
	
	public void addRecipe(ARecipe recipe) {
		recipeContainer.put(recipe.getRecipeId(), recipe);
		RecipeGroup group = recipe.getGroup();
		group.addRecipe(recipe);
	}
	
}
