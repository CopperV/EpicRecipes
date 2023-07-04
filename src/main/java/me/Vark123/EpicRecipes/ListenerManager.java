package me.Vark123.EpicRecipes;

import org.bukkit.Bukkit;

import me.Vark123.EpicRecipes.PlayerSystem.Listeners.PlayerJoinListener;
import me.Vark123.EpicRecipes.PlayerSystem.Listeners.PlayerQuitListener;

public final class ListenerManager {

	private ListenerManager() {}
	
	public static void registerListeners() {
		Main inst = Main.inst();
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), inst);
		Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(), inst);
	}
	
}
