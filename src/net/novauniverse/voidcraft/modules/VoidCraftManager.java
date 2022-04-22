package net.novauniverse.voidcraft.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import net.novauniverse.voidcraft.VoidCraft;
import net.novauniverse.voidcraft.misc.VoidCraftUtils;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.NetherBoardScoreboard;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class VoidCraftManager extends NovaModule implements Listener {
	private static VoidCraftManager instance;

	private Map<UUID, Hologram> holograms;

	private Task hologramTask;

	public static VoidCraftManager getInstance() {
		return instance;
	}

	@Override
	public void onLoad() {
		VoidCraftManager.instance = this;
		holograms = new HashMap<>();
		hologramTask = new SimpleTask(new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().getOnlinePlayers().forEach(player -> {
					if (holograms.containsKey(player.getUniqueId())) {
						Hologram hologram = holograms.get(player.getUniqueId());

						if (player.getGameMode() == GameMode.SPECTATOR || player.isSneaking()) {
							hologram.getVisibilityManager().setVisibleByDefault(false);
						} else {
							Location location = player.getLocation().clone().add(0, 2.6, 0);
							hologram.teleport(location);
							hologram.getVisibilityManager().setVisibleByDefault(true);
						}

					}
				});
			}
		}, 0L);
	}

	@Override
	public void onEnable() throws Exception {
		Task.tryStartTask(hologramTask);
	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(hologramTask);
		holograms.values().forEach(hologram -> hologram.delete());
		holograms.clear();
	}

	public VoidCraftManager() {
		super("VoidCraft.VoidCraftManager");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		Hologram hologram = HologramsAPI.createHologram(VoidCraft.getInstance(), player.getLocation());
		hologram.insertTextLine(0, "TEMP");
		hologram.getVisibilityManager().hideTo(player);
		holograms.put(player.getUniqueId(), hologram);

		this.updatePlayer(player);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();

		if (holograms.containsKey(player.getUniqueId())) {
			holograms.get(player.getUniqueId()).delete();
			holograms.remove(player.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
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

		if (holograms.containsKey(player.getUniqueId())) {
			Hologram hologram = holograms.get(player.getUniqueId());
			((TextLine) hologram.getLine(0)).setText(color + "" + data.getLives() + (data.getLives() == 1 ? " life" : " lives"));
		}

		// NetherBoardScoreboard.getInstance().setPlayerNameColor(player, color);
		NetherBoardScoreboard.getInstance().setPlayerLine(0, player, ChatColor.GOLD + "Lives: " + color + "" + data.getLives());

		String newName = color + player.getName();

		player.setDisplayName(newName);
		player.setCustomName(newName);
		player.setPlayerListName(newName);

		player.setCustomNameVisible(true);
	}
}