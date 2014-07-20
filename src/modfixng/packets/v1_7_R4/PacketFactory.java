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

package modfixng.packets.v1_7_R4;

import modfixng.packets.PacketFactoryInterface;

import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketContainer;

public class PacketFactory implements PacketFactoryInterface {

	@Override
	public Object getBlockDigPacket(Player player, PacketContainer container) {
		return new BlockDig(player, container.getHandle());
	}

	@Override
	public Object getWindowClosePacket(Player player, PacketContainer container) {
		int id = container.getIntegers().getValues().get(0);
		return new CloseInventory(player, id);
	}

	@Override
	public Object getWindowClickPacket(Player player, PacketContainer container) {
		return new InventoryClick(player, container.getHandle());
	}

}
