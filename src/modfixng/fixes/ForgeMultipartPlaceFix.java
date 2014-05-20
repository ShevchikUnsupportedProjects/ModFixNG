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

import java.util.HashMap;

import modfixng.main.Config;
import modfixng.main.ModFixNG;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ForgeMultipartPlaceFix implements Listener, Feature {

	private Config config;
	public ForgeMultipartPlaceFix(Config config) {
		this.config = config;
	}

	private HashMap<String, Block> blocksPlaced = new HashMap<String, Block>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Block placed = event.getBlockPlaced();

		if (!event.isCancelled()) {
			if (placed.getTypeId() == config.fixMultipartBlockID) {
				blocksPlaced.put(event.getPlayer().getName(), placed);
			}
		}

		if (event.isCancelled()) {
			ItemStack item = event.getPlayer().getItemInHand();
			if (item.getTypeId() == config.fixMultipartItemID) {
				if (placed.getTypeId() != config.fixMultipartBlockID) {
					String name = event.getPlayer().getName();
					if (blocksPlaced.containsKey(name)) {
						blocksPlaced.get(name).setType(Material.AIR);
						blocksPlaced.remove(name);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		blocksPlaced.remove(event.getPlayer().getName());
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
		return "ForgeMultipartFix";
	}

}
