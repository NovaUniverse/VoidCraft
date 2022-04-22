package net.novauniverse.voidcraft.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.novauniverse.voidcraft.VoidCraft;
import net.novauniverse.voidcraft.customitems.VoidCraftLootBox;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.commons.utils.TextUtils;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependantUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependantSound;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.customitems.CustomItemManager;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.NetherBoardScoreboard;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class SessionManager extends NovaModule {
	public static final int SESSION_LENGTH = 5400;

	private static SessionManager instance;

	private Task countdownTask;
	private int timeLeft;
	private boolean sessionActive;

	private Random random;

	private UUID voidthief;

	public static SessionManager getInstance() {
		return instance;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public SessionManager() {
		super("VoidCraft.SessionManager");
	}

	@Override
	public void onLoad() {
		SessionManager.instance = this;
		random = new Random();
		sessionActive = false;
		countdownTask = new SimpleTask(VoidCraft.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (timeLeft > 0) {
					timeLeft--;
				} else {
					sessionActive = false;
					Task.tryStopTask(countdownTask);
					updateScoreboard();
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "" + ChatColor.AQUA + "Session ended");
					Bukkit.getServer().getOnlinePlayers().forEach(player -> VersionIndependantSound.NOTE_PLING.play(player));
					return;
				}

				if (voidthief != null) {
					Player voidtheifPlayer = Bukkit.getPlayer(voidthief);
					if (voidtheifPlayer != null) {
						TextComponent component = new TextComponent("Voidtheif");
						component.setColor(ChatColor.of("#063931"));
						voidtheifPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
					}
				}

				if (timeLeft == (SessionManager.SESSION_LENGTH - (10 * 60))) {
					List<Player> possiblePlayers = new ArrayList<Player>();
					Bukkit.getServer().getOnlinePlayers().forEach(player -> {
						if (player.getGameMode() != GameMode.SPECTATOR) {
							possiblePlayers.add(player);
						}
					});

					if (possiblePlayers.size() > 0) {
						setVoidTheif(possiblePlayers.get(random.nextInt(possiblePlayers.size())));
					} else {
						Log.warn("SessionManager", "Cant select voidthief since there are no online players that are not spectators");
					}
				}

				if (timeLeft == 60 * 10) {
					Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.AQUA + "10 Minutes left");
					Bukkit.getServer().getOnlinePlayers().forEach(player -> VersionIndependantSound.NOTE_PLING.play(player));
				}

				if (timeLeft == 60 * 5) {
					Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.AQUA + "5 Minutes left");
					Bukkit.getServer().getOnlinePlayers().forEach(player -> {
						VersionIndependantSound.NOTE_PLING.play(player);
						if (player.getGameMode() != GameMode.SPECTATOR) {
							ItemStack item = CustomItemManager.getInstance().getCustomItemStack(VoidCraftLootBox.class, player);
							if (player.getInventory().addItem(item).size() > 0) {
								player.sendMessage(ChatColor.RED + "You did not have enough space in your inventory for your loot box. Please contace the admins to get your loot box");
							}
						}
					});
				}

				updateScoreboard();
			}
		}, 20L);
	}

	public boolean isSessionActive() {
		return sessionActive;
	}

	public void setVoidTheif(@Nullable Player player) {
		if (player == null) {
			this.voidthief = null;
			return;
		}

		this.voidthief = player.getUniqueId();
		player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 2F, 0.5F);
		VersionIndependantUtils.get().sendTitle(player, ChatColor.RED + "Voidthief", ChatColor.RED + "You became the voidthief", 10, 60, 10);
	}

	public UUID getVoidthief() {
		return voidthief;
	}

	@Override
	public void onEnable() throws Exception {
		timeLeft = SessionManager.SESSION_LENGTH;
		updateScoreboard();

	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(countdownTask);
	}

	public void updateScoreboard() {
		if (sessionActive) {
			//Log.trace("timeLeft: " + timeLeft);
			NetherBoardScoreboard.getInstance().setGlobalLine(1, ChatColor.GOLD + "Time left: " + ChatColor.AQUA + TextUtils.formatTimeToHMS(timeLeft));
		} else {
			NetherBoardScoreboard.getInstance().setGlobalLine(1, ChatColor.RED + "Session not active");
		}
	}

	public void startSession() {
		this.setTimeLeft(SessionManager.SESSION_LENGTH);
		this.updateScoreboard();

		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "" + ChatColor.AQUA + "Session started");
		Bukkit.getServer().getOnlinePlayers().forEach(player -> VersionIndependantSound.NOTE_PLING.play(player));

		sessionActive = true;
		Task.tryStartTask(countdownTask);
	}
}