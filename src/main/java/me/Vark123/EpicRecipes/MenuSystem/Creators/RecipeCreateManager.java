package me.Vark123.EpicRecipes.MenuSystem.Creators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRecipes.CraftingSystem.CraftingManager;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroupManager;

@Getter
public final class RecipeCreateManager {

	private static final RecipeCreateManager inst = new RecipeCreateManager();

	private final ItemStack empty;
	private final ItemStack create;
	
	@Getter(value = AccessLevel.NONE)
	private final int CRAFTING_SIZE = 9*5;
	private final List<Integer> freeSlots;
	
	private RecipeCreateManager() {
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		create = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);{
			ItemMeta im = create.getItemMeta();
			im.setDisplayName("§6§lSTWORZ");
			create.setItemMeta(im);
		}
		freeSlots = new ArrayList<>(
				Arrays.asList(
						0,1,2,3,4, 
						9,10,11,12,13,
						18,19,20,21,22,
						27,28,29,30,31,
						36,37,38,39,40,
						24
				));
	}
	
	public static final RecipeCreateManager get() {
		return inst;
	}
	
	public void openMenu(Player p, String type, String groupId, String id) {
		
		RecipeGroup group = RecipeGroupManager.get().getGroupContainer().stream()
				.filter(gr -> gr.getGroupId().equals(groupId))
				.findFirst().get();
		
		RecipeCreateHolder holder = new RecipeCreateHolder(type, group, id);
		Inventory inv = Bukkit.createInventory(holder, 5*9, "§6§lKreator przepisow");

		for(int i = 0; i < CRAFTING_SIZE; ++i) {
			if(CraftingManager.get().getFreeSlots().contains(i))
				continue;
			inv.setItem(i, empty);
		}
		inv.setItem(24, new ItemStack(Material.AIR));
		inv.setItem(26, create);
		
		p.openInventory(inv);
	}
	
}
