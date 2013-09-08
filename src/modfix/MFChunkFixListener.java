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

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// ChunkUnloadInventoryFix
public class MFChunkFixListener implements Listener {

	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;

	MFChunkFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	//close inventory if player changed chunk while was moving
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerChangedChunkByMoveEvent(PlayerMoveEvent e)
	{
		if (config.enableChunkUnloadFixMove)
		{
			if (!e.getFrom().getChunk().equals(e.getTo().getChunk()))
			{
					e.getPlayer().closeInventory();
			}
		}
	}
	
	//close inventory if palyer teleported form chunk
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerChangedChunkByTeleport(PlayerTeleportEvent e)
	{
		if (config.enableChunkUnloadFixTP)
		{
			if (!e.getFrom().getChunk().equals(e.getTo().getChunk()))
			{
					e.getPlayer().closeInventory();
			}
		}
	}
	
	//close inventory on world change
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerChangedWolrd(PlayerChangedWorldEvent e)
	{
		if (config.enableChunkUnloadFixTP)
		{
			e.getPlayer().closeInventory();
		}
	}
}
