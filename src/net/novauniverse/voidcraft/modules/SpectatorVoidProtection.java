package net.novauniverse.voidcraft.modules;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;

import net.novauniverse.voidcraft.VoidCraft;
import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class SpectatorVoidProtection extends NovaModule {
	private Task task;

	public SpectatorVoidProtection() {
		super("VoidCraft.SpectatorVoidProtection");
	}

	@Override
	public void onLoad() {
		task = new SimpleTask(VoidCraft.getInstance(), new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().getOnlinePlayers().forEach(player -> {
					if (player.getGameMode() == GameMode.SPECTATOR) {
						Location location = player.getLocation();

						if (location.getY() < -70) {
							location.setY(-64);
							player.teleport(location);
						}
					}
				});
			}
		}, 10L);
	}

	@Override
	public void onEnable() throws Exception {
		Task.tryStartTask(task);
	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(task);
	}
}