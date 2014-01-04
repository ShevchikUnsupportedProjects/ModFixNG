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

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class FixFreecamEntities implements Listener {
	
	private ModFixNG main;
	private Config config;
	
	FixFreecamEntities(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initEntitiesCheck();
	}
	
	HashMap<String,Entity> playerOpenEntity = new HashMap<String,Entity>(100);
	
	//add player to list when he opens minecart
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onPlayerOpenedMinecart(PlayerInteractEntityEvent e)
	{
		if (!config.fixFreecamEntitiesEnabled)  {return;}
		
		String playername = e.getPlayer().getName();
		if (playerOpenEntity.containsKey(playername))
		{
			e.setCancelled(true);
			return;
		}
		
		if (config.fixFreecamEntitiesEntitiesIDs.contains(e.getRightClicked().getType().getTypeId()))
		{
			playerOpenEntity.put(playername,e.getRightClicked());
		}
	}

	//remove player from list when he closes minecart
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, PacketType.Play.Client.CLOSE_WINDOW)
						.clientSide()
				) 
				{
					@Override
					public void onPacketReceiving(PacketEvent e) 
					{
						if (!config.fixFreecamEntitiesEnabled) {return;}
						
						if (e.getPlayer() == null) {return;}

						final String playername = e.getPlayer().getName();
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
						{
							public void run()
							{
								removePlayerData(playername);
							}
						});
					}
				});
	}
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, PacketType.Play.Server.CLOSE_WINDOW)
						.serverSide()
				) 
				{
					@Override
					public void onPacketSending(PacketEvent e) 
					{
						if (!config.fixFreecamEntitiesEnabled) {return;}
						
						removePlayerData(e.getPlayer().getName());
				    }
				});
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e)
	{
		if (!config.fixFreecamEntitiesEnabled) {return;}

		removePlayerData(e.getPlayer().getName());
	}

	//check if entity is not valid or player is too far away from it, if yes - force close inventory
	private void initEntitiesCheck()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.fixFreecamEntitiesEnabled) {return;}

				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (playerOpenEntity.containsKey(player))
					{
						String playername = player.getName();
						Entity entity = playerOpenEntity.get(playername);
						if 
						(
							!entity.isValid() ||
							!entity.getWorld().getName().equals(player.getWorld().getName()) ||
							entity.getLocation().distanceSquared(player.getLocation()) > 36
						)
						{
							player.closeInventory();
							removePlayerData(playername);
						}
					}
				}
			}
		},0,1);
	}
	
	private void removePlayerData(String playername)
	{
		playerOpenEntity.remove(playername);
	}
	
}
