package net.novauniverse.voidcraft.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.novauniverse.voidcraft.VoidCraft;
import net.novauniverse.voidcraft.modules.VoidCraftManager;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.novacore.spigot.utils.PlayerUtils;

public class TransferLifeCommand extends NovaCommand {
	public TransferLifeCommand() {
		super("transferlife", VoidCraft.getInstance());

		setAllowedSenders(AllowedSenders.PLAYERS);

		setPermission("thirdlife.command.transferlife");
		setPermissionDefaultValue(PermissionDefault.TRUE);
		setDescription("Trasfer one of your lives to another player");
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player senderPlayer = (Player) sender;
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Useage: /transferlife <Player>");
		} else {
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if (target != null) {
				if (target.isOnline()) {
					PlayerData myData = PlayerDataManager.getInstance().getData(senderPlayer);

					if (myData.getLives() > 0) {
						PlayerData targetData = PlayerDataManager.getInstance().getData(target);

						boolean targetDead = targetData.getLives() == 0;

						if ((targetDead && VoidCraft.getInstance().getVoidCraftConfig().isAllowRevive()) || !targetDead) {
							boolean confirmed = false;

							if (args.length == 2) {
								if (args[1].equalsIgnoreCase("confirm")) {
									confirmed = true;
								}
							}

							if (confirmed) {
								targetData.setLives(targetData.getLives() + 1);
								targetData.save();

								VoidCraftManager.getInstance().updatePlayer(target);

								senderPlayer.setHealth(0);
								Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.AQUA + senderPlayer.getName() + ChatColor.BOLD + ChatColor.GOLD + " transfered a life to " + ChatColor.BOLD + ChatColor.AQUA + target.getName());

								if (targetDead) {
									target.teleport(target.getWorld().getSpawnLocation());
									target.setGameMode(GameMode.SURVIVAL);
									target.setHealth(PlayerUtils.getPlayerMaxHealth(target));
									target.setFoodLevel(20);
									target.setSaturation(20);
									target.setFireTicks(0);
									target.sendMessage(ChatColor.GREEN + "You have been respawned");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "You are about to transfer a life to " + target.getName() + ". Please run" + ChatColor.AQUA + " /transferlife " + target.getName() + " confirm" + ChatColor.RED + " to confirm this action. Running this command will kill you");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "You cant transfer lives to a dead player");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "You dont have any lives left");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "That player is not online");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Could not find player " + args[0]);
			}
		}

		return true;
	}
}