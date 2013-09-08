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

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class MFMinecartFreecamOpenFixListener implements Listener {
	
	private Main main;
	private ModFixConfig config;
	
	MFMinecartFreecamOpenFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initMinecartInventoryClickCheck();
	}
	
	private HashMap<String,Entity> playersopenedminecart = new HashMap<String,Entity>();
	
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
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.WINDOW_CLICK) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {	
				    	
						if (!config.enableMinecartFix) {return;}
				    
						Player pl = e.getPlayer();
						if (playersopenedminecart.containsKey(pl.getName()))
						{
							Entity ent = playersopenedminecart.get(pl.getName());
							if (!ent.isValid() || !ent.getWorld().equals(pl.getWorld()) || ent.getLocation().distanceSquared(pl.getLocation()) > 36)
							{
								e.setCancelled(true);
								playersopenedminecart.remove(pl.getName());
								e.getPlayer().closeInventory();
							}
						}
					}
				});
	}
	
	
	
	//remove player from list when he closes minecart
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {
				    	playersopenedminecart.remove(e.getPlayer().getName());
				    }
				});
	}
	
	//remove player from list when he closes minecart
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.SERVER_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Server.CLOSE_WINDOW) {
					@Override
				    public void onPacketSending(PacketEvent e) {
			    		playersopenedminecart.remove(e.getPlayer().getName());
				    }
				});
	}
	
}
