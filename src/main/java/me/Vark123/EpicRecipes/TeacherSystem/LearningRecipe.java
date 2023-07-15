package me.Vark123.EpicRecipes.TeacherSystem;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
@AllArgsConstructor
public class LearningRecipe {

	private ARecipe recipeToLearn;
	private Collection<LearnCost> costs;
	
}
