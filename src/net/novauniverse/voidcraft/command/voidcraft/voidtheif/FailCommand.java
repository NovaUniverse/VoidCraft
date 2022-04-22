package net.novauniverse.voidcraft.command.voidcraft.voidtheif;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.modules.SessionManager;
import net.novauniverse.voidcraft.modules.VoidCraftManager;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependantUtils;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class FailCommand extends NovaSubCommand {
	public FailCommand() {
		super("fail");

		setAllowedSenders(AllowedSenders.ALL);
		setPermission("voidcraft.command.voidtheif.fail");
		setPermissionDefaultValue(PermissionDefault.OP);
		setEmptyTabMode(true);
		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (SessionManager.getInstance().getVoidthief() == null) {
			sender.sendMessage(ChatColor.RED + "There is no active voidtheif right now");
		} else {
			Player player = Bukkit.getServer().getPlayer(SessionManager.getInstance().getVoidthief());
			if (player != null) {
				VersionIndependantUtils.get().sendTitle(player, ChatColor.RED + "Failed", ChatColor.RED + "You did not manage to kill a player", 10, 60, 10);
				VersionIndependantUtils.get().sendActionBarMessage(player, ChatColor.RED + "Failed");
				player.playSound(player.getLocation(), Sound.ENTITY_WITHER_HURT, 1F, 1F);
				PlayerData data = PlayerDataManager.getInstance().getData(player);
				if (data.getLives() <= 2) {
					data.setLives(1);
					data.save();
					player.setGameMode(GameMode.SPECTATOR);
					player.setHealth(0);
				} else {
					data.setLives(data.getLives() - 2);
					data.save();
				}
				VoidCraftManager.getInstance().updatePlayer(player);
				data.save();
			}
			SessionManager.getInstance().setVoidTheif(null);
			sender.sendMessage(ChatColor.GREEN + "Voidtheif cured");
		}
		return true;
	}
}