package net.novauniverse.voidcraft.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import net.novauniverse.voidcraft.lootbox.LootBoxContent;
import net.zeeraa.novacore.spigot.module.modules.customitems.CustomItem;
import net.zeeraa.novacore.spigot.utils.ItemBuilder;
import net.zeeraa.novacore.spigot.utils.ItemUtils;

public class VoidCraftLootBox extends CustomItem {
	@Override
	protected ItemStack createItemStack(Player player) {
		ItemBuilder builder = new ItemBuilder(ItemBuilder.getPlayerSkullWithBase64Texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ3NjFjYzE2NTYyYzg4ZDJmYmU0MGFkMzg1MDJiYzNiNGE4Nzg1OTg4N2RiYzM1ZjI3MmUzMGQ4MDcwZWVlYyJ9fX0="));

		builder.setAmount(1);
		builder.setName(ChatColor.AQUA + "VoidCraft Loot box");
		builder.addLore(ChatColor.WHITE + "Right click to open.");
		builder.addLore(ChatColor.WHITE + "The items will drop on the ground.");

		return builder.build();
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			event.setCancelled(true);
			if (event.getHand() == EquipmentSlot.HAND) {
				ItemUtils.removeOneFromHand(event.getPlayer());

				event.getPlayer().getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);

				LootBoxContent.getLootBoxContent().forEach(item -> {
					player.getWorld().dropItem(player.getLocation(), item.clone());
				});
			}
		}
	}
}