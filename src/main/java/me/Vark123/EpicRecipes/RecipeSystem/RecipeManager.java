package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public final class RecipeManager {

	private static final RecipeManager inst = new RecipeManager();
	
	private final Set<ARecipe> recipeContainer;
	
	private RecipeManager() {
		recipeContainer = new HashSet<>();
	}
	
	public static final RecipeManager get() {
		return inst;
	}
	
	public void addRecipe(ARecipe recipe) {
		recipeContainer.add(recipe);
		RecipeGroup group = recipe.getGroup();
		group.addRecipe(recipe);
	}
	
}
