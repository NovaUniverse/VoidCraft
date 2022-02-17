package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.modules.SessionManager;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class StartSession extends NovaSubCommand {
	public StartSession() {
		super("startsession");
		
		setAllowedSenders(AllowedSenders.ALL);

		setPermissionDefaultValue(PermissionDefault.OP);
		setPermission("voidcraft.command.voidcraft.startsession");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			SessionManager.getInstance().startSession();
		
		return true;
	}
}