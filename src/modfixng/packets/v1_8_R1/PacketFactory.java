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

package modfixng.packets.v1_8_R1;

import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketContainer;

public class PacketFactory {

	protected static PacketContainer getBlockDigPacket(Player player, PacketContainer container) {
		Object nmsPacket = new BlockDig();
		PacketContainer newpacket = new PacketContainer(container.getType(), nmsPacket);
		newpacket.getIntegers().write(0, container.getIntegers().read(0));
		newpacket.getIntegers().write(1, container.getIntegers().read(1));
		newpacket.getIntegers().write(2, container.getIntegers().read(2));
		newpacket.getIntegers().write(3, container.getIntegers().read(3));
		newpacket.getIntegers().write(4, container.getIntegers().read(4));
		return newpacket;
	}

}