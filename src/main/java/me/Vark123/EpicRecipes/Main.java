package me.Vark123.EpicRecipes;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.Vark123.EpicRecipes.FileSystem.FileManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

@Getter
public final class Main extends JavaPlugin {

	private static Main inst;

	public static Economy eco;
	public static Permission perm;
	
	private String prefix;

	@Override
	public void onEnable() {
		inst = this;
		prefix = "§8[§x§3§6§f§b§6§1§lP§x§5§2§f§4§5§5§lR§x§6§f§e§d§4§a§lZ§x§8§b§e§6§3§e§lE§x§a§8§d§e§3§3§lP§x§c§4§d§7§2§7§lI§x§e§1§d§0§1§c§lS§x§f§d§c§9§1§0§lY§8]";
		
		ListenerManager.registerListeners();
		CommandManager.setExecutors();
		
		FileManager.get().init();
		
		checkEco();
		checkPerm();
		
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	public final static Main inst() {
		return inst;
	}
	
	private boolean checkEco() {
		RegisteredServiceProvider<Economy> ecop = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if(ecop == null) {
			return false;
		}
		eco = ecop.getProvider();
		if(eco == null) {
			return false;
		}
		return true;
	}
	
	private boolean checkPerm() {
		RegisteredServiceProvider<Permission> ecop = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
		if(ecop == null) {
			return false;
		}
		perm = ecop.getProvider();
		if(perm == null) {
			return false;
		}
		return true;
	}
	
}
