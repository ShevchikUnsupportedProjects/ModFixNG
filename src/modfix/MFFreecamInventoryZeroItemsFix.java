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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MFFreecamInventoryZeroItemsFix {

	private ModFixNG main;
	private Config config;

	MFFreecamInventoryZeroItemsFix(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		initInvCheck();
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
	
}
