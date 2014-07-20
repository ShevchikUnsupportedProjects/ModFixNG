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

package modfixng.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface NMSUtilsInterface {

	public boolean hasInventory(Block b);

	public boolean isInventoryOpen(Player p);

	public String getOpenInventoryName(Player p);

	public boolean isContainerValid(Player p, int invid);

	public int getOpenInventoryId(Player p);

	public boolean isTryingToDropOpenCropanalyzer(Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

	public boolean isTryingToDropOpenToolBox(Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException;

}
