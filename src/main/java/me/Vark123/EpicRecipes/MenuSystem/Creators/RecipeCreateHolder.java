package me.Vark123.EpicRecipes.MenuSystem.Creators;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;

@Data
@AllArgsConstructor
public class RecipeCreateHolder implements InventoryHolder {

	private String craftType;
	private RecipeGroup group;
	private String id;
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
}
