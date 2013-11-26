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

package modfixng;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FixBagFrameInsert implements Listener {

	private ModFixNG main;
	private Config config;

	FixBagFrameInsert(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerClickedFrame(PlayerInteractEvent e)
	{
		if (!config.fixBagFrameInsertEnabled) {return;}
		
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {return;}
		
		if (config.fixBagFrameInsertBagIDs.contains(e.getPlayer().getItemInHand().getTypeId()))
		{
			if (config.fixBagFrameInsertGregIDs.contains(Utils.getIDstring(e.getClickedBlock())))
			{
				final Player player = e.getPlayer();
				if (config.fixBagFrameInsertFixType == 1)
				{
					e.setCancelled(true);
				} else
				if (config.fixBagFrameInsertFixType == 2)
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							player.closeInventory();
						}
					});
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerClickedFrame(PlayerInteractEntityEvent e)
	{
		if (!config.fixBagFrameInsertEnabled) {return;}
		
		if (config.fixBagFrameInsertBagIDs.contains(e.getPlayer().getItemInHand().getTypeId()))
		{
			if (e.getRightClicked().getType().getTypeId() == config.fixBagFrameInsertFrameentityID)
			{
				final Player player = e.getPlayer();
				if (config.fixBagFrameInsertFixType == 1)
				{
					e.setCancelled(true);
				} else
				if (config.fixBagFrameInsertFixType == 2)
				{
					Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
					{
						public void run()
						{
							player.closeInventory();
						}
					});
				}
			}
		}
	}
	
	
}
