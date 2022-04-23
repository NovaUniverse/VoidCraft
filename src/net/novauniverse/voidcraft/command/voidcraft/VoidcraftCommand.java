package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import net.novauniverse.voidcraft.VoidCraft;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;

public class VoidcraftCommand extends NovaCommand {
	public VoidcraftCommand() {
		super("voidcraft", VoidCraft.getInstance());

		setAllowedSenders(AllowedSenders.ALL);

		setPermissionDefaultValue(PermissionDefault.OP);
		setPermission("voidcraft.command.voidcraft");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);

		addSubCommand(new StartSession());
		addSubCommand(new EndSession());
		addSubCommand(new SetTimeLeft());
		addSubCommand(new GiveLootBox());
		addSubCommand(new SetLives());

		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Use " + ChatColor.AQUA + "/voidcraft help " + ChatColor.GOLD + "for help");
		return true;
	}
}