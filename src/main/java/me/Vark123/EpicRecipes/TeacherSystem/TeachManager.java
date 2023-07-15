package me.Vark123.EpicRecipes.TeacherSystem;

import lombok.Getter;

@Getter
public final class TeachManager {

	private static final TeachManager inst = new TeachManager();
	
	private TeachManager() {
		
	}
	
	public static final TeachManager get() {
		return inst;
	}
	
}
