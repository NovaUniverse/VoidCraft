package net.novauniverse.voidcraft.command.voidcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.customitems.VoidCraftLootBox;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.novacore.spigot.module.modules.customitems.CustomItemManager;

public class GiveLootBox extends NovaSubCommand {
	public GiveLootBox() {
		super("givelootbox");

		setAllowedSenders(AllowedSenders.ALL);

		setPermissionDefaultValue(PermissionDefault.OP);
		setPermission("voidcraft.command.voidcraft.givelootbox");

		setFilterAutocomplete(true);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Please provide a player");
			return true;
		}

		Player player = Bukkit.getServer().getPlayer(args[0]);

		if (player != null) {
			if (player.isOnline()) {
				ItemStack item = CustomItemManager.getInstance().getCustomItemStack(VoidCraftLootBox.class, player);
				if (player.getInventory().addItem(item).size() == 0) {
					sender.sendMessage(ChatColor.GREEN + "Added 1 loot box to " + player.getName() + "s inventory");
				} else {
					sender.sendMessage(ChatColor.RED + "That player does not have enough space in their inventory for a loot box");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "That player is not online");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Cant find player " + args[0]);
		}
		return true;
	}
}