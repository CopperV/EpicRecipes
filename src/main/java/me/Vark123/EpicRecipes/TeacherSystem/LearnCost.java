package me.Vark123.EpicRecipes.TeacherSystem;

import org.bukkit.entity.Player;

public interface LearnCost {

	public boolean canBuy(Player p);
	public void buy(Player p);
	public String getCostInfo(Player p);
	
}
