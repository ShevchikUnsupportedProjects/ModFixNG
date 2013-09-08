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

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

// BackPack fix
public class MFBagFixListener implements Listener {
	private Main main;
	private ModFixConfig config;
	
	MFBagFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initBag19BugFixListener();
	}
	
	//close inventory on death
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerD(PlayerDeathEvent event) {
		if (config.enableBackPackFix) {
			Player p = (Player) event.getEntity();
			p.closeInventory();
			if (config.enableCropanalyzerFix) 
			{
				if (p.getItemInHand().getTypeId() == config.CropanalyzerID)
				{
					Iterator<ItemStack> it = event.getDrops().iterator();
					while (it.hasNext())
					{
						ItemStack i = it.next();
						if (i.getTypeId() == config.CropanalyzerID)
						{
							it.remove();
						}
					}
				}
			}
		}
	}
	
	
	//close inventory on quit
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerExit(PlayerQuitEvent event) {
		if (config.enableBackPackFix) {
					event.getPlayer().closeInventory();
			}
	}

	
	//restrict using 1-9 buttons in modded inventories
	private void initBag19BugFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.WINDOW_CLICK) {
				    @SuppressWarnings("deprecation")
					@Override
				    public void onPacketReceiving(PacketEvent e) {
				    	if (!config.enableBackPackFix) {return;}
				    	
				    	Player pl = e.getPlayer();
			    		//if item in hand is one of the bad ids - check buttons
			    		if (config.BackPacks19IDs.contains(pl.getItemInHand().getTypeId())) {
			    			//restrict illegal bag moving
			    			//check click type , 2 ==  1..9 buttons (e.getPacket().getIntegers().getValues().get(3) - action type)
			    			if (e.getPacket().getIntegers().getValues().get(3) == 2)
			    			{//check to which slot we want to move item (if to bag slot - block action)
			    				if (pl.getInventory().getHeldItemSlot() == e.getPacket().getIntegers().getValues().get(2))
			    				{
					    			e.setCancelled(true);
						    		e.getPlayer().updateInventory();
			    				}
			    			}
				    		//restrict cropanalyzer moving
				    		if (!config.enableCropanalyzerFix) {return;}
				    		if (pl.getItemInHand().getTypeId() == config.CropanalyzerID)
				    		{
				    			if (e.getPacket().getIntegers().getValues().get(3) == 0 || e.getPacket().getIntegers().getValues().get(3) == 2)
				    			{
				    				//check which slot we clicked (if it is cropanalyzer slot - cancel action)
				    				if (e.getPacket().getIntegers().getValues().get(1) == pl.getInventory().getHeldItemSlot()+3)
				    				{
				    					e.setCancelled(true);
				    					e.getPlayer().updateInventory();
				    				}
				    			}
				    		}
				 	   	}
				    }
				});
	}	
}
