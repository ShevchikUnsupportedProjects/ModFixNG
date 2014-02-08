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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class FixBag implements Listener {
	private ModFixNG main;
	private Config config;

	public FixBag(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		init19ButtonInventoryClickListener();
		initInventoryClickListener();
	}

	// close inventory on death and also fix dropped cropanalyzer
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		Player p = event.getEntity();

		if (config.fixBagCropanalyzerFixEnabled) {
			if (ModFixNGUtils.isCropanalyzerOpen(p)) {
				try {
					ModFixNGUtils.findAndFixOpenCropanalyzer(p, event.getDrops());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		p.closeInventory();
	}

	// deny entity interact if inventory already opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractBlock(PlayerInteractEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			if (config.fixBagRestrictInteractIfInventoryOpen) {
				event.setCancelled(true);
				return;
			}
		}
	}

	// deny entity interact if inventory already opened
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		if (ModFixNGUtils.isInventoryOpen(event.getPlayer())) {
			if (config.fixBagRestrictInteractIfInventoryOpen) {
				event.setCancelled(true);
				return;
			}
		}
	}

	// close inventory on portal enter or exit
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
			return;
		}

		if (event.getTo().getBlock().getType() == Material.PORTAL || event.getFrom().getBlock().getType() == Material.PORTAL) {
			event.getPlayer().closeInventory();
		}

	}

	// close inventory on quit
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerExit(PlayerQuitEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		event.getPlayer().closeInventory();
	}

	// restrict using 1-9 buttons in bags inventories if it will move bag to another slot
	private void init19ButtonInventoryClickListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (!config.fixBagEnabled) {
						return;
					}

					if (!config.fixBag19ButtonClickEnabled) {
						return;
					}

					if (e.getPlayer() == null) {
						return;
					}

					Player player = e.getPlayer();
					// if item in hand is one of the bad ids - check buttons
					if (config.fixBag19ButtonClickBagIDs.contains(player.getItemInHand().getTypeId())) {
						// check click type(checking for shift+button)
						if (e.getPacket().getIntegers().getValues().get(3) == 2) {
							// check to which slot we want to move item(checking if it is the holding bag slot)
							final int heldslot = player.getInventory().getHeldItemSlot();
							if (heldslot == e.getPacket().getIntegers().getValues().get(2)) {
								e.setCancelled(true);
								player.updateInventory();
							}
						}
					}
				}
			}
		).start();
	}

	// do not allow to click invalid inventory
	private void initInventoryClickListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (!config.fixBagEnabled) {
						return;
					}

					if (e.getPlayer() == null) {
						return;
					}

					int invid = e.getPacket().getIntegers().getValues().get(0);
					if (!ModFixNGUtils.isClickValid(invid, e.getPlayer())) {
						e.setCancelled(true);
						e.getPlayer().updateInventory();
					}
				}
			}
		).syncStart();
	}

	// close inventory if trying to drop opened toolbox or cropnalyzer
	private boolean closinginventory = false;
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		if (closinginventory) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack droppeditem = event.getItemDrop().getItemStack();
		if (config.fixBagCropanalyzerFixEnabled) {
			if (ModFixNGUtils.isCropanalyzerOpen(player)) {
				try {
					if (ModFixNGUtils.isTryingToDropOpenCropanalyzer(player, droppeditem)) {
						closinginventory = true;
						player.closeInventory();
						closinginventory = false;
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		if (config.fixBagToolboxFixEnabled) {
			if (ModFixNGUtils.isToolboxOpen(player)) {
				try {
					if (ModFixNGUtils.isTryingToDropOpenToolBox(player, droppeditem)) {
						closinginventory = true;
						player.closeInventory();
						closinginventory = false;
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
