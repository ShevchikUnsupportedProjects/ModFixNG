package modfixng;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketContainer;

public class FixSlotDesync {

	private ModFixNG main;
	private Config config;
	public FixSlotDesync(ModFixNG main, Config config)
	{
		this.main = main;
		this.config = config;
		startInvSync();
	}
	
	
	private void startInvSync()
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable()
		{
			public void run()
			{
				if (!config.fixSlotDesyncEnabled) {return;}
				
				for (Player player : Bukkit.getOnlinePlayers())
				{
					sendItemUpdate(player, 5, player.getInventory().getHelmet());
					sendItemUpdate(player, 6, player.getInventory().getChestplate());
					sendItemUpdate(player, 7, player.getInventory().getLeggings());
					sendItemUpdate(player, 8, player.getInventory().getBoots());
				}
			}
		}, 0, 20);
	}

    public void sendItemUpdate(Player player, int slot, ItemStack item)
    {
    	PacketContainer updateslot = main.protocolManager.createPacket(Packets.Server.SET_SLOT);
    	updateslot.getIntegers().write(0, 0);
    	updateslot.getIntegers().write(1, slot);
    	updateslot.getItemModifier().write(0, item);
        try {
			main.protocolManager.sendServerPacket(player, updateslot);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    }
	
}
