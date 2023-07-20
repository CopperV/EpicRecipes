package me.Vark123.EpicRecipes.MenuSystem.Teacher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import lombok.AccessLevel;
import lombok.Getter;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;
import me.Vark123.EpicRecipes.TeacherSystem.LearningRecipe;
import me.Vark123.EpicRecipes.TeacherSystem.Teacher;

@Getter
public final class TeachMenuManager {

	private static final TeachMenuManager inst = new TeachMenuManager();
	
	private final ItemStack next;
	private final ItemStack previous;
	private final ItemStack empty;
	
	@Getter(value = AccessLevel.NONE)
	public final int ITEMS_PER_PAGE = 45;
	
	private TeachMenuManager() {
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
	}
	
	public static final TeachMenuManager get() {
		return inst;
	}
	
	public void openMenu(Player p, Teacher teacher) {
		EpicCraftPlayer craftPlayer = PlayerManager.get().getPlayer(p).orElseThrow();
		List<LearningRecipe> unlearnedRecipes = teacher.getRecipesToTeach()
				.stream()
				.filter(rec -> {
					return !craftPlayer.getLearnedRecipes().contains(rec.getRecipeToLearn());
				}).toList();
		TeacherHolder holder = new TeacherHolder(0, unlearnedRecipes);
		Inventory inv = Bukkit.createInventory(holder, 6*9, "§a§lNauczyciel przepisow");
		setupPage(p, inv);
		p.openInventory(inv);
	}
	
	public void openMenu(Player p, Inventory presentView, int newPage) {
		InventoryHolder holder = presentView.getHolder();
		if(holder == null
				|| !(holder instanceof TeacherHolder))
			return;
		
		TeacherHolder teachHolder = (TeacherHolder) holder;
		teachHolder.setPage(newPage);
		setupPage(p, presentView);
		p.updateInventory();
	}
	
	private void setupPage(Player p, Inventory inv) {
		inv.clear();
		InventoryHolder holder = inv.getHolder();
		if(holder == null
				|| !(holder instanceof TeacherHolder))
			return;
		TeacherHolder teachHolder = (TeacherHolder) holder;
		List<LearningRecipe> recipes = teachHolder.getRecipes();
		int page = teachHolder.getPage();
		int offset = ITEMS_PER_PAGE*page;
		int end = (ITEMS_PER_PAGE*(page+1)) > recipes.size() ? recipes.size() : (ITEMS_PER_PAGE*(page+1));
		
		for(int i = 0; i < (end-offset); ++i) {
			LearningRecipe recipe = recipes.get(i+offset);
			ItemStack it = recipe.getRecipeToLearn().getCraftResult();
			
			it.setType(Material.MAP);
			ItemMeta im = it.getItemMeta();
			im.setDisplayName("§b§l§oPrzepis: §r"+im.getDisplayName());
			List<String> lore = im.getLore() == null ? new LinkedList<>() : im.getLore();
			lore.addAll(Arrays.asList(" ","§6§l》§c Wymagania nauki §6§l《"," "));
			recipe.getCosts().forEach(cost -> {
				lore.add("§4> §r"+cost.getCostInfo(p));
			});
			im.setLore(lore);
			it.setItemMeta(im);
			
			NBTItem nbt = new NBTItem(it);
			nbt.setString("recipe_id", recipe.getRecipeToLearn().getRecipeId());
			nbt.applyNBT(it);
			
			inv.setItem(i, it);
		}

		inv.setItem(45, empty);
		inv.setItem(46, empty);
		if(offset > 0) {
			inv.setItem(47, previous);
		} else {
			inv.setItem(47, empty);
		}
		inv.setItem(48, empty);
		inv.setItem(49, getPageInfo(teachHolder));
		inv.setItem(50, empty);
		if(end < recipes.size()) {
			inv.setItem(51, next);
		} else {
			inv.setItem(51, empty);
		}
		inv.setItem(52, empty);
		inv.setItem(53, empty);
	}
	
	private ItemStack getPageInfo(TeacherHolder recipeHolder) {
		int page = recipeHolder.getPage();
		
		ItemStack it = new ItemStack(Material.PAPER);
		ItemMeta im = it.getItemMeta();
		im.setDisplayName("§lStrona "+(page+1));
		it.setItemMeta(im);
		
		return it;
	}
	
}
