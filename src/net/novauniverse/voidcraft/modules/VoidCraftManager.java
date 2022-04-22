package net.novauniverse.voidcraft.modules;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.novauniverse.voidcraft.VoidCraft;
import net.novauniverse.voidcraft.misc.VoidCraftUtils;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.NetherBoardScoreboard;

public class VoidCraftManager extends NovaModule implements Listener {
	private static VoidCraftManager instance;

	public static VoidCraftManager getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		VoidCraftManager.instance = this;
	}

	public VoidCraftManager() {
		super("VoidCraft.VoidCraftManager");
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		this.updatePlayer(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerRespawnEvent e) {
		PlayerData data = PlayerDataManager.getInstance().getData(e.getPlayer());
		if (data.getLives() == 0) {
			new BukkitRunnable() {
				@Override
				public void run() {
					e.getPlayer().setGameMode(GameMode.SPECTATOR);
				}
			}.runTaskLater(VoidCraft.getInstance(), 1L);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (!SessionManager.getInstance().isSessionActive()) {
			return;
		}

		Player player = e.getEntity();

		PlayerData data = PlayerDataManager.getInstance().getData(player);

		if (data.getLives() > 0) {
			data.setLives(data.getLives() - 1);
		}

		if (VoidCraft.getInstance().getVoidCraftConfig().shouldMessagePlayer()) {
			switch (data.getLives()) {
			case 2:
				player.sendMessage(ChatColor.YELLOW + "You are now a yellow player");
				break;

			case 1:
				player.sendMessage(ChatColor.RED + "You are now a red player");
				break;

			case 0:
				player.sendMessage(ChatColor.GRAY + "You are now a spectator");
				player.setGameMode(GameMode.SPECTATOR);
				break;

			default:
				break;
			}
		}

		data.save();

		this.updatePlayer(e.getEntity());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (!SessionManager.getInstance().isSessionActive()) {
				e.setCancelled(true);
			}
		}
	}

	public void updatePlayer(Player player) {
		PlayerData data = PlayerDataManager.getInstance().getData(player);

		ChatColor color = VoidCraftUtils.getPlayerColor(data.getLives());

		// NetherBoardScoreboard.getInstance().setPlayerNameColor(player, color);
		NetherBoardScoreboard.getInstance().setPlayerLine(0, player, ChatColor.GOLD + "Lives: " + color + "" + data.getLives());

		String newName = color + player.getName();

		player.setDisplayName(newName);
		player.setCustomName(newName);
		player.setPlayerListName(newName);

		player.setCustomNameVisible(true);
	}
}