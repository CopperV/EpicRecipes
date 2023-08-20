package me.Vark123.EpicRecipes.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.CraftingSystem.CraftingManager;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.ProfessionSystem.AProfession;
import me.Vark123.EpicRecipes.ProfessionSystem.ProfessionManager;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Alchemia;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Jubilerstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Kowalstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Luczarstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Platnerstwo;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroup;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroupManager;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;
import me.Vark123.EpicRecipes.RecipeSystem.ShapedRecipe;
import me.Vark123.EpicRecipes.RecipeSystem.ShapelessRecipe;
import me.Vark123.EpicRecipes.TeacherSystem.LearnCost;
import me.Vark123.EpicRecipes.TeacherSystem.LearningRecipe;
import me.Vark123.EpicRecipes.TeacherSystem.TeachManager;
import me.Vark123.EpicRecipes.TeacherSystem.Teacher;
import me.Vark123.EpicRecipes.TeacherSystem.LearningCosts.ExperienceCost;
import me.Vark123.EpicRecipes.TeacherSystem.LearningCosts.MoneyCost;
import me.Vark123.EpicRecipes.TeacherSystem.LearningCosts.ReputationCost;
import me.Vark123.EpicRecipes.TeacherSystem.LearningCosts.RzemiosloCost;

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
		loadCraftBlocks();
		loadProfItems();
		loadLearnItems();
		loadTeachers();
	}
	
	private void loadTeachers() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(teachers);
		ConfigurationSection section = fYml.getConfigurationSection("teachers");
		section.getKeys(false).stream().forEach(key -> {
			ConfigurationSection teacherSection = section.getConfigurationSection(key);
			
			String id = teacherSection.getString("id");
			Collection<LearningRecipe> recipesToTeach = teacherSection.getStringList("recipes")
					.stream()
					.filter(recId -> TeachManager.get().getById(recId).isPresent())
					.map(recId -> TeachManager.get().getById(recId).get())
					.toList();
			TeachManager.get().getTeachers().put(id, new Teacher(id, recipesToTeach));
		});
	}
	
	private void loadLearnItems() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(config);
		ConfigurationSection section = fYml.getConfigurationSection("recipe_requirements");
		section.getKeys(false).stream().forEach(id -> {
			ConfigurationSection recipeSection = section.getConfigurationSection(id);
			String recipeId = recipeSection.getString("recipe_id");
			ARecipe recipe = RecipeManager.get().getRecipeContainer().get(recipeId);
			if(recipe == null)
				return;
			
			Collection<LearnCost> costs = new HashSet<>();
			if(recipeSection.contains("cost")) {
				double amount = recipeSection.getDouble("cost");
				costs.add(new MoneyCost(amount));
			}
			if(recipeSection.contains("profession")) {
				String prof = recipeSection.getString("profession");
				costs.add(new RzemiosloCost(prof));
				if(recipeSection.contains("level")) {
					int level = recipeSection.getInt("level");
					costs.add(new ExperienceCost(prof, level));
				}
			}
			if(recipeSection.contains("reputation")) {
				String[] repArr = recipeSection.getString("reputation").split(":");
				if(StringUtils.isNumeric(repArr[1]) 
						&& !repArr[1].contains(".")) {
					costs.add(new ReputationCost(repArr[0], Integer.parseInt(repArr[1])));
				}
			}
			TeachManager.get().getRecipesToLearn().add(new LearningRecipe(recipe, costs));
		});
	}
	
	private void loadProfItems() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(config);
		ConfigurationSection section = fYml.getConfigurationSection("profession_upgrade");
		section.getKeys(false).stream().forEach(key -> {
			ConfigurationSection profSection = section.getConfigurationSection(key);
			profSection.getKeys(false).parallelStream()
				.filter(RecipeManager.get().getRecipeContainer()::containsKey)
				.map(RecipeManager.get().getRecipeContainer()::get)
				.forEach(recipe -> {
					String id = recipe.getRecipeId();
					int points = profSection.getInt(id+".points");
					ProfessionManager.get().addProfItem(key, recipe, points, recipe.getMmResult());
				});
		});
	}
	
	private void loadCraftBlocks() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(config);
		fYml.getStringList("regions").forEach(region -> {
			CraftingManager.get().registerCraftBlock(region);
		});
	}

	private void loadRecipeGroups() {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(groups);
		ConfigurationSection rootSection = fYml.getConfigurationSection("root");
		String groupId = rootSection.getString("groupName");
		String display = ChatColor.translateAlternateColorCodes('&', rootSection.getString("display"));
		boolean visible = rootSection.getBoolean("visible", true);

		RecipeGroup root = RecipeGroupManager.get().createGroup(groupId, display, visible);
		loadSons(rootSection, root);

		RecipeGroupManager.get().createGroup(root, "unasigned", "§7§oNieprzypisane", true);
	}

	private void loadSons(ConfigurationSection fatherSection, RecipeGroup father) {
		if (!fatherSection.contains("sons"))
			return;
		ConfigurationSection sonsSection = fatherSection.getConfigurationSection("sons");
		sonsSection.getKeys(false).stream().forEachOrdered(key -> {
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
		YamlConfiguration configYml = YamlConfiguration.loadConfiguration(config);
		
		shapedYml.getKeys(false).stream().forEachOrdered(key -> {
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
			String strItem = shapelessYml.getString(key + ".result");
			int amount = shapelessYml.getInt(key + ".amount");
			boolean visible = shapelessYml.getBoolean(key + ".visible", true);
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
		
		List<String> commonRecipes = configYml.getStringList("common_recipes");
		commonRecipes.stream().filter(recipe -> {
			return RecipeManager.get().getRecipeContainer().containsKey(recipe);
		}).map(recipe -> {
			return RecipeManager.get().getRecipeContainer().get(recipe);
		}).forEach(recipe -> {
			recipe.setCommon(true);
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
	
	public EpicCraftPlayer loadPlayer(Player p) {
		File f = getPlayerFile(p);
		if(f == null) {
			System.out.println("Cannot read player recipes file ["+p.getName()+"]");
			return new EpicCraftPlayer(p, new HashSet<>(), new HashSet<>());
		}
		
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);
		
		List<String> strLearnedRecipes = fYml.getStringList("learned_recipes");
		Collection<ARecipe> learnedRecipes = strLearnedRecipes.stream()
				.filter(s -> {
					return RecipeManager.get().getRecipeContainer().containsKey(s);
				}).map(s -> {
					return RecipeManager.get().getRecipeContainer().get(s);
				}).collect(Collectors.toSet());
		
		Set<AProfession> professions = new HashSet<>();
		int kowalstwoLevel = fYml.getInt("kowalstwo_level", 0);
		int kowalstwoProgress = fYml.getInt("kowalstwo_progress", 0);
		int alchemiaLevel = fYml.getInt("alchemia_level", 0);
		int alchemiaProgress = fYml.getInt("alchemia_progress", 0);
		int platnerstwoLevel = fYml.getInt("platnerstwo_level", 0);
		int platnerstwoProgress = fYml.getInt("platnerstwo_progress", 0);
		int luczarstwoLevel = fYml.getInt("luczarstwo_level", 0);
		int luczarstwoProgress = fYml.getInt("luczarstwo_progress", 0);
		int jubilerstwoLevel = fYml.getInt("jubilerstwo_level", 0);
		int jubilerstwoProgress = fYml.getInt("jubilerstwo_progress", 0);
		professions.add(new Kowalstwo(p, kowalstwoLevel, kowalstwoProgress));
		professions.add(new Alchemia(p, alchemiaLevel, alchemiaProgress));
		professions.add(new Platnerstwo(p, platnerstwoLevel, platnerstwoProgress));
		professions.add(new Luczarstwo(p, luczarstwoLevel, luczarstwoProgress));
		professions.add(new Jubilerstwo(p, jubilerstwoLevel, jubilerstwoProgress));
	
		EpicCraftPlayer epicPlayer = new EpicCraftPlayer(p, learnedRecipes, professions);
		return epicPlayer;
	}
	
	public void savePlayer(EpicCraftPlayer epicPlayer) {
		Player p = epicPlayer.getPlayer();
		File f = getPlayerFile(p);
		if(f == null) {
			System.out.println("Cannot save player recipes file ["+p.getName()+"]");
			return;
		}

		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(f);

		List<String> learnedRecipes = epicPlayer.getLearnedRecipes().stream().map(recipe -> {
			return recipe.getRecipeId();
		}).toList();
		fYml.set("learned_recipes", learnedRecipes);
		
		epicPlayer.getProfessions().forEach(prof -> {
			String name = prof.getClass().getSimpleName().toLowerCase();
			fYml.set(name+"_level", prof.getLevel());
			fYml.set(name+"_progress", prof.getProgress());
		});
		
		try {
			fYml.save(f);
		} catch (IOException e) {			
			System.out.println("Cannot save player recipes file ["+p.getName()+"]");
			e.printStackTrace();
		}
		
	}
	
	private File getPlayerFile(Player p) {
		String fileName = p.getName().toLowerCase()+".yml";
		File f = new File(playerDir, fileName);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return f;
	}
	
	public void saveShapedRecipe(ShapedRecipe recipe) {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(shaped);
		
		String id = recipe.getRecipeId();
		fYml.set(id+".amount", recipe.getAmount());
		fYml.set(id+".result",recipe.getMmResult());
		
		List<String> strRecipe = new LinkedList<String>();
		for(int i = 0; i < 25; ++i) {
			strRecipe.add(recipe.getRecipe().getOrDefault(i, "null"));
		}
		fYml.set(id+".recipe", strRecipe);
		
		try {
			fYml.save(shaped);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveShapelessRecipe(ShapelessRecipe recipe) {
		YamlConfiguration fYml = YamlConfiguration.loadConfiguration(shapeless);

		String id = recipe.getRecipeId();
		fYml.set(id+".amount", recipe.getAmount());
		fYml.set(id+".result",recipe.getMmResult());
		recipe.getRecipe().forEach((mmId, amount) -> {
			fYml.set(id+".recipe."+mmId+".mm_id", amount);
			fYml.set(id+".recipe."+mmId+".amount", amount);
		});
		
		try {
			fYml.save(shapeless);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
