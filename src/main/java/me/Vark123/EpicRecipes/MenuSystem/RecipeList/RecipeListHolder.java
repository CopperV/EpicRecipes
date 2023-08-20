package me.Vark123.EpicRecipes.MenuSystem.RecipeList;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;

@Data
@AllArgsConstructor
public class RecipeListHolder implements InventoryHolder {

	private RecipeGroup presentGroup;
	private List<ItemStack> visibleItems;
	private boolean recipeView;
	private int page;

	private RecipeListHolder father;
	
	@Override
	public Inventory getInventory() {
		return null;
	}

}
