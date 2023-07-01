package me.Vark123.EpicRecipes;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	private static Main inst;

	@Override
	public void onEnable() {
		inst = this;
		
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
