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

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import modfixng.events.ClickInventoryPacketClickInventoryEvent.Mode;
import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class RestrictShiftClick implements Feature, Listener {

	private Config config;

	public RestrictShiftClick(Config config) {
		this.config = config;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInInventoryClick(ClickInventoryPacketClickInventoryEvent event) {
		if (event.getMode() == Mode.SHIFT_MOUSE_CLICK) {
			String invname = ModFixNGUtils.getOpenInventoryName(event.getPlayer());
			if (config.restrictShiftInvetoryNames.contains(invname)) {
				event.setCancelled(true);
				event.getPlayer().updateInventory();
			}
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
		return "InventoryShiftClickRestrict";
	}

}
