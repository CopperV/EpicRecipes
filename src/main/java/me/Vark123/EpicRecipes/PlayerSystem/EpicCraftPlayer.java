package me.Vark123.EpicRecipes.PlayerSystem;

import java.util.Collection;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.Vark123.EpicRecipes.ProfessionSystem.AProfession;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
@AllArgsConstructor
public class EpicCraftPlayer {

	private Player player;
	private Collection<ARecipe> learnedRecipes;
	private Set<AProfession> professions;
	
}
