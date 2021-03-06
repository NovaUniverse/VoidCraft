package net.novauniverse.voidcraft.command.voidcraft.voidtheif;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.VoidCraft;
import net.novauniverse.voidcraft.modules.SessionManager;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;

public class VoidThiefCommand extends NovaCommand {
	public VoidThiefCommand() {
		super("voidthief", VoidCraft.getInstance());

		addSubCommand(new FailCommand());
		addSubCommand(new CureCommand());

		addHelpSubCommand();

		setAllowedSenders(AllowedSenders.ALL);
		setPermission("voidcraft.command.voidthief.fail");
		setPermissionDefaultValue(PermissionDefault.OP);
		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (SessionManager.getInstance().getVoidthief() == null) {
			sender.sendMessage(ChatColor.RED + "No active voidthief");
		} else {
			OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(SessionManager.getInstance().getVoidthief());
			sender.sendMessage(ChatColor.GREEN + player.getName() + " is the voidthief");
		}
		return true;
	}
}