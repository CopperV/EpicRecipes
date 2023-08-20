package me.Vark123.EpicRecipes.RecipeSystem;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import lombok.NonNull;
import me.Vark123.EpicRecipes.Utils.Utils;

@Getter
public class ShapelessRecipe extends ARecipe {

	private Map<String, Integer> recipe;
	
	public ShapelessRecipe(
			@NonNull Map<String, Integer> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult) {
		this(recipe, recipeId, group, mmResult, true);
	}

	public ShapelessRecipe(
			@NonNull Map<String, Integer> recipe,
			@NonNull String recipeId, 
			@NonNull RecipeGroup group, 
			@NonNull String mmResult, 
			boolean visible) {
		this(recipe, recipeId, group, mmResult, visible, 1);
	}

	public ShapelessRecipe(
			@NonNull Map<String, Integer> recipe,
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
		Map<String, Integer> check = new LinkedHashMap<>();
		for(int i = 0; i < 25; ++i) {
			int slot = Utils.mapCraftToInv(i);
			ItemStack it = inv.getItem(slot);
			if(it == null)
				continue;
			NBTItem nbt = new NBTItem(it);
			if(!nbt.hasTag("MYTHIC_TYPE"))
				return false;
			String mmId = nbt.getString("MYTHIC_TYPE");
			if(!recipe.containsKey(mmId))
				return false;
			if(check.containsKey(mmId)) {
				int tmp = check.get(mmId) + 1;
				check.put(mmId, tmp);
			} else {
				check.put(mmId, 1);
			}
		}
		if(check.size() != recipe.size())
			return false;
		
		MutableBoolean canCraft = new MutableBoolean(true);
		recipe.keySet().stream().filter(s -> {
			if(!check.containsKey(s))
				return true;
			if(recipe.get(s).intValue() != check.get(s).intValue())
				return true;
			return false;
		}).findAny().ifPresent(s -> {
			canCraft.setFalse();
		});
		return canCraft.booleanValue();
	}

	@Override
	public ItemStack getCraftResult() {
		ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(mmResult, amount);
		return it;
	}

	@Override
	public Map<Integer, ItemStack> getRecipeView() {
		Map<Integer, ItemStack> view = new HashMap<>();
		MutableInt index = new MutableInt();
		recipe.forEach((str, i) -> {
			ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(str, i);
			view.put(index.intValue(), it);
			index.add(1);
		});
		return view;
	}

}
