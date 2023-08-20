package me.Vark123.EpicRecipes.TeacherSystem;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;
import me.Vark123.EpicRecipes.Utils.BooleanAction;
import me.Vark123.EpicRecipes.Utils.BooleanMessage;

@Getter
public final class TeachManager {

	private static final TeachManager inst = new TeachManager();
	
	private final Collection<LearningRecipe> recipesToLearn;
	private final Map<String, Teacher> teachers;
	
	private TeachManager() {
		recipesToLearn = new LinkedList<>();
		teachers = new HashMap<>();
	}
	
	public static final TeachManager get() {
		return inst;
	}
	
	public Optional<LearningRecipe> getById(String id) {
		ARecipe recipe = RecipeManager.get().getRecipeContainer().get(id);
		if(recipe == null)
			return Optional.empty();
		return getByRecipe(recipe);
	}
	
	public Optional<LearningRecipe> getByRecipe(ARecipe recipe) {
		return recipesToLearn.stream()
			.filter(tmp -> tmp.getRecipeToLearn().equals(recipe))
			.findFirst();
	}
	
	public void learnRecipe(Player p, LearningRecipe recipe) {
		EpicCraftPlayer craftPlayer = PlayerManager.get().getPlayer(p)
				.orElseThrow();
		for(LearnCost cost : recipe.getCosts()) {
			BooleanMessage action = cost.canBuy(p);
			if(action.getAction().equals(BooleanAction.DENY)) {
				action.getMessage().ifPresent(p::sendMessage);
				return;
			}
		}
		for(LearnCost cost : recipe.getCosts()) {
			cost.buy(p);
		}
		ARecipe craftRecipe = recipe.getRecipeToLearn();
		craftPlayer.getLearnedRecipes().add(craftRecipe);
		p.sendMessage(Main.inst().getPrefix()+" §aNauczyles sie przepisu na "+
				craftRecipe.getCraftResult().getItemMeta().getDisplayName());
	}
	
}
