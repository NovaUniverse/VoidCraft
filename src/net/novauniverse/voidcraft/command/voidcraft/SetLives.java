package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.modules.VoidCraftManager;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class SetLives extends NovaSubCommand {
	public SetLives() {
		super("setlives");

		setAllowedSenders(AllowedSenders.ALL);

		setPermissionDefaultValue(PermissionDefault.OP);
		setPermission("voidcraft.command.voidcraft.setlives");

		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Provide the player and lives");
			return true;
		}

		Player player = Bukkit.getServer().getPlayer(args[0]);
		if (player != null) {
			PlayerData data = PlayerDataManager.getInstance().getData(player);

			try {
				int lives = Integer.parseInt(args[1]);

				if (lives < 0) {
					sender.sendMessage(ChatColor.RED + "Invalid amount of lives");
					return true;
				}

				data.setLives(lives);
				data.save();
				VoidCraftManager.getInstance().updatePlayer(player);

				sender.sendMessage(ChatColor.GREEN + "Ok");
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Please provide the lives as a valid number");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Cant find player " + args[0]);
		}
		return true;
	}
}