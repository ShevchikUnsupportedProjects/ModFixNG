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

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
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
    	PacketContainer updateslot = main.protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
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
