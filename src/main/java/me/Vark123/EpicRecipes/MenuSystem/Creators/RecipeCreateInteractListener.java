package me.Vark123.EpicRecipes.MenuSystem.Creators;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.FileSystem.FileManager;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;
import me.Vark123.EpicRecipes.RecipeSystem.ShapedRecipe;
import me.Vark123.EpicRecipes.RecipeSystem.ShapelessRecipe;
import me.Vark123.EpicRecipes.Utils.Utils;

public class RecipeCreateInteractListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		
		if(e.getClickedInventory() == null 
				|| !e.getClickedInventory().getType().equals(InventoryType.CHEST)) return;

		Inventory inv = e.getView().getTopInventory();
		if(inv == null)
			return;
		if(e.getClickedInventory() == null || !e.getClickedInventory().equals(inv))
			return;
		if(!(inv.getHolder() instanceof RecipeCreateHolder))
			return;
		
		Player p = (Player) e.getWhoClicked();
		ItemStack it = e.getCurrentItem();
		if(it == null)
			return;
		if(it.equals(RecipeCreateManager.get().getEmpty())) {
			e.setCancelled(true);
			return;
		}
		if(!it.equals(RecipeCreateManager.get().getCreate()))
			return;
		
		e.setCancelled(true);
		RecipeCreateHolder holder = (RecipeCreateHolder) inv.getHolder();

		if(inv.getItem(24) == null)
			return;
		ItemStack result = inv.getItem(24);
		NBTItem nbtResult = new NBTItem(result);
		if(!nbtResult.hasTag("MYTHIC_TYPE")) {
			p.closeInventory();
			return;
		}
		String MMResult = nbtResult.getString("MYTHIC_TYPE");
		
		Map<Integer, ItemStack> recipe = new LinkedHashMap<>();
		for(int i = 0; i < 25; ++i) {
			int invSlot = Utils.mapCraftToInv(i);
			ItemStack item = inv.getItem(invSlot);
			if(item == null || item.getType().equals(Material.AIR))
				continue;
			NBTItem nbt = new NBTItem(item);
			if(!nbt.hasTag("MYTHIC_TYPE"))
				continue;
			recipe.put(i, item);
		}

		if(recipe.isEmpty()) {
			p.closeInventory();
			return;
		}
		
		if(holder.getCraftType().equalsIgnoreCase("shaped")) {
			Map<Integer, String> shapedRecipe = new LinkedHashMap<>();
			recipe.forEach((slot, item) -> {
				String mmId = new NBTItem(item).getString("MYTHIC_TYPE");
				shapedRecipe.put(slot, mmId);
			});
			ShapedRecipe rec = new ShapedRecipe(shapedRecipe, holder.getId(), holder.getGroup(), MMResult);
			FileManager.get().saveShapedRecipe(rec);
			RecipeManager.get().addRecipe(rec);
		} else if(holder.getCraftType().equalsIgnoreCase("shapeless")) {
			Map<String, Integer> shapelessRecipe = new LinkedHashMap<>();
			recipe.values().forEach(item -> {
				String mmId = new NBTItem(item).getString("MYTHIC_TYPE");
				int amount = shapelessRecipe.getOrDefault(mmId, 0);
				shapelessRecipe.put(mmId, item.getAmount() + amount);
			});
			ShapelessRecipe rec = new ShapelessRecipe(shapelessRecipe, holder.getId(), holder.getGroup(), MMResult);
			FileManager.get().saveShapelessRecipe(rec);
			RecipeManager.get().addRecipe(rec);
		}
		
		inv.setItem(24, null);
		recipe.keySet().forEach(slot -> {
			inv.setItem(Utils.mapCraftToInv(slot), null);
		});

		p.sendMessage(Main.inst().getPrefix()+" §a§lStworzyles nowy przepis :)");
		p.closeInventory();
		
	}
	
}
