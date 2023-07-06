package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
public abstract class ARecipe {

	private String recipeId;
	private RecipeGroup group;
	protected String mmResult;
	protected int amount;
	private boolean recipeVisible;
	@Setter
	private boolean common;
	
	public ARecipe(
			@NonNull String recipeId,
			@NonNull RecipeGroup group,
			@NonNull String mmResult) {
		this(recipeId, group, mmResult, true);
	}
	public ARecipe(
			@NonNull String recipeId,
			@NonNull RecipeGroup group,
			@NonNull String mmResult,
			boolean visible) {
		this(recipeId, group, mmResult, visible, 1);
	}
	public ARecipe(
			@NonNull String recipeId,
			@NonNull RecipeGroup group,
			@NonNull String mmResult,
			boolean visible,
			int amount) {
		this.recipeId = recipeId;
		this.group = group;
		this.mmResult = mmResult;
		this.amount = amount;
		this.recipeVisible = visible;
		this.common = false;
	}
	
	public abstract boolean canCraft(Inventory inv);
	public abstract void craft(Inventory inv);
	public abstract ItemStack getCraftResult();
	public abstract Map<Integer, ItemStack> getRecipeView();
	
}
