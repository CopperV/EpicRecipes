package me.Vark123.EpicRecipes.PlayerSystem;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.Vark123.EpicRecipes.ProfessionSystem.AProfession;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
public final class PlayerManager {

	private static final PlayerManager inst = new PlayerManager();
	
	private final Map<Player, EpicCraftPlayer> container;
	
	private PlayerManager() {
		container = new ConcurrentHashMap<>();
	}
	
	public static final PlayerManager get() {
		return inst;
	}
	
	public EpicCraftPlayer addPlayer(Player p, Collection<ARecipe> learnedRecipes, Set<AProfession> professions) {
		EpicCraftPlayer epicPlayer = new EpicCraftPlayer(p, learnedRecipes, professions);
		addPlayer(epicPlayer);
		return epicPlayer;
	}
	
	public void addPlayer(EpicCraftPlayer epicPlayer) {
		container.put(epicPlayer.getPlayer(), epicPlayer);
	}
	
	public Optional<EpicCraftPlayer> getPlayer(Player p) {
		if(!container.containsKey(p))
			return Optional.empty();
		return Optional.of(container.get(p));
	}
	
	public void removePlayer(Player p) {
		container.remove(p);
	}
	
}
