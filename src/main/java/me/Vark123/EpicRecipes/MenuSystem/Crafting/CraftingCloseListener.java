package me.Vark123.EpicRecipes.MenuSystem.Crafting;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRecipes.CraftingSystem.CraftingManager;

public class CraftingCloseListener implements Listener {

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Inventory inv = e.getView().getTopInventory();
		if(inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if(holder == null
				|| !(holder instanceof CraftingHolder))
			return;
		Player p = (Player) e.getPlayer();
		List<Integer> freeSlots = CraftingManager.get().getFreeSlots();
		
		freeSlots.stream().filter(i -> {
			return (inv.getItem(i) != null 
					&& !inv.getItem(i).getType().equals(Material.AIR));
		}).map(i -> {
			return inv.getItem(i);
		}).forEach(it -> {
			Utils.dropItemStack(p, it);
		});
	}
	
}
