package me.Vark123.EpicRecipes.CraftingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
public final class CraftingManager {

	private static final CraftingManager inst = new CraftingManager();
	
	private final Collection<CraftBlock> craftingBlocks;
	private final List<Integer> freeSlots;
	
	private CraftingManager() {
		craftingBlocks = new HashSet<>();
		freeSlots = new ArrayList<>(
				Arrays.asList(
						0,1,2,3,4, 
						9,10,11,12,13,
						18,19,20,21,22,
						27,28,29,30,31,
						36,37,38,39,40,
						24));
	}
	
	public static final CraftingManager get() {
		return inst;
	}
	
	public void registerCraftBlock(String craftRegion) {
		registerCraftBlock(craftRegion, Material.CRAFTING_TABLE);
	}
	
	public void registerCraftBlock(String craftRegion, Material block) {
		CraftBlock craftBlock = new CraftBlock(craftRegion, block);
		registerCraftBlock(craftBlock);
	}
	
	public void registerCraftBlock(CraftBlock block) {
		craftingBlocks.add(block);
	}
	
	public Optional<ARecipe> getCraftRecipe(Player p, Inventory craftInventory) {
		return Optional.empty();
	}
	
}
