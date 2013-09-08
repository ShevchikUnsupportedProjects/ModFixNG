/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 */

package modfix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;

// Villagers fix
public class MFVillagerFixListener implements Listener {

	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;

	MFVillagerFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}

	//restrict shift-click
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void VillagerIncClickEvent(InventoryClickEvent event) {
		if (config.enableVillagersFix) {
			if (event.getView().getTopInventory() != null && event.getView().getTopInventory().getType().equals(InventoryType.MERCHANT)) {
				if (event.isShiftClick()) {
					if (event.getSlotType().equals(SlotType.RESULT) && event.getCurrentItem().getType() != Material.EMERALD) {
						event.setCancelled(true);
						event.getWhoClicked().closeInventory();
						Bukkit.getPlayerExact(event.getWhoClicked().getName()).sendMessage(ChatColor.RED+ "Запрещено покупать у жителей за изумруды shift-кликом");
					}
				}
			}
		}
	}
}
