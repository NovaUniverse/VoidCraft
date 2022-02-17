package net.novauniverse.voidcraft.misc;

import org.bukkit.ChatColor;

public class VoidCraftUtils {
	public static ChatColor getPlayerColor(int lives) {
		switch (lives) {
		case 4:

			return ChatColor.AQUA;
		case 3:

			return ChatColor.YELLOW;
		case 2:

			return ChatColor.RED;
		case 1:

			return ChatColor.GRAY;

		case 0:

			return ChatColor.DARK_BLUE;

		default:
			return ChatColor.DARK_BLUE;
		}
	}
}