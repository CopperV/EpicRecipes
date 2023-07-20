package me.Vark123.EpicRecipes.TeacherSystem.LearningCosts;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgReputation;
import me.Vark123.EpicRPG.Reputation.ReputationLevels;
import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.TeacherSystem.LearnCost;
import me.Vark123.EpicRecipes.Utils.BooleanMessage;

public class ReputationCost implements LearnCost {

	private final String reputation;
	private final int level;
	
	public ReputationCost(String reputation, int level) {
		this.reputation = reputation;
		this.level = level;
	}
	
	@Override
	public BooleanMessage canBuy(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgReputation rpgRep = rpg.getReputation();
		me.Vark123.EpicRPG.Reputation.Reputation rep = rpgRep.getReputacja().get(reputation);
		if(rep == null)
			return BooleanMessage.DENY();
		if(rep.getReputationLevel().getId() >= level) {
			return BooleanMessage.ALLOW();
		} else {
			return BooleanMessage.DENY(Main.inst().getPrefix()
					+" §cTwoja reputacja u §r"+rep.getDisplayFraction()
					+ " jest zbyt niska, by sie tego nauczyc: "
					+ "§a§o"+ReputationLevels.getReputationLevelById(level)+" §7(§f"+rep.getReputationLevel()+"§7)");
		}
	}

	@Override
	public void buy(Player p) {
		return;
	}

	@Override
	public String getCostInfo(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgReputation rpgRep = rpg.getReputation();
		me.Vark123.EpicRPG.Reputation.Reputation rep = rpgRep.getReputacja().get(reputation);
		if(rep == null)
			return "";
		return "§8Reputacja "+rep.getDisplayFraction()+"§r§8: §a§o"
			+rep.getReputationLevel().getName()
			+" §7[§f"+rep.getReputationLevel().getId()+"§7]";
	}

}
