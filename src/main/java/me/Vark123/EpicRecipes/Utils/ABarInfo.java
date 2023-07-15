package me.Vark123.EpicRecipes.Utils;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import lombok.Getter;
import me.Vark123.EpicRecipes.Main;

@Getter
public abstract class ABarInfo {

	private BossBar bar;
	private BukkitTask barTask;
	
	public ABarInfo(Player p) {
		this.bar = Bukkit.createBossBar(" ", BarColor.YELLOW, BarStyle.SOLID);
		this.bar.setVisible(false);
		bar.addPlayer(p);
	}
	
	protected void showInfo(String title, double min, double max, int duration) {
		
		double percent = min/max;
		if(percent < 0)
			percent = 0;
		else if(percent > 1)
			percent = 1;
		title += " §r- §e§o"+String.format("%.2f", (percent*100))+"%";
		
		if(barTask != null
				&& !barTask.isCancelled())
			barTask.cancel();
		
		bar.setTitle(title);
		bar.setProgress(percent);
		bar.setVisible(true);
		
		barTask = new BukkitRunnable() {
			@Override
			public void run() {
				bar.setVisible(false);
			}
		}.runTaskLaterAsynchronously(Main.inst(), 20*duration);
		
	}
	
}
