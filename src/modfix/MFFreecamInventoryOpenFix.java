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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class MFFreecamInventoryOpenFix implements Listener {

	private Main main;
	private ModFixConfig config;

	MFFreecamInventoryOpenFix(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initBlockCheck();
		initInvCheck();
	}
	
	private HashMap<Block,HashSet<String>> openedinvs = new HashMap<Block,HashSet<String>>();
	private HashMap<String,Block> backreference = new HashMap<String,Block>();
	private HashMap<Block,Integer> matreference = new HashMap<Block,Integer>();
	
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onPlayerOpenedBlockInventory(PlayerInteractEvent e)
	{
		if (!config.enableFreecamFix) {return;}
		
		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {return;}
		
		if (config.freecamBlockIDs.contains(Utils.getIDstring(e.getClickedBlock())))
		{
			Block b = e.getClickedBlock();
			String pl = e.getPlayer().getName();
			if (openedinvs.get(b) == null)
			{
				openedinvs.put(b, new HashSet<String>());
			}
			openedinvs.get(b).add(pl);
			backreference.put(pl, b);
			matreference.put(b, b.getTypeId());
		}
	}
	
	private void initBlockCheck()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				for (Block b : new HashSet<Block>(matreference.keySet()))
				{
					if (b!=null)
					{
						if (b.getTypeId() != (matreference.get(b)))
						{
							for (String p : openedinvs.get(b))
							{
								backreference.remove(p);
								if (Bukkit.getPlayerExact(p) != null)
								{
									try {
										 Bukkit.getPlayerExact(p).closeInventory();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
							openedinvs.remove(b);
							matreference.remove(b);
						}
					}
				}
			}
		},0,1);
	}
	
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				  new PacketAdapter(main, ConnectionSide.CLIENT_SIDE, 
				  ListenerPriority.HIGHEST, Packets.Client.CLOSE_WINDOW) {
					@Override
				    public void onPacketReceiving(final PacketEvent e) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
							public void run() {
								String pl = e.getPlayer().getName();
								if (backreference.containsKey(pl)) {
									openedinvs.get(backreference.get(pl)).remove(pl);
									if (openedinvs.get(backreference.get(pl)).size() == 0) {
										openedinvs.remove(backreference.get(pl));
									}
									matreference.remove(backreference.get(pl));
									backreference.remove(pl);
								}
							}
						});
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
				    	String pl = e.getPlayer().getName();
				    	if (backreference.containsKey(pl))
				    	{
				    		openedinvs.get(backreference.get(pl)).remove(pl);
				    		if (openedinvs.get(backreference.get(pl)).size() == 0) {
				    			openedinvs.remove(backreference.get(pl));
				    		}
				    		matreference.remove(backreference.get(pl));
				    		backreference.remove(pl);
				    	}
				    }
				});
	}
	
	
	
	//additional check for 0-amount items
	public void initInvCheck()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.enablefreecamzeroitemscheck) {return;}
				for (Player p : Bukkit.getOnlinePlayers())
				{
					//hotbar slots
					for(int i = 0; i < 9; i++) {
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
	
	
}
