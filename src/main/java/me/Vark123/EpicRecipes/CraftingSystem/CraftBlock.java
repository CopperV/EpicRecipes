package me.Vark123.EpicRecipes.CraftingSystem;

import org.bukkit.Material;

import lombok.Getter;

@Getter
public class CraftBlock {

	private Material block;
	public String craftRegion;
	
	public CraftBlock(String craftRegion) {
		this(craftRegion, Material.CRAFTING_TABLE);
	}
	
	public CraftBlock(String craftRegion, Material block) {
		this.craftRegion = craftRegion;
		this.block = block;
	}
	
}
