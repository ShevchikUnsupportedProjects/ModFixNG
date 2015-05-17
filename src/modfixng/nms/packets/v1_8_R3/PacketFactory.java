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

package modfixng.nms.packets.v1_8_R3;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumDirection;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig.EnumPlayerDigType;

import org.bukkit.entity.Player;

import com.comphenix.protocol.events.PacketContainer;

public class PacketFactory {

	protected static PacketContainer getBlockDigPacket(Player player, PacketContainer container) {
		Object nmsPacket = new BlockDig();
		PacketContainer newpacket = new PacketContainer(container.getType(), nmsPacket);
		newpacket.getSpecificModifier(BlockPosition.class).write(0, container.getSpecificModifier(BlockPosition.class).read(0));
		newpacket.getSpecificModifier(EnumDirection.class).write(0, container.getSpecificModifier(EnumDirection.class).read(0));
		newpacket.getSpecificModifier(EnumPlayerDigType.class).write(0, container.getSpecificModifier(EnumPlayerDigType.class).read(0));
		return newpacket;
	}

}