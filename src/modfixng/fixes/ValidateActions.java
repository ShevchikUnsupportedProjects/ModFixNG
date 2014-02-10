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

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class ValidateActions implements Listener {
	private ModFixNG main;
	private Config config;

	public ValidateActions(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		initInventoryClickListener();
		initInventoryCloseListener();
	}

	// deny entity interact if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		if (!config.validateActionsEnabled) {
			return;
		}
		if (!config.validateActionsInteractEnabled) {
			return;
		}

		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// deny entity interact if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!config.validateActionsEnabled) {
			return;
		}
		if (!config.validateActionsInteractEnabled) {
			return;
		}

		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// deny commands use if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (!config.validateActionsEnabled) {
			return;
		}
		if (!config.validateActionsInteractEnabled) {
			return;
		}
		
		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// do not allow to click invalid inventory
	private void initInventoryClickListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
				.listenerPriority(ListenerPriority.LOWEST)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (!config.validateActionsEnabled) {
						return;
					}
					if (!config.validateActionsInventoryEnabled) {
						return;
					}

					if (e.getPlayer() == null) {
						return;
					}

					int invid = e.getPacket().getIntegers().getValues().get(0);
					if (!ModFixNGUtils.isContainerValid(invid, e.getPlayer())) {
						e.setCancelled(true);
						e.getPlayer().updateInventory();
					}
				}
			}
		).syncStart();
	}

	// ignore invalid inventory close
	private void initInventoryCloseListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.CLOSE_WINDOW)
				.listenerPriority(ListenerPriority.LOWEST)
			) {
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (!config.validateActionsEnabled) {
						return;
					}
					if (!config.validateActionsInventoryEnabled) {
						return;
					}

					if (e.getPlayer() == null) {
						return;
					}

					int invid = e.getPacket().getIntegers().getValues().get(0);
					if (!ModFixNGUtils.isContainerValid(invid, e.getPlayer())) {
						e.setCancelled(true);
						e.getPlayer().closeInventory();
					}
				}
			}
		).syncStart();
	}

}
