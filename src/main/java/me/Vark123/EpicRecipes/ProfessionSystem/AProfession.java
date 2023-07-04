package me.Vark123.EpicRecipes.ProfessionSystem;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AProfession {

	private String id;
	private String display;
	private int level;
	private int progress;
	
	public abstract void print(Player p);
	public abstract void addProgress();
	
}
