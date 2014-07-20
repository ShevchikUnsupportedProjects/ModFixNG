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

package modfixng.packets.v1_6_R3;

import modfixng.packets.PacketFactoryInterface;

import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketContainer;

public class PacketFactory implements PacketFactoryInterface {

	@Override
	public PacketContainer getBlockDigPacket(Player player, PacketContainer container) {
		Object nmsPacket = new BlockDig(player);
		PacketContainer newpacket = new PacketContainer(container.getType(), nmsPacket);
		newpacket.getIntegers().write(0, container.getIntegers().read(0));
		newpacket.getIntegers().write(1, container.getIntegers().read(1));
		newpacket.getIntegers().write(2, container.getIntegers().read(2));
		newpacket.getIntegers().write(3, container.getIntegers().read(3));
		newpacket.getIntegers().write(4, container.getIntegers().read(4));
		return newpacket;
	}

	@Override
	public PacketContainer getWindowClosePacket(Player player, PacketContainer container) {
		Object nmsPacket = new CloseInventory(player, container.getIntegers().read(0));
		PacketContainer newpacket = new PacketContainer(container.getType(), nmsPacket);
		newpacket.getIntegers().write(0, container.getIntegers().read(0));
		return newpacket;
	}

	@Override
	public PacketContainer getWindowClickPacket(Player player, PacketContainer container) {
		Object nmsPacket = new InventoryClick(player);
		PacketContainer newpacket = new PacketContainer(container.getType(), nmsPacket);
		newpacket.getIntegers().write(0, container.getIntegers().read(0));
		newpacket.getIntegers().write(1, container.getIntegers().read(1));
		newpacket.getIntegers().write(2, container.getIntegers().read(2));
		newpacket.getIntegers().write(3, container.getIntegers().read(3));
		newpacket.getShorts().write(0, container.getShorts().read(0));
		newpacket.getItemModifier().write(0, container.getItemModifier().read(0));
		return newpacket;
	}

}
