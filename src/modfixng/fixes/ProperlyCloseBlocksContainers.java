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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.nms.utils.NMSUtilsAccess;
import modfixng.utils.ModFixNGUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class ProperlyCloseBlocksContainers implements Listener, Feature {

	private Config config;
	public ProperlyCloseBlocksContainers(Config config) {
		this.config = config;
	}

	private final HashMap<Player, BlockState> playerOpenBlock = new HashMap<Player, BlockState>(200);

	private final HashSet<Material> knownBlockMaterials  = new HashSet<Material>(
		Arrays.asList(
			new Material[] {
				//some vanilla minecraft item that has gui but doesn't implement IInventory
				Material.ANVIL,
				Material.ENCHANTMENT_TABLE,
			}
		)
	);
	// add player to list when he opens block inventory
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerOpenedBlock(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Player player = e.getPlayer();

		if (playerOpenBlock.containsKey(player)) {
			if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(player)) {
				e.setCancelled(true);
				return;
			}
		}

		final Block b = e.getClickedBlock();
		if (config.properlyCloseBlocksContainersBlocksMaterials.contains(ModFixNGUtils.getMaterialString(b)) || NMSUtilsAccess.getNMSUtils().hasInventory(b) || knownBlockMaterials.contains(b.getType())) {
			playerOpenBlock.put(player, b.getState());
		}
	}

	//remove player from list when he closes inventory
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInvetoryClose(InventoryCloseEvent event) {
		playerOpenBlock.remove(event.getPlayer());
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		playerOpenBlock.remove(event.getPlayer());
	}

	//check valid on inventory click
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		BlockState blockstate = playerOpenBlock.get(player);
		if (blockstate != null) {
			Block block = blockstate.getBlock();
			if (!isValid(player, blockstate, block)) {
				event.setCancelled(true);
				player.closeInventory();
			}
		}
	}

	private BukkitTask task;
	// check if block is broken or player is too far away from it or the block is broken, if yes - force close inventory
	private void initBlockCheck() {
		task = Bukkit.getScheduler().runTaskTimer(
			ModFixNG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					Iterator<Entry<Player, BlockState>> iterator = playerOpenBlock.entrySet().iterator();
					while (iterator.hasNext()) {
						Entry<Player, BlockState> entry = iterator.next();
						BlockState blockstate = entry.getValue();
						if (!isValid(entry.getKey(), blockstate, blockstate.getBlock())) {
							iterator.remove();
							entry.getKey().closeInventory();
						}
					}
				}
			},
			0, 1
		);
	}

	private boolean isValid(Player player, BlockState bs, Block b) {
		if (!NMSUtilsAccess.getNMSUtils().isInventoryValid(player)) {
			return false;
		}
		if (!b.getWorld().equals(player.getWorld()) || b.getLocation().distanceSquared(player.getLocation()) > 36) {
			return false;
		}
		if ((bs.getType() == Material.FURNACE) || (bs.getType() == Material.BURNING_FURNACE)) {
			return (b.getType() == Material.FURNACE) || (b.getType() == Material.BURNING_FURNACE);
		}
		return bs.getType() == b.getType();
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
		initBlockCheck();
	}

	@Override
	public void unload() {
		task.cancel();
		HandlerList.unregisterAll(this);
		Iterator<Entry<Player, BlockState>> iterator = playerOpenBlock.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Player, BlockState> entry = iterator.next();
			iterator.remove();
			entry.getKey().closeInventory();
		}
	}

	@Override
	public String getName() {
		return "ProperlyCloseBlocksContainers";
	}

}