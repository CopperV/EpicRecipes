package me.Vark123.EpicRecipes.ProfessionSystem;

import java.util.Collection;
import java.util.HashSet;

import lombok.Getter;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
public final class ProfessionManager {

	private static final ProfessionManager inst = new ProfessionManager();
	
	private final Collection<ProfItem> container;
	
	private ProfessionManager() {
		container = new HashSet<>();
	}
	
	public static final ProfessionManager get() {
		return inst;
	}
	
	public ProfItem addProfItem(String profession, ARecipe recipe, int points, String MMResult) {
		ProfItem item = new ProfItem(profession, recipe, points, MMResult);
		addProfItem(item);
		return item;
	}
	
	public void addProfItem(ProfItem item) {
		container.add(item);
	}
	
}
