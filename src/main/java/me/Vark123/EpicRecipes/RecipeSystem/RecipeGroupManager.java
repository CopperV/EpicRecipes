package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;

@Getter
public final class RecipeGroupManager {

	private static final RecipeGroupManager inst = new RecipeGroupManager();
	
	private final Set<RecipeGroup> groupContainer;
	
	private RecipeGroupManager() {
		groupContainer = new LinkedHashSet<>();
	}
	
	public static final RecipeGroupManager get() {
		return inst;
	}

	public RecipeGroup createGroup(String groupId, String display) {
		return createGroup(groupId, display, false);
	}
	
	public RecipeGroup createGroup(String groupId, String display, boolean groupVisible) {
		return createGroup(null, groupId, display, groupVisible);
	}
	
	public RecipeGroup createGroup(RecipeGroup father, String groupId, String display, boolean groupVisible) {
		RecipeGroup group = new RecipeGroup(father, groupId, display, groupVisible);
		return createGroup(group);
	}
	
	public RecipeGroup createGroup(RecipeGroup group) {
		groupContainer.add(group);
		RecipeGroup father = group.getFather();
		if(father != null
				&& groupContainer.contains(father)) {
			father.addSon(group);
		}
		return group;
	}
	
}
