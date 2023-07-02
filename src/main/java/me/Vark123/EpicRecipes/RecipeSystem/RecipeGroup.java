package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class RecipeGroup {

	private RecipeGroup father;
	private Set<RecipeGroup> sons;
	
	private Set<ARecipe> recipes;
	
	private String groupId;
	private String display;
	private boolean groupVisible;
	
	public RecipeGroup(@NonNull String groupId, @NonNull String display) {
		this(groupId, display, true);
	}
	
	public RecipeGroup(@NonNull String groupId, @NonNull String display, boolean groupVisible) {
		this(null, groupId, display, groupVisible);
	}
	
	public RecipeGroup(RecipeGroup father, @NonNull String groupId, @NonNull String display, boolean groupVisible) {
		this.father = father;
		this.groupId = groupId;
		this.display = display;
		this.groupVisible = groupVisible;
		
		sons = new HashSet<>();
		recipes = new HashSet<>();
	}
	
	public void addSon(RecipeGroup son) {
		sons.add(son);
	}
	public void addSons(Collection<? extends RecipeGroup> sons) {
		this.sons.addAll(sons);
	}
	
	public void addRecipe(ARecipe recipe) {
		recipes.add(recipe);
	}
	public void addRecipes(Collection<? extends ARecipe> recipes) {
		this.recipes.addAll(recipes);
	}
	
}
