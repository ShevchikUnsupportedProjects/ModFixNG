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

import java.util.HashSet;
import java.util.LinkedList;

import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.PacketContainerReadable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.async.AsyncListenerHandler;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class ValidateActions implements Listener, Feature {

	// deny entity interact if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// deny entity interact if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// deny active slot switch while invnetory is opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerSlotSwitch(PlayerItemHeldEvent event) {
		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// deny commands use if inventory opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	// restrict block break while inventory is opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}


	private LinkedList<AsyncListenerHandler> listeners = new LinkedList<AsyncListenerHandler>();

	private HashSet<String> players = new HashSet<String>();
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		players.add(e.getPlayer().getName());
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLeave(PlayerQuitEvent e) {
		players.remove(e.getPlayer().getName());
	}

	// deny block dig drop mode packets if inventory opened
	public void initDropButtonPlayClickListener() {
		AsyncListenerHandler listener = ModFixNG.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(ModFixNG.getInstance(), PacketType.Play.Client.BLOCK_DIG)
				.listenerPriority(ListenerPriority.LOWEST)
			) {
				@Override
				public void onPacketReceiving(PacketEvent e) {
					Player player = e.getPlayer();
					if (player == null) {
						return;
					}

					if (!players.contains(player.getName())) {
						e.setCancelled(true);
						return;
					}

					int status = e.getPacket().getIntegers().getValues().get(4);
					if (status == 3 || status == 4) {
						if (ModFixNGUtils.isInventoryOpen(player)) {
							e.setCancelled(true);
						}
					}
				}
			}
		);
		listener.syncStart();
		listeners.add(listener);
	}

	// do not allow to click invalid inventory
	private void initInventoryClickListener() {
		AsyncListenerHandler listener = ModFixNG.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(ModFixNG.getInstance(), PacketType.Play.Client.WINDOW_CLICK)
				.listenerPriority(ListenerPriority.LOWEST)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					Player player = e.getPlayer();
					if (player == null) {
						return;
					}

					if (!players.contains(player.getName())) {
						e.setCancelled(true);
						e.getPlayer().updateInventory();
						return;
					}

					int invid = e.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.INVENTORY_ID);
					if (!ModFixNGUtils.isContainerValid(invid, player)) {
						e.setCancelled(true);
						e.getPlayer().updateInventory();
					}
				}
			}
		);
		listener.syncStart();
		listeners.add(listener);
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
		initDropButtonPlayClickListener();
		initInventoryClickListener();
	}

	@Override
	public void unload() {
		for (AsyncListenerHandler listener : listeners) {
			ModFixNG.getProtocolManager().getAsynchronousManager().unregisterAsyncHandler(listener);
		}
		HandlerList.unregisterAll(this);
	}

	@Override
	public String getName() {
		return "ValidateActions";
	}

}
