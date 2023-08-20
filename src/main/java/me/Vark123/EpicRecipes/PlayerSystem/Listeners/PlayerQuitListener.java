package me.Vark123.EpicRecipes.PlayerSystem.Listeners;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Vark123.EpicRecipes.FileSystem.FileManager;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;

public class PlayerQuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		cleanPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		cleanPlayer(e.getPlayer());
	}
	
	private void cleanPlayer(Player p) {
		Optional<EpicCraftPlayer> oCraftPlayer = PlayerManager.get().getPlayer(p);
		if(oCraftPlayer.isEmpty())
			return;
		PlayerManager.get().removePlayer(p);
		EpicCraftPlayer craftPlayer = oCraftPlayer.get();
		FileManager.get().savePlayer(craftPlayer);
	}
	
}
