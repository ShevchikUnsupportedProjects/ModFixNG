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

import java.util.Arrays;
import java.util.HashSet;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.PacketContainerReadable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
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
		initDropButtonInventoryClickListener();
	}

	// close inventory on death and fix dropped items
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!config.fixBagEnabled) {
			return;
		}

		Player p = event.getEntity();

		p.closeInventory();

		event.getDrops().clear();
		for (ItemStack item : p.getInventory().getContents()) {
			if (item != null) {
				event.getDrops().add(item);
			}
		}
		for (ItemStack armor : p.getInventory().getArmorContents()) {
			if (armor != null) {
				event.getDrops().add(armor);
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
	private HashSet<String> knownInventoryNames = new HashSet<String>(
		Arrays.asList(
			new String[] {
				//forestry
				"forestry.storage.gui.ContainerNaturalistBackpack",
				"forestry.storage.gui.ContainerBackpack",
				"forestry.mail.gui.ContainerLetter",
				"forestry.apiculture.gui.ContainerBeealyzer",
				"forestry.arboriculture.gui.ContainerTreealyzer",
				"forestry.lepidopterology.gui.ContainerFlutterlyzer"
			}
		)
	);
	private void init19ButtonInventoryClickListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent event) {
					if (!config.fixBagEnabled) {
						return;
					}

					if (!config.fixBag19ButtonClickEnabled) {
						return;
					}

					if (event.getPlayer() == null) {
						return;
					}

					Player player = event.getPlayer();
					// check click type(checking for shift+button)
					if (event.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.MODE) == PacketContainerReadable.InventoryClick.Mode.NUMBER_KEY_PRESS) {
						// check to which slot we want to move item(checking if it is the holding bag slot)
						final int heldslot = player.getInventory().getHeldItemSlot();
						if (heldslot == event.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.BUTTON)) {
							// check inventory name (checking if one of the inventory names in list)
							String inventoryName = ModFixNGUtils.getOpenInventoryName(player);
							if (inventoryName != null && (config.fixBag19ButtonClickBagInventoryNames.contains(inventoryName) || knownInventoryNames.contains(inventoryName))) {
								event.setCancelled(true);
								player.updateInventory();
							}
						}
					}
				}
			}
		).syncStart();
	}

	// close inventory if trying to drop opened toolbox or cropnalyzer(q button in inventory)
	public void initDropButtonInventoryClickListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent event) {
					if (!config.fixBagEnabled) {
						return;
					}

					if (event.getPlayer() == null) {
						return;
					}

					int mode = event.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.MODE);
					int button = event.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.BUTTON);
					if (mode == PacketContainerReadable.InventoryClick.Mode.DROP && button != -999) {
						int slot = event.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.SLOT);
						if (isInvalidDropInventory(event.getPlayer(), slot)) {
							event.setCancelled(true);
							event.getPlayer().updateInventory();
						}
					}
				}
			}
		).syncStart();
	}
	private boolean isInvalidDropInventory(Player player, int slot) {
		if (config.fixBagCropanalyzerFixEnabled) {
			if (ModFixNGUtils.isCropanalyzerOpen(player)) {
				try {
					if (ModFixNGUtils.isTryingToDropOpenCropanalyzer(player, slot)) {
						return true;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		if (config.fixBagToolboxFixEnabled) {
			if (ModFixNGUtils.isToolboxOpen(player)) {
				try {
					if (ModFixNGUtils.isTryingToDropOpenToolBox(player, slot)) {
						return true;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

}
