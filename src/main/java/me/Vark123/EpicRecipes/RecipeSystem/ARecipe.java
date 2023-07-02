package me.Vark123.EpicRecipes.RecipeSystem;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class ARecipe {

	private String recipeId;
	private RecipeGroup group;
	protected String mmResult;
	protected int amount;
	
	public ARecipe(
			@NonNull String recipeId,
			@NonNull RecipeGroup group,
			@NonNull String mmResult) {
		this(recipeId, group, mmResult, 1);
	}
	public ARecipe(
			@NonNull String recipeId,
			@NonNull RecipeGroup group,
			@NonNull String mmResult,
			int amount) {
		this.recipeId = recipeId;
		this.group = group;
		this.mmResult = mmResult;
		this.amount = amount;
	}
	
	public abstract boolean canCraft(Inventory inv);
	public abstract void craft(Inventory inv);
	public abstract ItemStack getCraftResult();
	
}
