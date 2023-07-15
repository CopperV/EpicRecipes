package me.Vark123.EpicRecipes.ProfessionSystem.Impl;

import java.util.Optional;

import org.bukkit.entity.Player;

import me.Vark123.EpicRPG.Players.RpgPlayer;
import me.Vark123.EpicRecipes.ProfessionSystem.AProfession;
import me.Vark123.EpicRecipes.ProfessionSystem.ProfessionLevel;

public class Luczarstwo extends AProfession {

	
	
	public Luczarstwo(Player p, int level, int progress) {
		super(p, "luczarstwo", "§e§lLuczarstwo", level, progress);
	}

	@Override
	public void print(Player p) {
		RpgPlayer rpg = me.Vark123.EpicRPG.Players.PlayerManager.getInstance().getRpgPlayer(p);
		if(!rpg.getRzemiosla().hasLuczarstwo()) {
			p.sendMessage("    "+getDisplay()+"§7: §eNienauczony");
			return;
		}
		Optional<ProfessionLevel> oProf = ProfessionLevel.getById(getLevel());
		if(oProf.isEmpty())
			return;
		ProfessionLevel prof = oProf.get();
		double min = prof.getAmount();
//		int max = Integer.MAX_VALUE;
		double present = getProgress() - min;
		StringBuilder percent = new StringBuilder();
		ProfessionLevel.getById(getLevel()+1)
			.ifPresentOrElse(nextProf -> {
				double max = nextProf.getAmount() - min;
				double perc = (present/max) * 100.0;
				percent.append("§e§o"+String.format("%.2f", perc)+"%");
			}, () -> {
				percent.append("§e§oNaN");
			});
		p.sendMessage("    "+getDisplay()+"§7: §e"+prof.getDisplay()+" §7[§e"+prof.getId()+"§7]");
		p.sendMessage("        §2Doswiadczenie: §e"+percent.toString());
	}

	@Override
	public void addProgress(int progress) {
		
	}
	
}
