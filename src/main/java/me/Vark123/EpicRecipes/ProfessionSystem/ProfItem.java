package me.Vark123.EpicRecipes.ProfessionSystem;

import lombok.Getter;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Alchemia;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Jubilerstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Kowalstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Luczarstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Platnerstwo;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
public class ProfItem {

	private Class<?> professionClass;
	private ARecipe recipe;
	private int points;
	private String MMResult;
	
	public ProfItem(String profession, ARecipe recipe, int points, String MMResult) {
		this.recipe = recipe;
		this.points = points;
		this.MMResult = MMResult;
		switch(profession.toLowerCase()) {
			case "kowalstwo":
				this.professionClass = Kowalstwo.class;
				break;
			case "luczarstwo":
				this.professionClass = Luczarstwo.class;
				break;
			case "platnerstwo":
				this.professionClass = Platnerstwo.class;
				break;
			case "alchemia":
				this.professionClass = Alchemia.class;
				break;
			case "jubilerstwo":
				this.professionClass = Jubilerstwo.class;
				break;
		}
	}
	
	
}
