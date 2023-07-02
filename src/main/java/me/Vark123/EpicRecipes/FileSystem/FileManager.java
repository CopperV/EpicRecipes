package me.Vark123.EpicRecipes.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroupManager;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;
import me.Vark123.EpicRecipes.RecipeSystem.ShapedRecipe;
import me.Vark123.EpicRecipes.RecipeSystem.ShapelessRecipe;

public final class FileManager {

	private static final FileManager inst = new FileManager();

	private final File playerDir;
	private final File config;
	private final File shapeless;
	private final File shaped;
	private final File teachers;
	private final File groups;

	private boolean init = false;

	private FileManager() {
		playerDir = new File(Main.inst().getDataFolder(), "players");
		config = new File(Main.inst().getDataFolder(), "config.yml");
		shapeless = new File(Main.inst().getDataFolder(), "shapeless.yml");
		shaped = new File(Main.inst().getDataFolder(), "shaped.yml");
		teachers = new File(Main.inst().getDataFolder(), "teachers.yml");
		groups = new File(Main.inst().getDataFolder(), "groups.yml");
	}

	public static final FileManager get() {
		return inst;
	}

	public void init() {
		if (init)
			return;
		init = true;

		if (!Main.inst().getDataFolder().exists())
			Main.inst().getDataFolder().mkdir();
		if (!playerDir.exists())
			playerDir.mkdir();

		try {
			if (!config.exists())
				config.createNewFile();
			if (!shapeless.exists())
				shapeless.createNewFile();
			if (!shaped.exists())
				shaped.createNewFile();
			if (!teachers.exists())
				teachers.createNewFile();
			if (!groups.exists())
				groups.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(Main.inst());
			return;
		}

		loadRecipeGroups();
		loadRecipes();
	}

	private void loadRecipeGroups() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(groups);
		ConfigurationSection rootSection = fYml.getConfigurationSection("root");
		String groupId = rootSection.getString("groupName");
		String display = ChatColor.translateAlternateColorCodes('&', rootSection.getString("display"));
		boolean visible = rootSection.getBoolean("visible", true);

		RecipeGroup root = RecipeGroupManager.get().createGroup(groupId, display, visible);
		loadSons(rootSection, root);

		RecipeGroupManager.get().createGroup(root, "unasigned", "&7&oNieprzypisane", true);
	}

	private void loadSons(ConfigurationSection fatherSection, RecipeGroup father) {
		if (!fatherSection.contains("sons"))
			return;
		ConfigurationSection sonsSection = fatherSection.getConfigurationSection("sons");
		sonsSection.getKeys(false).forEach(key -> {
			ConfigurationSection son = sonsSection.getConfigurationSection(key);
			String groupId = son.getString("groupName");
			String display = ChatColor.translateAlternateColorCodes('&', son.getString("display"));
			boolean visible = son.getBoolean("visible", true);

			RecipeGroup group = RecipeGroupManager.get().createGroup(father, groupId, display, visible);
			loadSons(son, group);
		});
	}

	private void loadRecipes() {
		YamlConfiguration shapedYml = YamlConfiguration.loadConfiguration(shaped);
		YamlConfiguration shapelessYml = YamlConfiguration.loadConfiguration(shapeless);

		shapedYml.getKeys(false).stream().forEach(key -> {
			String strItem = shapedYml.getString(key + ".result");
			int amount = shapedYml.getInt(key + ".amount");
			boolean visible = shapedYml.getBoolean(key + ".visible", true);
			Map<Integer, String> recipe = getShapedRecipe(shapedYml.getStringList(key + ".recipe"));

			ConfigurationSection section = YamlConfiguration.loadConfiguration(groups).getConfigurationSection("root");
			Optional<RecipeGroup> oGroup = getRecipeGroup(section, key);
			oGroup.ifPresentOrElse(group -> {
				ARecipe aRecipe = new ShapedRecipe(recipe, key, group, strItem, visible, amount);
				RecipeManager.get().addRecipe(aRecipe);
			}, () -> {
				Optional<RecipeGroup> oMiscGroup = RecipeGroupManager.get().getGroupContainer().stream()
						.filter(miscGroup -> {
							return miscGroup.getGroupId().equalsIgnoreCase("unasigned");
						}).findAny();
				oMiscGroup.ifPresentOrElse(miscGroup -> {
					ARecipe aRecipe = new ShapedRecipe(recipe, key, miscGroup, strItem, visible, amount);
					RecipeManager.get().addRecipe(aRecipe);
				}, () -> {
					throw new IllegalArgumentException("Cannot match recipe "+key+" to any group");
				});
			});
		});
		
		shapelessYml.getKeys(false).stream().forEach(key -> {
			String strItem = shapedYml.getString(key + ".result");
			int amount = shapedYml.getInt(key + ".amount");
			boolean visible = shapedYml.getBoolean(key + ".visible", true);
			Map<String, Integer> recipe = getShapelessRecipe(shapelessYml.getConfigurationSection(key+".recipe"));

			ConfigurationSection section = YamlConfiguration.loadConfiguration(groups).getConfigurationSection("root");
			Optional<RecipeGroup> oGroup = getRecipeGroup(section, key);
			oGroup.ifPresentOrElse(group -> {
				ARecipe aRecipe = new ShapelessRecipe(recipe, key, group, strItem, visible, amount);
				RecipeManager.get().addRecipe(aRecipe);
			}, () -> {
				Optional<RecipeGroup> oMiscGroup = RecipeGroupManager.get().getGroupContainer().stream()
						.filter(miscGroup -> {
							return miscGroup.getGroupId().equalsIgnoreCase("unasigned");
						}).findAny();
				oMiscGroup.ifPresentOrElse(miscGroup -> {
					ARecipe aRecipe = new ShapelessRecipe(recipe, key, miscGroup, strItem, visible, amount);
					RecipeManager.get().addRecipe(aRecipe);
				}, () -> {
					throw new IllegalArgumentException("Cannot match recipe "+key+" to any group");
				});
			});
		});
	}

	private Optional<RecipeGroup> getRecipeGroup(ConfigurationSection root, String key) {
		String groupId = root.getString("groupName");
		if (root.contains("recipes") && root.getStringList("recipes").contains(key)) {
			Optional<RecipeGroup> oGroup = RecipeGroupManager.get().getGroupContainer().stream().filter(group -> {
				return group.getGroupId().equalsIgnoreCase(groupId);
			}).findAny();
			if (oGroup.isPresent())
				return oGroup;
		}
		if (root.contains("sons")) {
			ConfigurationSection sonsSection = root.getConfigurationSection("sons");
			for (String sonKey : sonsSection.getKeys(false)) {
				ConfigurationSection son = sonsSection.getConfigurationSection(sonKey);
				Optional<RecipeGroup> oGroup = getRecipeGroup(son, key);
				if (oGroup.isPresent())
					return oGroup;
			}
		}
		return Optional.empty();
	}

	private Map<Integer, String> getShapedRecipe(List<String> list) {
		Map<Integer, String> grid = new HashMap<>();
		for (int i = 0; i < list.size(); ++i) {
			String strItem = list.get(i);
			if (strItem.equalsIgnoreCase("null"))
				continue;
			grid.put(i, strItem);
		}
		return grid;
	}

	private Map<String, Integer> getShapelessRecipe(ConfigurationSection recipeSection) {
		Map<String, Integer> recipe = new HashMap<>();
		recipeSection.getKeys(false).forEach(key -> {
			String strItem = recipeSection.getString(key + ".mm_id");
			int amount = recipeSection.getInt(key + ".amount");
			recipe.put(strItem, amount);
		});
		return recipe;
	}

}
