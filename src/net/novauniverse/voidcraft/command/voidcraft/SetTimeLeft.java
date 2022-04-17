package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.modules.SessionManager;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class SetTimeLeft extends NovaSubCommand {
	public SetTimeLeft() {
		super("settimeleft");

		setAllowedSenders(AllowedSenders.ALL);

		setPermissionDefaultValue(PermissionDefault.OP);
		setPermission("voidcraft.command.voidcraft.settimeleft");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Provide the time in seconds");
			return true;
		}

		try {
			int timeLeft = Integer.parseInt(args[0]);

			if (timeLeft < 0) {
				sender.sendMessage(ChatColor.RED + "Invalid time left");
				return true;
			}

			SessionManager.getInstance().setTimeLeft(timeLeft);

			sender.sendMessage(ChatColor.GREEN + "Time left set to " + timeLeft);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Please provide the time as a valid number");
		}

		return true;
	}
}