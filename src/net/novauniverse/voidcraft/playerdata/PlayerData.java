package net.novauniverse.voidcraft.playerdata;

import java.util.UUID;

public class PlayerData {
	private UUID uuid;
	private int lives;

	public PlayerData(UUID uuid) {
		this(uuid, 5);
	}

	public PlayerData(UUID uuid, int lives) {
		this.uuid = uuid;
		this.lives = lives;
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

	public boolean isRed() {
		return this.lives == 1;
	}

	public void save() {
		PlayerDataManager.getInstance().savePlayerData(this);
	}
}