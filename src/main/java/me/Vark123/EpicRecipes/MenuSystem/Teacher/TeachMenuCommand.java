package me.Vark123.EpicRecipes.MenuSystem.Teacher;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.TeacherSystem.TeachManager;
import me.Vark123.EpicRecipes.TeacherSystem.Teacher;

public class TeachMenuCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("teacher"))
			return false;
		if(!sender.hasPermission("epicrecipes.admin")) {
			sender.sendMessage(Main.inst().getPrefix()+" §cNie posiadasz uprawnien do tej komendy");
			return false;
		}
		if(args.length < 2) {
			showCorrectUsage(sender);
			return false;
		}
		if(Bukkit.getPlayerExact(args[0])==null) {
			sender.sendMessage(Main.inst().getPrefix()+" §cGracz §a§o"+args[0]+" §cjest offline");
			return false;
		}
		Player p  = Bukkit.getPlayerExact(args[0]);
		String id = args[1];
		Teacher teacher = TeachManager.get().getTeachers().get(id);
		if(teacher == null) {
			showCorrectUsage(sender);
			return false;
		}
		TeachMenuManager.get().openMenu(p, teacher);
		return true;
	}

	private void showCorrectUsage(CommandSender sender) {
		sender.sendMessage(Main.inst().getPrefix()+" §cPoprawne uzycie komendy §c§oteacher§c:");
		sender.sendMessage("§4> §c§o/teacher nick id");
	}
}
