package me.Vark123.EpicRecipes.MenuSystem.RecipeList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RecipeListCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("recipes"))
			return false;
		if(!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		RecipeListManager.get().openMenu(p);
		return true;
	}

}
