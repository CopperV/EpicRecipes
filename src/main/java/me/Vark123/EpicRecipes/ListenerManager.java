package me.Vark123.EpicRecipes;

import org.bukkit.Bukkit;

import me.Vark123.EpicRecipes.CraftingSystem.CraftBlockClickListener;
import me.Vark123.EpicRecipes.MenuSystem.Crafting.CraftingClickListener;
import me.Vark123.EpicRecipes.MenuSystem.Crafting.CraftingCloseListener;
import me.Vark123.EpicRecipes.MenuSystem.RecipeList.RecipeListMenuListener;
import me.Vark123.EpicRecipes.MenuSystem.Teacher.TeachMenuListener;
import me.Vark123.EpicRecipes.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicRecipes.PlayerSystem.Listeners.PlayerQuitListener;

public final class ListenerManager {

	private ListenerManager() {}
	
	public static void registerListeners() {
		Main inst = Main.inst();
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);

		Bukkit.getPluginManager().registerEvents(new CraftBlockClickListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CraftingCloseListener(), inst);
		Bukkit.getPluginManager().registerEvents(new CraftingClickListener(), inst);

		Bukkit.getPluginManager().registerEvents(new RecipeListMenuListener(), inst);
		Bukkit.getPluginManager().registerEvents(new TeachMenuListener(), inst);
	}
	
}
