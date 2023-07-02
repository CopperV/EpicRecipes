package me.Vark123.EpicRecipes;

import org.bukkit.plugin.java.JavaPlugin;

import me.Vark123.EpicRecipes.FileSystem.FileManager;

public final class Main extends JavaPlugin {

	private static Main inst;

	@Override
	public void onEnable() {
		inst = this;
		
		ListenerManager.registerListeners();
		CommandManager.setExecutors();
		
		FileManager.get().init();
		
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	public final static Main inst() {
		return inst;
	}
	
}
