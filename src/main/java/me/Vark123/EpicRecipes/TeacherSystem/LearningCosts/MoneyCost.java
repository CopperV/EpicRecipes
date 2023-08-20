package me.Vark123.EpicRecipes.TeacherSystem.LearningCosts;

import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.TeacherSystem.LearnCost;
import me.Vark123.EpicRecipes.Utils.BooleanMessage;

public class MoneyCost implements LearnCost {

	private final double cost;
	
	public MoneyCost(double cost) {
		this.cost = cost;
	}
	
	@Override
	public BooleanMessage canBuy(Player p) {
		if(Main.eco.getBalance(p) >= cost) {
			return BooleanMessage.ALLOW();
		}
		return BooleanMessage.DENY(Main.inst().getPrefix()
				+" §cMasz za malo srodkow by nauczyc sie tego przepisu:"
				+ " §e§o"+String.format("%.2f", (cost))+"$");
	}

	@Override
	public void buy(Player p) {
		Main.eco.withdrawPlayer(p, cost);
	}

	@Override
	public String getCostInfo(Player p) {
		return "§8Koszt: §e§o"+String.format("%.2f", (cost))+"$";
	}

}
