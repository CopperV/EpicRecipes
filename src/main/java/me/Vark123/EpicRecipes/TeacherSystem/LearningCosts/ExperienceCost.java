package me.Vark123.EpicRecipes.TeacherSystem.LearningCosts;

import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.PlayerSystem.EpicCraftPlayer;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;
import me.Vark123.EpicRecipes.ProfessionSystem.AProfession;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Alchemia;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Jubilerstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Kowalstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Luczarstwo;
import me.Vark123.EpicRecipes.ProfessionSystem.Impl.Platnerstwo;
import me.Vark123.EpicRecipes.TeacherSystem.LearnCost;
import me.Vark123.EpicRecipes.Utils.BooleanMessage;

public class ExperienceCost implements LearnCost {

	private Class<? extends AProfession> rzemioslo;
	private final int level;
	
	public ExperienceCost(String rzemioslo, int level) {
		this.level = level;
		switch(rzemioslo.toLowerCase()) {
			case "kowalstwo":
				this.rzemioslo = Kowalstwo.class;
				break;
			case "luczarstwo":
				this.rzemioslo = Luczarstwo.class;
				break;
			case "platnerstwo":
				this.rzemioslo = Platnerstwo.class;
				break;
			case "alchemia":
				this.rzemioslo = Alchemia.class;
				break;
			case "jubilerstwo":
				this.rzemioslo = Jubilerstwo.class;
				break;
		}
	}
	
	@Override
	public BooleanMessage canBuy(Player p) {
		if(rzemioslo == null)
			return BooleanMessage.DENY();
		EpicCraftPlayer craftPlayer = PlayerManager.get().getPlayer(p).orElseThrow();
		AProfession prof = craftPlayer.getProfessions().stream()
				.filter(locProf -> {
					return locProf.getClass().getName().equals(rzemioslo.getName());
				}).findFirst().orElseThrow();
		if (prof.getLevel() >= level)
			return BooleanMessage.ALLOW();
		return BooleanMessage.DENY(Main.inst().getPrefix()
				+" §cJestes za malo doswiadczony w tym rzemiosle."
				+ "Nie mozesz nauczyc sie tego przepisu.");
	}

	@Override
	public void buy(Player p) {
		return;
	}

	@Override
	public String getCostInfo(Player p) {
		if(rzemioslo == null)
			return "";
		return "§8Doswiadczenie: §7"+level;
	}

}
