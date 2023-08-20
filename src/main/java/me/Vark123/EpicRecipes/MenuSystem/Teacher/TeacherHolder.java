package me.Vark123.EpicRecipes.MenuSystem.Teacher;

import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRecipes.TeacherSystem.LearningRecipe;

@Getter
@AllArgsConstructor
public class TeacherHolder implements InventoryHolder {

	@Setter
	private int page;
	private List<LearningRecipe> recipes;
	
	@Override
	public Inventory getInventory() {
		return null;
	}
	
}
