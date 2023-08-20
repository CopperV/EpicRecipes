package me.Vark123.EpicRecipes.CraftingSystem;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRecipes.RecipeSystem.ARecipe;

@Getter
public class CraftEvent extends Event implements Cancellable {
	
	@Setter
	private boolean cancelled;
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private ARecipe recipe;

	public CraftEvent(Player player, ARecipe recipe) {
		this.player = player;
		this.recipe = recipe;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
