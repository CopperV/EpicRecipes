package me.Vark123.EpicRecipes.MenuSystem.Creators;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Vark123.EpicRecipes.Main;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeGroupManager;
import me.Vark123.EpicRecipes.RecipeSystem.RecipeManager;

public class RecipeCreateCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("recipe"))
			return false;
		if(!(sender instanceof Player)) {
			sender.sendMessage(Main.inst().getPrefix()+" §cKomenda tylko dla graczy");
			return false;
		}
		if(!sender.hasPermission("epicrecipes.admin")) {
			sender.sendMessage(Main.inst().getPrefix()+" §cNie posiadasz uprawnien do tej komendy");
			return false;
		}
		
		Player p = (Player) sender;
		if(args.length == 0) {
			showCorrectUsage(sender);
			return false;
		}
		
		switch(args[0].toLowerCase()) {
			case "dodaj":
				if(args.length < 4) {
					showCorrectUsage(sender);
					return false;
				}
				
				String craftType = args[1].toLowerCase();
				if(!(craftType.equals("shaped") || craftType.equals("shapeless"))) {
					showCorrectUsage(sender);
					return false;
				}
				
				String group = args[2];
				if(RecipeGroupManager.get().getGroupContainer().stream()
						.filter(gr -> gr.getGroupId().equals(group))
						.findAny()
						.isEmpty()) {
					p.sendMessage(Main.inst().getPrefix()+" §cPodana grupa nie istnieje");
					return false;
				}
				
				String id = args[3];
				if(RecipeManager.get().getRecipeContainer().containsKey(id)) {
					p.sendMessage(Main.inst().getPrefix()+" §cPrzepis o takim id juz istnieje.");
					p.sendMessage("§cSprobuj uzyc komendy §c§o/przepis zmien <id>");
					return false;
				}
				
				RecipeCreateManager.get().openMenu(p, craftType, group, id);
				break;
			case "usun":
				break;
			case "zmien":
				break;
			default:
				showCorrectUsage(sender);
				return false;
		}
		
		
		return true;
	}
	
	private void showCorrectUsage(CommandSender sender) {
		sender.sendMessage(Main.inst().getPrefix()+" §cPoprawne uzycie komendy §c§oprzepis§c:");
		sender.sendMessage("§4> §c§o/przepis dodaj <shapeless/shaped> <group> id");
		sender.sendMessage("§4> §c/przepis usun <id>");
		sender.sendMessage("§4> §c/przepis zmien <id>");
	}
	
}
