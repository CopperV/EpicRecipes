package me.Vark123.EpicRecipes.Utils;

public class Utils {

	private Utils() {}
	
	public static int mapInvToCraft(int slot) {
		int row = slot/9;
		int num = slot%9;
		return row*5 + num;
	}
	
	public static int mapCraftToInv(int slot) {
		int row = slot/5;
		int num = slot%5;
		return row*9 + num;
	}
	
}
