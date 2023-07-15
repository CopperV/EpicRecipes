package me.Vark123.EpicRecipes;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.Vark123.EpicRecipes.FileSystem.FileManager;

@Getter
public final class Main extends JavaPlugin {

	private static Main inst;
	
	private String prefix;

	@Override
	public void onEnable() {
		inst = this;
		prefix = "§8[§x§3§6§f§b§6§1§lP§x§5§2§f§4§5§5§lR§x§6§f§e§d§4§a§lZ§x§8§b§e§6§3§e§lE§x§a§8§d§e§3§3§lP§x§c§4§d§7§2§7§lI§x§e§1§d§0§1§c§lS§x§f§d§c§9§1§0§lY§8]";
		
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
