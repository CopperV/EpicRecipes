package me.Vark123.EpicRecipes.CraftingSystem;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;

import me.Vark123.EpicRecipes.MenuSystem.Crafting.CraftingInvManager;

public class CraftBlockClickListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.useInteractedBlock().equals(Result.DENY))
			return;
		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		Block b = e.getClickedBlock();
		if(b == null)
			return;
		Material m = b.getType();
		List<String> regions = WorldGuard.getInstance().getPlatform()
				.getRegionContainer().createQuery()
				.getApplicableRegions(BukkitAdapter.adapt(b.getLocation()))
				.getRegions().stream()
				.map(region -> {
					return region.getId();
				}).toList();
		
		Player p = e.getPlayer();
		CraftingManager.get().getCraftingBlocks().stream().filter(craftBlock -> {
			if(!regions.contains(craftBlock.getCraftRegion()))
				return false;
			return craftBlock.getBlock().equals(m);
		}).findAny().ifPresent(craftBlock -> {
			e.setUseInteractedBlock(Result.DENY);
			e.setUseItemInHand(Result.DENY);
			e.setCancelled(true);
			CraftingInvManager.get().openMenu(p);
		});
	}

}
