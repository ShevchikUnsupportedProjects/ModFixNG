package modfixng;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;

public class RestrictBreakWhileOpen implements Listener {

	private ModFixNG main;
	private Config config;

	RestrictBreakWhileOpen(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
	}


	private HashMap<String,BlockState> playerOpenBlock = new HashMap<String,BlockState>(100);
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=true)
	public void onPlayerOpenedBlock(PlayerInteractEvent e)
	{
		if (!config.restrictBlockBreakWhileOpenEnabled) {return;}

		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {return;}
		
		String playername = e.getPlayer().getName();
		if (playerOpenBlock.containsKey(playername))
		{
			e.setCancelled(true);
			return;
		}

		Block b = e.getClickedBlock();
		if (config.restrictBlockBreakWhileOpenIDs.contains(ModFixNGUtils.getIDstring(b)))
		{
			ItemStack i = e.getPlayer().getItemInHand();
			if (!config.restrictBlockBreakWhileOpenWrehchesIDs.contains(i.getTypeId()) && !ModFixNGUtils.isWrench(i))
			{
				playerOpenBlock.put(playername, b.getState());
			}
		}
	}
	
	//remove player from list when he closes inventory
	private void initClientCloseInventoryFixListener()
	{
		main.protocolManager.addPacketListener(
				new PacketAdapter(
						PacketAdapter
						.params(main, PacketType.Play.Client.CLOSE_WINDOW)
						.clientSide()
						.optionManualGamePhase()
						.gamePhase(GamePhase.PLAYING)
						.listenerPriority(ListenerPriority.LOWEST)
				) 
				{
					@Override
					public void onPacketReceiving(PacketEvent e) 
					{
						if (!config.restrictBlockBreakWhileOpenEnabled) {return;}
						
						if (e.getPlayer() == null) {return;}
						
						final String playername = e.getPlayer().getName();
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable()
						{
							public void run()
							{
								playerOpenBlock.remove(playername);
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
						.optionManualGamePhase()
						.gamePhase(GamePhase.PLAYING)
						.listenerPriority(ListenerPriority.LOWEST)
				) 
				{
					@Override
					public void onPacketSending(PacketEvent e) 
					{
						if (!config.restrictBlockBreakWhileOpenEnabled) {return;}
						
						playerOpenBlock.remove(e.getPlayer().getName());
				    }
				});
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e)
	{
		if (!config.restrictBlockBreakWhileOpenEnabled) {return;}
		
		playerOpenBlock.remove(e.getPlayer().getName());
	}
	
	//restrict block break while block is open
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onBlockBreak(BlockBreakEvent e)
	{
		if (!config.restrictBlockBreakWhileOpenEnabled) {return;}
		
		Block brokenblock = e.getBlock();
		for (BlockState bs : playerOpenBlock.values())
		{
			if (bs.getBlock().equals(brokenblock))
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.RED+"Вы не можете сломать этот блок пока он открыт другим игроком");
				return;
			}
		}
	}
	
	//restrict block interact using wrenches
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onWrenchInteract(PlayerInteractEvent e)
	{
		if (!config.restrictBlockBreakWhileOpenEnabled) {return;}
		
		ItemStack i = e.getPlayer().getItemInHand();
		if (config.restrictBlockBreakWhileOpenWrehchesIDs.contains(i.getTypeId()) || ModFixNGUtils.isWrench(i))
		{
			Block brokenblock = e.getClickedBlock();
			for (BlockState bs : playerOpenBlock.values())
			{
				if (bs.getBlock().equals(brokenblock))
				{
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED+"Вы не можете сломать этот блок пока он открыт другим игроком");
					return;
				}
			}
		}
	}
	
}
