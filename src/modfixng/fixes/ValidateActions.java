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

package modfixng.fixes;

import modfixng.events.BlockDigPacketItemDropEvent;
import modfixng.main.ModFixNG;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class ValidateActions implements Listener, Feature {

	// deny entity interact if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	// deny entity interact if inventory opened
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	// deny active slot switch while invnetory is opened
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerSlotSwitch(PlayerItemHeldEvent event) {
		if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	// deny commands use if inventory opened
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	// restrict block break while inventory is opened
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	//deny drop by q button (not in inventory) while inventory is open
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInItemDrop(BlockDigPacketItemDropEvent event) {
		if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
	}

	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public String getName() {
		return "ValidateActions";
	}

}