package net.novauniverse.voidcraft.misc;

import org.bukkit.ChatColor;

public class VoidCraftUtils {
	public static ChatColor getPlayerColor(int lives) {
		switch (lives) {
		case 5:
			return ChatColor.DARK_BLUE;
			
		case 4:
			return ChatColor.AQUA;
			
		case 3:
			return ChatColor.GREEN;
			
		case 2:
			return ChatColor.YELLOW;

		case 1:
			return ChatColor.RED;

		default:
			return ChatColor.GRAY;
		}
	}
}