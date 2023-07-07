package me.Vark123.EpicRecipes.MenuSystem.Crafting;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRecipes.CraftingSystem.CraftingManager;

@Getter
public class CraftingInvManager {

	private static final CraftingInvManager inst = new CraftingInvManager();

	private final ItemStack empty;
	private final ItemStack redCreate;
	private final ItemStack greenCreate;
	
	@Getter(value = AccessLevel.NONE)
	private final int CRAFTING_SIZE = 9*5;
	
	private CraftingInvManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		redCreate = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);{
			ItemMeta im = redCreate.getItemMeta();
			im.setDisplayName("§6§lSTWORZ");
			redCreate.setItemMeta(im);
		}
		greenCreate = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);{
			ItemMeta im = greenCreate.getItemMeta();
			im.setDisplayName("§6§lSTWORZ");
			greenCreate.setItemMeta(im);
		}
	}
	
	public static final CraftingInvManager get() {
		return inst;
	}
	
	public void openMenu(Player p) {
		CraftingHolder holder = new CraftingHolder();
		Inventory inv = Bukkit.createInventory(holder, CRAFTING_SIZE, "§2§lStol rzemieslniczy");
		
		for(int i = 0; i < CRAFTING_SIZE; ++i) {
			if(CraftingManager.get().getFreeSlots().contains(i))
				continue;
			inv.setItem(i, empty);
		}
		inv.setItem(24, new ItemStack(Material.AIR));
		inv.setItem(26, redCreate);
		
		p.openInventory(inv);
	}
	
}
