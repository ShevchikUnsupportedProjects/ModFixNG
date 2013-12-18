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

import java.lang.reflect.Method;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.utility.MinecraftReflection;

public class Utils {

	
	public static String getIDstring(Block bl)
	{
		String blstring = String.valueOf(bl.getTypeId());
		if (bl.getData() !=0) {blstring += ":"+bl.getData();}
		return blstring;
	}
	
	public static String getIDstring(ItemStack item)
	{
		String blstring = String.valueOf(item.getTypeId());
		if (item.getDurability() !=0) {blstring += ":"+item.getDurability();}
		return blstring;
	}
	
	
	public static boolean hasInventory(Block b)
	{
		try {
			World world = b.getWorld();
			Method getTEMethod = world.getClass().getDeclaredMethod("getTileEntityAt", int.class, int.class, int.class);
			getTEMethod.setAccessible(true);
			Object te = getTEMethod.invoke(world, b.getX(), b.getY(), b.getZ());
			Class<?> invclass = MinecraftReflection.getMinecraftClass("IInventory");
			if (te != null && invclass.isAssignableFrom(te.getClass()))
			{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
