package net.novauniverse.voidcraft.modules;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.novauniverse.voidcraft.VoidCraft;
import net.novauniverse.voidcraft.misc.VoidCraftUtils;
import net.novauniverse.voidcraft.playerdata.PlayerData;
import net.novauniverse.voidcraft.playerdata.PlayerDataManager;
import net.zeeraa.novacore.commons.utils.UUIDUtils;
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

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent e) {
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
				break;

			default:
				break;
			}
		}

		data.save();

		this.updatePlayer(e.getEntity());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (e.getEntity() instanceof Player) {
				if (SessionManager.getInstance().isSessionActive()) {
					Player player = (Player) e.getEntity();
					Player attacker = (Player) e.getDamager();

					PlayerData data = PlayerDataManager.getInstance().getData(player);
					PlayerData attackerData = PlayerDataManager.getInstance().getData(attacker);

					if (data.isRed() && !data.isProtected()) {
						// Player is at 1 life and no protection. Allow attack
						// edit: wait why the f**k did we add an empty if statement?
					}

					// Allow voidtheif to attack everyone
					if (!UUIDUtils.isSame(attacker.getUniqueId(), SessionManager.getInstance().getVoidthief())) {
						if (data.isRed() && data.isProtected()) {
							// Player is at 1 life but has protection, attacker is not a red player. Block
							// attack and message player
							attacker.sendMessage(ChatColor.RED + "You cant attack that player since they have not yet attacked anyone");
							e.setCancelled(true);
						}

						if (!attackerData.isRed() && !data.isRed()) {
							// None of the players are red. Cancel attack
							attacker.sendMessage(ChatColor.RED + "You cant attack friendly players");
							e.setCancelled(true);
						}
					}

					// Check if we should remove the protection of the attacker
					if (!e.isCancelled()) {
						if (attackerData.isRed() && attackerData.isProtected()) {
							attackerData.setProtected(false);
							attackerData.save();
							attacker.sendMessage(ChatColor.RED + "You lost your protection against other players since you attacked someone");
							attacker.playSound(attacker, Sound.ENTITY_BAT_HURT, 1F, 0.5F);
						}
					}
				} else {
					e.getDamager().sendMessage(ChatColor.RED + "Session is not active");
					e.setCancelled(true);
				}
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