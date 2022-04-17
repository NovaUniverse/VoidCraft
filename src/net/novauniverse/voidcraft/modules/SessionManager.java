package net.novauniverse.voidcraft.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.novauniverse.voidcraft.VoidCraft;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.commons.utils.TextUtils;
import net.zeeraa.novacore.spigot.abstraction.VersionIndependantUtils;
import net.zeeraa.novacore.spigot.abstraction.enums.VersionIndependantSound;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.NetherBoardScoreboard;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class SessionManager extends NovaModule {
	private static SessionManager instance;

	private Task countdownTask;
	private int timeLeft;

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
		countdownTask = new SimpleTask(VoidCraft.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (timeLeft > 0) {
					timeLeft--;
				}
				
				if(timeLeft == 600) {
					Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.AQUA + "10 Minutes left");
				}
				
				updateScoreboard();
			}
		}, 20L);
	}

	@Override
	public void onEnable() throws Exception {
		timeLeft = 5400;
		updateScoreboard();

	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(countdownTask);
	}

	public void updateScoreboard() {
		if (timeLeft > 0) {
			NetherBoardScoreboard.getInstance().setGlobalLine(1, ChatColor.GOLD + "Time left: " + ChatColor.AQUA + TextUtils.formatTimeToHMS(timeLeft));
		} else {
			NetherBoardScoreboard.getInstance().setGlobalLine(1, ChatColor.RED + "Session not active");
		}
	}

	public void startSession() {
		this.setTimeLeft(5400);
		this.updateScoreboard();
		
		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "" + ChatColor.AQUA + "Session started");
		Bukkit.getServer().getOnlinePlayers().forEach(player -> VersionIndependantUtils.get().playSound(player, player.getLocation(), VersionIndependantSound.NOTE_PLING));
	}
}