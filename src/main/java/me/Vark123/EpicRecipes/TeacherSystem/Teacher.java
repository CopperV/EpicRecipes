package me.Vark123.EpicRecipes.TeacherSystem;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Teacher {
	
	private String id;
	private Collection<LearningRecipe> recipesToTeach;

}
