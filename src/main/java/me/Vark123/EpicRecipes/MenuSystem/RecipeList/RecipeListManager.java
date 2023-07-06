package me.Vark123.EpicRecipes.MenuSystem.RecipeList;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import lombok.Getter;
import me.Vark123.EpicRecipes.CraftingSystem.CraftingManager;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroupManager;
import me.Vark123.EpicRecipes.Utils.Utils;

@Getter
public final class RecipeListManager {

	private static final RecipeListManager inst = new RecipeListManager();
	
	private final ItemStack next;
	private final ItemStack previous;
	private final ItemStack empty;
	private final ItemStack back;
	
	private final int ITEMS_PER_PAGE = 45;
	
	private RecipeListManager() {
		next = new ItemStack(Material.PAPER);{
			ItemMeta im = next.getItemMeta();
			im.setDisplayName("§lNastepna");
			next.setItemMeta(im);
		}
		previous = new ItemStack(Material.PAPER);{
			ItemMeta im = previous.getItemMeta();
			im.setDisplayName("§lPoprzednia");
			previous.setItemMeta(im);
		}
		empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);{
			ItemMeta im = empty.getItemMeta();
			im.setDisplayName(" ");
			empty.setItemMeta(im);
		}
		back = new ItemStack(Material.BARRIER);{
			ItemMeta im = back.getItemMeta();
			im.setDisplayName("§cPowrot");
			back.setItemMeta(im);
		}
	}
	
	public static final RecipeListManager get() {
		return inst;
	}
	
	public void openMenu(Player p) {
		Optional<RecipeGroup> oGroup = RecipeGroupManager.get()
				.getGroupContainer().stream().filter(group -> {
					return group.getGroupId().equalsIgnoreCase("root");
				}).findAny();
		if(oGroup.isEmpty())
			return;
		RecipeGroup group = oGroup.get();
		int page = 0;
		List<ItemStack> items = getRecipeListItems(p, group);
		RecipeListHolder holder = new RecipeListHolder(group, items, false, page, null);
		Inventory inv = Bukkit.createInventory(holder, 6*9, "§2§lPrzepisy");
		
		setupPage(inv);
		
		p.openInventory(inv);
	}
	
	public void openMenu(Player p, Inventory presentView, RecipeGroup newGroup) {
		InventoryHolder holder = presentView.getHolder();
		if(holder == null
				|| !(holder instanceof RecipeListHolder))
			return;
		RecipeListHolder oldHolder = (RecipeListHolder) holder;
		List<ItemStack> items = getRecipeListItems(p, newGroup);
		
		RecipeListHolder newHolder;
		RecipeGroup father = oldHolder.getPresentGroup().getFather();
		if(father != null 
				&& father.equals(newGroup)
				&& oldHolder.getFather() != null) {
			newHolder = oldHolder.getFather();
		} else {
			int page = 0;
			newHolder = new RecipeListHolder(newGroup, items, false, page, oldHolder);
		}
		
		Inventory inv = Bukkit.createInventory(newHolder, 6*9, "§2§lPrzepisy");
		
		setupPage(inv);
		
		p.openInventory(inv);
	}
	
	public void openMenu(Player p, Inventory presentView, int page) {
		InventoryHolder holder = presentView.getHolder();
		if(holder == null
				|| !(holder instanceof RecipeListHolder))
			return;
		RecipeListHolder recipeHolder = (RecipeListHolder) holder;
		
		recipeHolder.setPage(page);
		setupPage(presentView);
		p.updateInventory();
	}
	
	public void openMenu(Player p, Inventory presentView, ARecipe recipe) {
		InventoryHolder holder = presentView.getHolder();
		if(holder == null
				|| !(holder instanceof RecipeListHolder))
			return;
		RecipeListHolder recipeHolder = (RecipeListHolder) holder;
		recipeHolder.setRecipeView(true);
		Map<Integer, ItemStack> recipeView = recipe.getRecipeView();
		
		presentView.clear();
		recipeView.forEach((i, item) -> {
			int slot = Utils.mapCraftToInv(i);
			presentView.setItem(slot, item);
		});
		
		List<Integer> freeSlots = CraftingManager.get().getFreeSlots();
		for(int i = 0; i < 54; ++i) {
			if(freeSlots.contains(i))
				continue;
			presentView.setItem(i, empty);
		}
		presentView.setItem(49, back);
		ItemStack result = MythicBukkit.inst()
				.getItemManager()
				.getItemStack(recipe.getMmResult(), recipe.getAmount());
		if(result != null)
			presentView.setItem(24, result);
		
		p.updateInventory();
	}
	
	private void setupPage(Inventory inv) {
		inv.clear();
		InventoryHolder holder = inv.getHolder();
		if(holder == null
				|| !(holder instanceof RecipeListHolder))
			return;
		RecipeListHolder recipeHolder = (RecipeListHolder) holder;
		List<ItemStack> items = recipeHolder.getVisibleItems();
		int page = recipeHolder.getPage();
		int offset = ITEMS_PER_PAGE*page;
		int end = (ITEMS_PER_PAGE*(page+1)) > items.size() ? items.size() : (ITEMS_PER_PAGE*(page+1));
		
		for(int i = 0; i < (end-offset); ++i) {
			inv.setItem(i, items.get(i+offset));
		}
		
		if(recipeHolder.getPresentGroup().getFather() != null) {
			inv.setItem(45, back);
		} else {
			inv.setItem(45, empty);
		}
		inv.setItem(46, empty);
		if(offset > 0) {
			inv.setItem(47, previous);
		} else {
			inv.setItem(47, empty);
		}
		inv.setItem(48, empty);
		inv.setItem(49, getPageInfo(recipeHolder));
		inv.setItem(50, empty);
		if(end < items.size()) {
			inv.setItem(51, next);
		} else {
			inv.setItem(51, empty);
		}
		inv.setItem(52, empty);
		inv.setItem(53, empty);
	}
	
	private ItemStack getPageInfo(RecipeListHolder recipeHolder) {
		int page = recipeHolder.getPage();
		RecipeGroup group = recipeHolder.getPresentGroup();
		
		ItemStack it = new ItemStack(Material.PAPER);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§lStrona "+(page+1));
		im.setLore(Arrays.asList("§7Grupa: §r"+group.getDisplay()));
		it.setItemMeta(im);
		
		return it;
	}
	
	private List<ItemStack> getRecipeListItems(Player p, RecipeGroup group) {
		List<ItemStack> items = new LinkedList<>();
		
		group.getSons().stream().filter(son -> {
			return son.isGroupVisible();
		}).forEachOrdered(son -> {
			ItemStack it = new ItemStack(Material.WRITABLE_BOOK);
			
			ItemMeta im = it.getItemMeta();
			im.setDisplayName("§7Grupa: §r"+son.getDisplay());
			it.setItemMeta(im);
			
			NBTItem nbt = new NBTItem(it);
			nbt.setString("group", son.getGroupId());
			nbt.applyNBT(it);
			
			items.add(it);
		});
		
		Optional<EpicCraftPlayer> oEpicPlayer = PlayerManager.get().getPlayer(p);
		oEpicPlayer.ifPresentOrElse(epicPlayer -> {
			group.getRecipes().stream().filter(recipe -> {
				return epicPlayer.getLearnedRecipes().contains(recipe)
						|| recipe.isCommon();
			}).forEachOrdered(recipe -> {
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(recipe.getMmResult());
				if(it == null)
					return;
				it.setAmount(recipe.getAmount());
				
				NBTItem nbt = new NBTItem(it);
				nbt.setString("recipe_id", recipe.getRecipeId());
				nbt.applyNBT(it);
				
				items.add(it);
			});
		}, () -> {
			group.getRecipes().stream().filter(recipe -> {
				return recipe.isCommon();
			}).forEach(recipe -> {
				ItemStack it = MythicBukkit.inst().getItemManager().getItemStack(recipe.getMmResult());
				if(it == null)
					return;
				it.setAmount(recipe.getAmount());
				
				NBTItem nbt = new NBTItem(it);
				nbt.setString("recipe_id", recipe.getRecipeId());
				nbt.applyNBT(it);
				
				items.add(it);
			});
		});
		
		return items;
	}
	
}
