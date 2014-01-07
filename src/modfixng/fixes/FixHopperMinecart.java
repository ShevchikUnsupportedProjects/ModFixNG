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

import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class FixHopperMinecart implements Listener {

	@SuppressWarnings("unused")
	private ModFixNG main;
	private Config config;
	
	public FixHopperMinecart(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	//force close player inventory if he was in hopper minecart on quit
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerLeave(PlayerQuitEvent e)
	{
		if (!config.fixHopperMinecart) {return;}
		
		if (e.getPlayer().isInsideVehicle() && e.getPlayer().getVehicle() instanceof HopperMinecart)
		{
			e.getPlayer().closeInventory();
		}
	}
	
}
