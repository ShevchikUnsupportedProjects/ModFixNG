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

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class MFExpFixListener implements Listener {

	private Main main;
	private ModFixConfig config;

	MFExpFixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
	}

	
	private HashMap<String,Integer> plinf = new HashMap<String,Integer>(); //We will add player to this list when he enters furnace and remove when he leaves
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void OnPlayerIneractFurnace(PlayerInteractEvent e)
	{
		if (!config.enableExpFix) {return;}
		
	
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			String checkid = Utils.getIDstring(e.getClickedBlock());
			if (config.furnSlotIDs.contains(checkid))
			{
				final Block b = e.getClickedBlock();
				//schedule the task that will remove all orbs nearby
				int task =
				Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
				{
					public void run()
					{
						if (config.enableExpFixExtendedCheck)
						{
							deleteExperienceOrbsNearBlock(b);
						}
					}
				},0,1);
				plinf.put(e.getPlayer().getName(),task);
			}
		}
	}
	
	private void deleteExperienceOrbsNearBlock(final Block b)
	{
		//remove all items
		Entity fakeEntity = b.getWorld().spawnEntity(b.getLocation(), EntityType.ARROW);
		for (Entity item : fakeEntity.getNearbyEntities(3, 3, 3))
		{
			if (item instanceof ExperienceOrb)
			{
				item.remove();
			}
		}
		fakeEntity.remove();
	}
	
	//won't allow player to earn exp if he is in furnace	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onExpGain(PlayerExpChangeEvent e) 
	{
		if (plinf.containsKey(e.getPlayer().getName()))
		{
			e.setAmount(0);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e)
	{//player can quit without closing furnace inventory, let's check it
		removePlayerInf(e.getPlayer().getName());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent e)
	{//player can be kicked without closing furnace inventory, let's check it
		removePlayerInf(e.getPlayer().getName());
	}
	
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(PacketEvent e) {
						removePlayerInf(e.getPlayer().getName());
				    }
				});
	}
	
	
	private void initServerCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.SERVER_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Server.CLOSE_WINDOW) {
					@Override
				    public void onPacketSending(PacketEvent e) {
						removePlayerInf(e.getPlayer().getName());
				    }
				});
	}
	
	private void removePlayerInf(String pName)
	{
		if (plinf.containsKey(pName))
		{
			Bukkit.getScheduler().cancelTask(plinf.get(pName));
			plinf.remove(pName);
		}
	}	
	
}
