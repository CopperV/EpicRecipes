package me.Vark123.EpicRecipes.ProfessionSystem;

import java.util.Optional;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.Vark123.EpicRecipes.Utils.ABarInfo;

@Getter
public abstract class AProfession extends ABarInfo {

	private String id;
	private String display;
	@Setter
	private int level;
	@Setter
	private int progress;
	private Player p;

	public AProfession(Player p, String id, String display, int level, int progress) {
		super(p);
		this.id = id;
		this.display = display;
		this.level = level;
		this.progress = progress;
		this.p = p;
	}
	
	public abstract void print(Player p);
	public void addProgress(int progress) {
		Optional<ProfessionLevel> oProf = ProfessionLevel.getById(getLevel());
		if(oProf.isEmpty())
			return;
		Optional<ProfessionLevel> oNextProf = ProfessionLevel.getById(getLevel()+1);
		if(oNextProf.isEmpty())
			return;
		
		Player p = getP();
		ProfessionLevel profLevel = oProf.get();
		ProfessionLevel nextProfLevel = oNextProf.get();
		setProgress(getProgress()+progress);
		//LEVEL UPDATE!
		if(getProgress() >= nextProfLevel.getAmount()) {
			setLevel(getLevel()+1);
			
			profLevel = nextProfLevel;
			oNextProf = ProfessionLevel.getById(getLevel()+1);
			
			if(oNextProf.isEmpty()) {
				nextProfLevel = null;
			} else {
				nextProfLevel = oNextProf.get();
			}
			
			if(profLevel.getId() == 12) {
				setProgress(profLevel.getAmount());
			}
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 0.75f);
			p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, p.getEyeLocation(), 10, 0.1f, 0.1f, 0.1f, 0.05f);
		} else
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.6f);
		
		String title = display+" §a"+profLevel.getDisplay()+" §7[§r"+profLevel.getId()+"§7]";
		double barMin = 1;
		double barMax = 1;
		if(profLevel.getId() < 12) {
			barMin = getProgress() - profLevel.getAmount();
			barMax = nextProfLevel.getAmount() - profLevel.getAmount();
		}
		showInfo(title, barMin, barMax, 3);
		
	}
	
}
