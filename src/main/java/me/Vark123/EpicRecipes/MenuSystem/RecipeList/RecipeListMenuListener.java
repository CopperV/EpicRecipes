package me.Vark123.EpicRecipes.MenuSystem.RecipeList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroupManager;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;

public class RecipeListMenuListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		Inventory inv = e.getView().getTopInventory();
		if(inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if(holder == null
				|| !(holder instanceof RecipeListHolder))
			return;
		e.setCancelled(true);
		ItemStack it = e.getCurrentItem();
		if(it == null)
			return;
		
		Player p = (Player) e.getWhoClicked();
		RecipeListHolder recipeHolder = (RecipeListHolder) holder;
		if(it.equals(RecipeListManager.get().getNext())) {
			RecipeListManager.get().openMenu(p, inv, recipeHolder.getPage()+1);
			return;
		}
		if(it.equals(RecipeListManager.get().getPrevious())) {
			RecipeListManager.get().openMenu(p, inv, recipeHolder.getPage()-1);
			return;
		}
		if(it.equals(RecipeListManager.get().getBack())) {
			if(recipeHolder.isRecipeView()) {
				recipeHolder.setRecipeView(false);
				RecipeListManager.get().openMenu(p, inv, recipeHolder.getPage());
			} else {
				RecipeGroup father = recipeHolder.getPresentGroup().getFather();
				RecipeListManager.get().openMenu(p, inv, father);
			}
			return;
		}
		
		NBTItem nbt = new NBTItem(it);
		if(nbt.hasTag("group")) {
			String strGroup = nbt.getString("group");
			RecipeGroupManager.get().getGroupContainer().stream()
				.filter(group -> {
					return group.getGroupId().equals(strGroup);
				}).findAny().ifPresent(group -> {
					RecipeListManager.get().openMenu(p, inv, group);
				});
			return;
		}
		if(nbt.hasTag("recipe_id")) {
			String strRecipe = nbt.getString("recipe_id");
			RecipeManager.get().getRecipeContainer().values().stream()
				.filter(recipe -> {
					return recipe.getRecipeId().equals(strRecipe);
				}).findAny().ifPresent(recipe -> {
					RecipeListManager.get().openMenu(p, inv, recipe);
				});
			return;
		}
	}

}
