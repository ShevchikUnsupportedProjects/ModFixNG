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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class MFRP2FixListener implements Listener {
	
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFRP2FixListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}
	
	//check if we have RP2 wires near the redstone wire and then break it if so
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onRedstoneUpdate(BlockRedstoneEvent e)
	{
		if (!config.enableRP2wiresfix) {return;}
		
		Block b = e.getBlock();
		if (b.getType() == Material.REDSTONE_WIRE)
		{
			if (isRP2WiresNear(b))
			{
				b.breakNaturally();
			}
		}
	}
	
	private boolean isRP2WiresNear(Block b)
	{	
		return 
				isRP2WiresContactsBlockFace(BlockFace.EAST,b) || isRP2WiresContactsBlockFace(BlockFace.WEST,b) ||
				isRP2WiresContactsBlockFace(BlockFace.NORTH,b) || isRP2WiresContactsBlockFace(BlockFace.SOUTH,b) ||
				isRP2WiresContactsBlockFace(BlockFace.UP,b) || isRP2WiresContactsBlockFace(BlockFace.DOWN,b)
		;

	}
	
	private boolean isRP2WiresContactsBlockFace(BlockFace bf, Block b)
	{
		return (config.RP2WiresIDs.contains(Utils.getIDstring(b.getRelative(bf,1))));
	}

}
