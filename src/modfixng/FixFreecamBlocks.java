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
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class FixFreecamBlocks implements Listener {

	private ModFixNG main;
	private Config config;

	FixFreecamBlocks(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		//zeroItemsCheck
		initInvCheck();
		//forceCloseInv
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initBlockCheck();
	}
	
	//check for 0-amount items
	private void initInvCheck()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.fixFreecamBlockZeroItemsCheckEnabled) {return;}
				
				for (Player p : Bukkit.getOnlinePlayers())
				{
					//hotbar slots
					for(int i = 0; i < 9; i++) 
					{
						ItemStack item = p.getInventory().getItem(i);
						if (item != null && item.getAmount() == 0)
						{
							p.getInventory().setItem(i, null);
						}
					}
					//armor
					for (ItemStack armor : p.getInventory().getArmorContents())
					{
						if (armor.getAmount() == 0)
						{
							armor.setType(Material.AIR);
						}
					}
				}
			}
		},0,1);
	}
	
	
	
	private HashMap<String,BlockState> playerOpenBlock = new HashMap<String,BlockState>(100);
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onPlayerOpenedBlock(PlayerInteractEvent e)
	{
		if (!config.fixFreecamBlockCloseInventoryOnBreakCheckEnabled) {return;}
		
		String playername = e.getPlayer().getName();
		if (playerOpenBlock.containsKey(playername))
		{
			e.setCancelled(true);
			return;
		}
		
		Block b = e.getClickedBlock();
		if (config.fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs.contains(Utils.getIDstring(b)) || (config.fixFreecamBlockCloseInventoryOnBreakAutoDetectContainers && Utils.hasInventory(b)))
		{
			playerOpenBlock.put(e.getPlayer().getName(), b.getState());
		}
	}
	
	//remove player from list when he closes inventory
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
						if (!config.fixFreecamBlockCloseInventoryOnBreakCheckEnabled) {return;}
						
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
						.params(main, Packets.Server.CLOSE_WINDOW)
						.serverSide()
						.listenerPriority(ListenerPriority.HIGHEST)
				) 
				{
					@Override
					public void onPacketSending(PacketEvent e) 
					{
						if (!config.fixFreecamBlockCloseInventoryOnBreakCheckEnabled) {return;}
						
						removePlayerData(e.getPlayer().getName());
				    }
				});
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e)
	{
		if (!config.fixFreecamBlockCloseInventoryOnBreakCheckEnabled) {return;}
		
		removePlayerData(e.getPlayer().getName());
	}
	
	//check if block is broken or player is too far away from it, if yes - force close inventory
	private void initBlockCheck()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.fixFreecamBlockCloseInventoryOnBreakCheckEnabled) {return;}

				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (playerOpenBlock.containsKey(player.getName()))
					{
						String playername = player.getName();
						BlockState bs = playerOpenBlock.get(playername);
						Block b = bs.getBlock();
						if 
						(
							b.getType() != bs.getType() || 
							!b.getWorld().getName().equals(player.getWorld().getName()) ||
							b.getLocation().distanceSquared(player.getLocation()) > 36
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
		playerOpenBlock.remove(playername);
	}
	
}
