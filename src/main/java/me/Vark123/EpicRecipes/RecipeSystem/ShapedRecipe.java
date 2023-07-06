package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lumine.mythic.bukkit.MythicBukkit;
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
		this(recipe, recipeId, group, mmResult, true);
	}

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult, 
			boolean visible) {
		this(recipe, recipeId, group, mmResult, visible, 1);
	}

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult, 
			boolean visible,
			int amount) {
		super(recipeId, group, mmResult, visible, amount);
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

	@Override
	public Map<Integer, ItemStack> getRecipeView() {
		Map<Integer, ItemStack> view = new HashMap<>();
		recipe.forEach((i, str) -> {
			ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(str);
			view.put(i, it);
		});
		return view;
	}

}
