package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.modules.SessionManager;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class EndSession extends NovaSubCommand {
	public EndSession() {
		super("endsession");

		setAllowedSenders(AllowedSenders.ALL);

		setPermissionDefaultValue(PermissionDefault.OP);
		setPermission("voidcraft.command.voidcraft.endsession");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (SessionManager.getInstance().getTimeLeft() < 301) {
			sender.sendMessage(ChatColor.RED + "Cant end session since there is less than 5 minutes left");
			return true;
		}

		SessionManager.getInstance().setTimeLeft(301);
		sender.sendMessage(ChatColor.GREEN + "Set time left to 5 minutes");
		return true;
	}
}