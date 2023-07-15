package me.Vark123.EpicRecipes;

import org.bukkit.Bukkit;

import me.Vark123.EpicRecipes.CraftingSystem.CraftingCommand;
import me.Vark123.EpicRecipes.MenuSystem.RecipeList.RecipeListCommand;
import me.Vark123.EpicRecipes.ProfessionSystem.ProfessionCommand;

public final class CommandManager {

	private CommandManager() {}
	
	public static void setExecutors() {
		Bukkit.getPluginCommand("recipes").setExecutor(new RecipeListCommand());
		Bukkit.getPluginCommand("crafting").setExecutor(new CraftingCommand());
		Bukkit.getPluginCommand("profession").setExecutor(new ProfessionCommand());
	}
	
}
