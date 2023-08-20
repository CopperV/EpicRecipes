package me.Vark123.EpicRecipes.CraftingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.mutable.MutableObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.nbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRPG.Utils.Utils;
import me.Vark123.EpicRecipes.MenuSystem.Crafting.CraftingInvManager;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;
import me.Vark123.EpicRecipes.ProfessionSystem.ProfessionManager;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;

@Getter
public final class CraftingManager {

	private static final CraftingManager inst = new CraftingManager();
	
	private final Collection<CraftBlock> craftingBlocks;
	private final List<Integer> freeSlots;
	
	@Getter(value = AccessLevel.NONE)
	private final long CRAFT_COOLDOWN = 500;
	private final Map<Player, Date> cd;
	
	private CraftingManager() {
		craftingBlocks = new HashSet<>();
		freeSlots = new ArrayList<>(
				Arrays.asList(
						0,1,2,3,4, 
						9,10,11,12,13,
						18,19,20,21,22,
						27,28,29,30,31,
						36,37,38,39,40
				));
		cd = new ConcurrentHashMap<>();
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
	
	public void craft(Player p, Inventory craftInventory) {
		if(cd.containsKey(p) 
				&& (new Date().getTime() - cd.get(p).getTime()) < CRAFT_COOLDOWN)
			return;
		
		ItemStack result = craftInventory.getItem(24);
		NBTItem nbt = new NBTItem(result);
		ARecipe recipe = RecipeManager.get().getRecipeContainer().get(nbt.getString("recipe_id"));
		if(recipe == null)
			return;
		
		EpicCraftPlayer craftPlayer = PlayerManager.get().getPlayer(p).orElseThrow();
		
		CraftEvent event = new CraftEvent(p, recipe);
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled())
			return;
		
		ProfessionManager.get().getContainer().stream()
			.filter(profItem -> recipe.equals(profItem.getRecipe()))
			.forEach(profItem -> {
				craftPlayer.getProfessions().stream()
					.filter(prof -> prof.getClass().getName().equals(profItem.getProfessionClass().getName()))
					.forEach(prof -> prof.addProgress(profItem.getPoints()));
			});
		
		result = recipe.getCraftResult();
		Utils.dropItemStack(p, result);
		freeSlots.forEach(i -> {
			ItemStack it = craftInventory.getItem(i);
			if(it == null)
				return;
			Utils.takeItems(p, craftInventory, i, 1);
		});
		tryMatch(p, craftInventory);
		
		cd.put(p, new Date());
	}
	
	public void tryMatch(Player p, Inventory craftInventory) {
		
		Optional<EpicCraftPlayer> oEpicPlayer = PlayerManager.get().getPlayer(p);
		MutableObject<EpicCraftPlayer> epicPlayer = new MutableObject<>();
		if(oEpicPlayer.isPresent())
			epicPlayer.setValue(oEpicPlayer.get());
		
		RecipeManager.get().getRecipeContainer().values().parallelStream().filter(recipe -> {
			if(recipe.isCommon()
					&& recipe.canCraft(craftInventory))
				return true;
			if(epicPlayer.getValue() != null
					&& !epicPlayer.getValue().getLearnedRecipes().contains(recipe))
				return false;
			if(!recipe.canCraft(craftInventory))
				return false;
			return true;
		}).findFirst().ifPresentOrElse(recipe -> {
			ItemStack result = recipe.getCraftResult();
			if(result == null){
				craftInventory.setItem(24, new ItemStack(Material.AIR));
				craftInventory.setItem(26, CraftingInvManager.get().getRedCreate());
				return;
			}
			NBTItem nbt = new NBTItem(result);
			nbt.setString("recipe_id", recipe.getRecipeId());
			nbt.applyNBT(result);
			craftInventory.setItem(24, result);
			craftInventory.setItem(26, CraftingInvManager.get().getGreenCreate());
		}, () -> {
			craftInventory.setItem(24, new ItemStack(Material.AIR));
			craftInventory.setItem(26, CraftingInvManager.get().getRedCreate());
		});
//		p.updateInventory();
		
	}
	
}
