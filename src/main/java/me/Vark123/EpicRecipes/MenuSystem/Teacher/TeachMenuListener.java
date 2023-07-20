package me.Vark123.EpicRecipes.MenuSystem.Teacher;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;
import me.Vark123.EpicRecipes.TeacherSystem.TeachManager;

public class TeachMenuListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.isCancelled())
			return;
		Inventory inv = e.getView().getTopInventory();
		if(inv == null)
			return;
		InventoryHolder holder = inv.getHolder();
		if(holder == null
				|| !(holder instanceof TeacherHolder))
			return;
		e.setCancelled(true);
		ItemStack it = e.getCurrentItem();
		if(it == null)
			return;
		
		Player p = (Player) e.getWhoClicked();
		TeacherHolder teachHolder = (TeacherHolder) holder;
		if(it.equals(TeachMenuManager.get().getNext())) {
			TeachMenuManager.get().openMenu(p, inv, teachHolder.getPage()+1);
			return;
		}
		if(it.equals(TeachMenuManager.get().getPrevious())) {
			TeachMenuManager.get().openMenu(p, inv, teachHolder.getPage()-1);
			return;
		}
		
		NBTItem nbt = new NBTItem(it);
		if(nbt.hasTag("recipe_id")) {
			String strRecipe = nbt.getString("recipe_id");
			ARecipe recipe = RecipeManager.get().getRecipeContainer().get(strRecipe);
			if(recipe == null)
				return;
			TeachManager.get().getRecipesToLearn()
				.stream()
				.filter(tmp -> tmp.getRecipeToLearn().equals(recipe))
				.findAny().ifPresent(learnRecipe -> {
					TeachManager.get().learnRecipe(p, learnRecipe);
					p.closeInventory();
				});
		}
	}
	
}
