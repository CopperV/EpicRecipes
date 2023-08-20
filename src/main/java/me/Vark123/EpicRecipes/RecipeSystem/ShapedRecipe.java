package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import lombok.NonNull;
import me.Vark123.EpicRecipes.Utils.Utils;

@Getter
public class ShapedRecipe extends ARecipe {
	
	private Map<Integer, String> recipe;

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult) {
		this(recipe, recipeId, group, mmResult, true);
	}

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult, 
			boolean visible) {
		this(recipe, recipeId, group, mmResult, visible, 1);
	}

	public ShapedRecipe(
			@NonNull Map<Integer, String> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult, 
			boolean visible,
			int amount) {
		super(recipeId, group, mmResult, visible, amount);
		this.recipe = recipe;
	}

	@Override
	public boolean canCraft(Inventory inv) {
		for(int slot = 0; slot < 25; ++slot) {
			int invSlot = Utils.mapCraftToInv(slot);
			ItemStack it = inv.getItem(invSlot);
			if(it == null && recipe.containsKey(slot))
				return false;
			if(it != null && !recipe.containsKey(slot))
				return false;
			if(it == null
					|| it.getType().equals(Material.AIR))
				continue;
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("MYTHIC_TYPE"))
				return false;
			String mmId = nbt.getString("MYTHIC_TYPE");
			if(!mmId.equals(recipe.get(slot)))
				return false;
		}
		return true;
	}

	@Override
	public ItemStack getCraftResult() {
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(mmResult, amount);
		return it;
	}

	@Override
	public Map<Integer, ItemStack> getRecipeView() {
		Map<Integer, ItemStack> view = new HashMap<>();
		recipe.forEach((i, str) -> {
			ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(str);
			view.put(i, it);
		});
		return view;
	}

}
