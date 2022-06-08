package net.novauniverse.voidcraft.command.voidcraft.voidtheif;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.modules.SessionManager;
import net.novauniverse.voidcraft.modules.VoidCraftManager;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependentUtils;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;

public class CureCommand extends NovaSubCommand {
	public CureCommand() {
		super("cure");

		setAllowedSenders(AllowedSenders.ALL);
		setPermission("voidcraft.command.voidthief.cure");
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
				VersionIndependentUtils.get().sendTitle(player, ChatColor.GREEN + "Cured", ChatColor.GREEN + "You lost the voidthief status", 10, 60, 10);
				VersionIndependentUtils.get().sendActionBarMessage(player, ChatColor.GREEN + "Cured");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
				PlayerData data = PlayerDataManager.getInstance().getData(player);
				data.setLives(data.getLives() + 1);
				data.save();
				VoidCraftManager.getInstance().updatePlayer(player);
			}
			SessionManager.getInstance().setVoidTheif(null);
			sender.sendMessage(ChatColor.GREEN + "Voidthief cured");
		}
		return true;
	}
}