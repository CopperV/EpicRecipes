package me.Vark123.EpicRecipes.CraftingSystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.MenuSystem.Crafting.CraftingInvManager;

public class CraftingCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("crafting"))
			return false;
		if(!(sender instanceof Player))
			return false;
		if(!sender.hasPermission("epicrecipes.crafting"))
			return false;
		Player p = (Player) sender;
		CraftingInvManager.get().openMenu(p);
		return true;
	}

}
