package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public final class RecipeGroupManager {

	private static final RecipeGroupManager inst = new RecipeGroupManager();
	
	private final Set<RecipeGroup> groupContainer;
	
	private RecipeGroupManager() {
		groupContainer = new HashSet<>();
	}
	
	public static final RecipeGroupManager get() {
		return inst;
	}

	public void createGroup(String groupId, String display) {
		createGroup(groupId, display, false);
	}
	
	public void createGroup(String groupId, String display, boolean groupVisible) {
		createGroup(null, groupId, display, groupVisible);
	}
	
	public void createGroup(RecipeGroup father, String groupId, String display, boolean groupVisible) {
		RecipeGroup group = new RecipeGroup(father, groupId, display, groupVisible);
		createGroup(group);
	}
	
	public void createGroup(RecipeGroup group) {
		groupContainer.add(group);
		RecipeGroup father = group.getFather();
		if(father != null
				&& groupContainer.contains(father)) {
			father.addSon(group);
		}
	}
	
}
