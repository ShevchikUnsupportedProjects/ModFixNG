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
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

//CraftingTablesFix
public class MFTableFixListener implements Listener {

	private Main main;
	private ModFixConfig config;
	
	MFTableFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initCloseInventoryFixListener();
		scheduleCheckTask();
	}
	
	
	private HashMap<Block, String> protectblocks = new HashMap<Block, String>();
	private HashMap<String, Block> backreference = new HashMap<String, Block>();
	private HashMap<Block, Integer> matreference = new HashMap<Block, Integer>();
	
	//allow only one player to interact with table at a time
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void OnPlayerIneractTable(PlayerInteractEvent e)
	{
		if (!config.enableTablesFix) {return;}
		
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Player pl = e.getPlayer();
			Block binteract = e.getClickedBlock();
			String checkid = Utils.getIDstring(binteract);
			if (config.IntTablesIDs.contains(checkid))
			{
				if (protectblocks.get(binteract) == null)
				{//Put block to list of protected blocks
					protectblocks.put(binteract, pl.getName());
					backreference.put(pl.getName(), binteract);
					matreference.put(binteract, binteract.getTypeId());
					return;
				}
				//If it's the same player let him open this (in case we lost something and block is still protected (this is really bad if this happened))
				if (pl.getName().equals(protectblocks.get(binteract))) {return;}

				//We reached here, if itemid is in the Interact list we will disallow interact
				pl.sendMessage(ChatColor.RED + "Вы не можете открыть этот предмет, по крайней мере сейчас");
				e.setCancelled(true);
			}
		}
	}
	
	

	private void initCloseInventoryFixListener()
	{//remove block from hashmap on inventory close, just InventoryCloseEvent is not enough, not every mod fires it, so we will use packets.
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {
					String plname = e.getPlayer().getName();
					if (backreference.containsKey(plname))
						{//gotcha, you closed table inventory
						    protectblocks.remove(backreference.get(plname));
						    backreference.remove(plname);
						    matreference.remove(backreference.get(plname));
						}
				    }
				});
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e)
	{//Player can break opened block and then won't trigger inventory closing
		if (!config.enableTablesFix) {return;}
		
		Block br = e.getBlock();
		if (protectblocks.containsKey(br))
		{
			//check if user shouldn't be able to break this block
			if (config.BrkTablesIDs.contains(Utils.getIDstring(br)))
			{
				e.getPlayer().sendMessage(ChatColor.RED + "Вы не можете сломать этот предмет, по крайней мере сейчас");
				e.setCancelled(true);
			} else {
				backreference.remove(protectblocks.get(br));
				protectblocks.remove(br);
			    matreference.remove(br);
			}
		}
	}
	
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e)
	{//player can quit without closing table inventory, let's check it
		String plname = e.getPlayer().getName();
		if (backreference.containsKey(plname))
		{
		    protectblocks.remove(backreference.get(plname));
		    backreference.remove(plname);
		    matreference.remove(backreference.get(plname));
		}
	}
		
	
	private void scheduleCheckTask()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.enableTablesFixExtendedCheck) {return;}
				
				for (Block b : new HashSet<Block>(protectblocks.keySet()))
				{
					//block is destroyed but we didn't catch this , because of lack of the bukkit events for mods
					//this means that someone is probably trying to duplicate items
					//we must remove all drop near block to avoid this
					if (b.getTypeId() != (matreference.get(b)))
					{
						if (config.BrkTablesIDs.contains(Utils.getIDstring(b)))
						{
							deleteDropNearBlock(b);
						}
						//remove block from hashmaps
						matreference.remove(b);
						backreference.remove(protectblocks.get(b));
						protectblocks.remove(b);
					}
				}
			}
		},0,1);
	}
	
	private void deleteDropNearBlock(final Block b)
	{
		//remove all items
		Entity fakeEntity = b.getWorld().spawnEntity(b.getLocation(), EntityType.ARROW);
		for (Entity item : fakeEntity.getNearbyEntities(3, 3, 3))
		{
			if (item instanceof Item)
			{
				item.remove();
			}
		}
		fakeEntity.remove();
	}
	
	
	
}
