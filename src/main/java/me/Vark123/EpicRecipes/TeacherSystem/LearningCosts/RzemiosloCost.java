package me.Vark123.EpicRecipes.TeacherSystem.LearningCosts;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.PlayerManager;
import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRPG.Players.Components.RpgRzemiosla;
import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.TeacherSystem.LearnCost;
import me.Vark123.EpicRecipes.Utils.BooleanMessage;

public class RzemiosloCost implements LearnCost {

	private final String rzemioslo;
	
	public RzemiosloCost(String rzemioslo) {
		this.rzemioslo = rzemioslo.toLowerCase();
	}
	
	@Override
	public BooleanMessage canBuy(Player p) {
		RpgPlayer rpg = PlayerManager.getInstance().getRpgPlayer(p);
		RpgRzemiosla rzemiosla = rpg.getRzemiosla();
		try {
			Field field = rzemiosla.getClass().getDeclaredField(rzemioslo);
			field.setAccessible(true);
			if(field.getBoolean(rzemiosla)) {
				return BooleanMessage.ALLOW();
			} else {
				return BooleanMessage.DENY(Main.inst().getPrefix()
						+" §cNie jestes nauczony odpowiedniego rzemiosla."
						+ " Nie mozesz nauczyc sie tego przepisu!");
			}
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return BooleanMessage.DENY();
		}
	}

	@Override
	public void buy(Player p) {
		return;
	}

	@Override
	public String getCostInfo(Player p) {
		return "§8Rzemioslo: §7"+StringUtils.capitalize(rzemioslo);
	}

}
