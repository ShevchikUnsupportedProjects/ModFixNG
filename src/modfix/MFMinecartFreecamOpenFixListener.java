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

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class MFMinecartFreecamOpenFixListener implements Listener {
	
	private ModFixNG main;
	private Config config;
	
	MFMinecartFreecamOpenFixListener(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initMinecartInventoryClickCheck();
	}
	
	private ConcurrentHashMap<String,Entity> playersopenedminecart = new ConcurrentHashMap<String,Entity>();
	
	//add player to list when he opens minecart
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerOpenedMinecart(PlayerInteractEntityEvent e)
	{
		if (!config.enableMinecartFix)  {return;}
		
		if (config.minecartsIDs.contains(e.getRightClicked().getType().getTypeId()))
		{
			playersopenedminecart.put(e.getPlayer().getName(),e.getRightClicked());
		}
	}
	
	
	private void initMinecartInventoryClickCheck()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter.params(main, Packets.Client.WINDOW_CLICK)
						.clientSide()
						.listenerPriority(ListenerPriority.HIGHEST)
				) 
				{
					@Override
				    public void onPacketReceiving(PacketEvent e) 
					{	
				    	try {
				    		if (!config.enableMinecartFix) {return;}
				    
				    		if (e.getPlayer() == null) {return;}

				    		Player player = e.getPlayer();
				    		String plname = player.getName();
				    		if (playersopenedminecart.containsKey(plname))
				    		{
				    			Entity ent = playersopenedminecart.get(plname);
				    			if (!ent.isValid() || !ent.getWorld().equals(player.getWorld()) || ent.getLocation().distanceSquared(player.getLocation()) > 36)
				    			{
									e.setCancelled(true);
									e.getPlayer().closeInventory();
									playersopenedminecart.remove(plname);
				    			}
				    		}
				    	} catch (Exception ex) {ex.printStackTrace();}
					}
				});
	}
	
	
	
	//remove player from list when he closes minecart
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, Packets.Client.CLOSE_WINDOW)
						.clientSide()
						.listenerPriority(ListenerPriority.HIGHEST)
				) 
				{
					@Override
					public void onPacketReceiving(PacketEvent e) 
					{
						try {
							if (e.getPlayer() == null) {return;}
						
							playersopenedminecart.remove(e.getPlayer().getName());
				    	} catch (Exception ex) {ex.printStackTrace();}
					}
				});
	}
	
	//remove player from list when he closes minecart
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, Packets.Server.CLOSE_WINDOW)
						.serverSide()
						.listenerPriority(ListenerPriority.HIGHEST)
				) 
				{
					@Override
					public void onPacketSending(PacketEvent e) 
					{
			    		playersopenedminecart.remove(e.getPlayer().getName());
				    }
				});
	}
	
}
