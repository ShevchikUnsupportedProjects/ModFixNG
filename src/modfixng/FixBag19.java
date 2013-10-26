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

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

// BackPack fix
public class FixBag19 implements Listener {
	private ModFixNG main;
	private Config config;
	
	FixBag19(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		initBag19BugFixListener();
	}
	
	//close inventory on death
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) 
	{
		if (!config.enableBackPackFix) {return;}
		
		Player p = (Player) event.getEntity();
		p.closeInventory();
		if (config.enableCropanalyzerFix) 
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
	
	
	//close inventory on quit
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerExit(PlayerQuitEvent event) 
	{
		if (!config.enableBackPackFix) {return;}
		
		event.getPlayer().closeInventory();
	}

	
	//restrict using 1-9 buttons in modded inventories
	private void initBag19BugFixListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, Packets.Client.WINDOW_CLICK)
						.clientSide()
						.listenerPriority(ListenerPriority.HIGHEST)
						.optionIntercept()
				) 
				{
					  @SuppressWarnings("deprecation")
					  @Override
					  public void onPacketReceiving(PacketEvent e) 
					  {
						  try {
							  if (!config.enableBackPackFix) {return;}

							  if (e.getPlayer() == null) {return;}
						  
							  final Player player = e.getPlayer();					  
							  //if item in hand is one of the bad ids - check buttons
							  if (config.BackPacks19IDs.contains(player.getItemInHand().getTypeId())) 
							  {
								  //restrict illegal bag moving
								  //check click type , 2 ==  1..9 buttons (e.getPacket().getIntegers().getValues().get(3) - action type)
								  if (e.getPacket().getIntegers().getValues().get(3) == 2)
								  {//check to which slot we want to move item (if to bag slot - block action)
									  if (player.getInventory().getHeldItemSlot() == e.getPacket().getIntegers().getValues().get(2))
									  {
										  e.setCancelled(true);
										  player.updateInventory();
									  }
								  }
							  }
						  } catch (Exception ex) {ex.printStackTrace();}
					  }
				});
	}	
}
