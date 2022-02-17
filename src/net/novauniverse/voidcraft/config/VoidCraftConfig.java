package net.novauniverse.voidcraft.config;

import org.bukkit.configuration.file.FileConfiguration;

public class VoidCraftConfig {
	private FileConfiguration config;

	public VoidCraftConfig(FileConfiguration config) {
		this.config = config;
	}
	
	public boolean shouldMessagePlayer() {
		return config.getBoolean("message_player");
	}
	
	public boolean isRedProtection() {
		return config.getBoolean("protect_red_players");
	}

	public boolean isAllowTransferLife() {
		return config.getBoolean("allow_transfer_life");
	}

	public boolean isAllowRevive() {
		return config.getBoolean("allow_revive");
	}
}