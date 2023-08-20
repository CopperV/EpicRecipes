package me.Vark123.EpicRecipes.MenuSystem.Crafting;

import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.CraftingSystem.CraftingManager;

public class CraftingClickListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.isCancelled())
			return;
		Inventory inv = e.getView().getTopInventory();
		if (inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if (holder == null || !(holder instanceof CraftingHolder))
			return;

		Player p = (Player) e.getWhoClicked();
		int checkSlot;
		Inventory checkInv = e.getClickedInventory();
		if (checkInv == null)
			return;
		if (checkInv.equals(e.getView().getBottomInventory())) {
			if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
				checkSlot = e.getInventory().firstEmpty();
			} else {
				return;
			}
		} else if (checkInv.equals(e.getView().getTopInventory())) {
			checkSlot = e.getSlot();
		} else {
			return;
		}

		if (!CraftingManager.get().getFreeSlots().contains(checkSlot)) {
			e.setCancelled(true);
			if (checkSlot == 26 && inv.getItem(checkSlot).equals(CraftingInvManager.get().getGreenCreate())) {
				CraftingManager.get().craft(p, inv);
				return;
			}
			return;
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				CraftingManager.get().tryMatch(p, inv);
			}
		}.runTaskLaterAsynchronously(Main.inst(), 1);

	}
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if(e.isCancelled())
			return;
		Inventory inv = e.getView().getTopInventory();
		if (inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if (holder == null || !(holder instanceof CraftingHolder))
			return;
		
		List<Integer> freeSlots = CraftingManager.get().getFreeSlots();
		MutableBoolean cancel = new MutableBoolean();
		e.getRawSlots().stream().filter(i -> {
			return !freeSlots.contains(i);
		}).findAny().ifPresent(i -> {
			cancel.setTrue();
		});
		if(cancel.isTrue()) {
			e.setCancelled(true);
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		new BukkitRunnable() {
			@Override
			public void run() {
				CraftingManager.get().tryMatch(p, inv);
			}
		}.runTaskLaterAsynchronously(Main.inst(), 1);
	}

}
