package me.Vark123.EpicRecipes.ProfessionSystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.PlayerSystem.PlayerManager;

public class ProfessionCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("profession"))
			return false;
		if(!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		if(args.length > 0) {
			if(sender.hasPermission("epicrecipes.crafting"));
				if(Bukkit.getPlayerExact(args[0]) != null)
					p = Bukkit.getPlayerExact(args[0]);
		}
		Player player = p;
		PlayerManager.get().getPlayer(p).ifPresent(craftPlayer -> {
			sender.sendMessage(Main.inst().getPrefix()+" §aStan rzemiosl gracza "+player.getName());
			craftPlayer.getProfessions().forEach(prof -> {
				prof.print((Player) sender);
			});
		});
		return true;
	}

}
