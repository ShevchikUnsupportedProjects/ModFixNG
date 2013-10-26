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
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
				if (!config.enablefreecamzeroitemscheck) {return;}
				
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
				}
			}
		},0,1);
	}
	
	private HashMap<String,Block> playerOpenBlock = new HashMap<String,Block>();
	private HashMap<Block,Integer> openedBlockID = new  HashMap<Block,Integer>();
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onPlayerOpenedBlock(PlayerInteractEvent e)
	{
		if (!config.enablefreecaminvclosecheck) {return;}
		
		Block b = e.getClickedBlock();
		if (config.blocksWithInvIDs.contains(Utils.getIDstring(b)))
		{
			playerOpenBlock.put(e.getPlayer().getName(), b);
			openedBlockID.put(b, b.getTypeId());
		}
	}
	
	//remove player from list when he closes inventory
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
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
						if (!config.enablefreecaminvclosecheck) {return;}
						
						if (e.getPlayer() == null) {return;}
						
						String playername = e.getPlayer().getName();
						if (playerOpenBlock.containsKey(playername))
						{
							Block b = playerOpenBlock.get(playername);
							openedBlockID.remove(b);
							playerOpenBlock.remove(playername);
						}
					}
				}).syncStart();
	}
	
	//remove player from list when he closes inventory
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
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
						if (!config.enablefreecaminvclosecheck) {return;}
						
						String playername = e.getPlayer().getName();
						if (playerOpenBlock.containsKey(playername))
						{
							Block b = playerOpenBlock.get(playername);
							openedBlockID.remove(b);
							playerOpenBlock.remove(playername);
						}
				    }
				}).syncStart();
	}
	
	//check if block is broken, is yes - force close inventory
	private void initBlockCheck()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.enablefreecamzeroitemscheck) {return;}
				
				HashSet<String> playerNamesToCheck = new HashSet<String>(playerOpenBlock.keySet());
				for (String playername : playerNamesToCheck)
				{
					Block b = playerOpenBlock.get(playername);
					if (b.getTypeId() != openedBlockID.get(b))
					{
						try {Bukkit.getPlayerExact(playername).closeInventory();} catch (Exception e) {}
						openedBlockID.remove(b);
						playerOpenBlock.remove(playername);
					}
				}
			}
		},0,1);
	}

}
