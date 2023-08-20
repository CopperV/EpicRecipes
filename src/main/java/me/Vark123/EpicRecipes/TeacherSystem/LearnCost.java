package me.Vark123.EpicRecipes.TeacherSystem;

import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Utils.BooleanMessage;

public interface LearnCost {

	public BooleanMessage canBuy(Player p);
	public void buy(Player p);
	public String getCostInfo(Player p);
	
}
