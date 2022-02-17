package net.novauniverse.voidcraft.playerdata;

import java.util.UUID;

public class PlayerData {
	private UUID uuid;
	private int lives;
	private boolean protection;

	public PlayerData(UUID uuid) {
		this(uuid, 5, true);
	}

	public PlayerData(UUID uuid, int lives, boolean protection) {
		this.uuid = uuid;
		this.lives = lives;
		this.protection = protection;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getLives() {
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}

	public boolean isProtected() {
		return protection;
	}
	
	public void setProtected(boolean protection) {
		this.protection = protection;
	}
	
	public void save() {
		PlayerDataManager.getInstance().savePlayerData(this);
	}
}