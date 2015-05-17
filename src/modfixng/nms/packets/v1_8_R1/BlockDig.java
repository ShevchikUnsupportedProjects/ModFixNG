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

package modfixng.nms.packets.v1_8_R1;

import modfixng.events.BlockDigPacketItemDropEvent;
import net.minecraft.server.v1_8_R1.EnumPlayerDigType;
import net.minecraft.server.v1_8_R1.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R1.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;

public class BlockDig extends PacketPlayInBlockDig {

	@Override
	public void a(PacketListenerPlayIn packetplayinlistener) {
		if (packetplayinlistener instanceof PlayerConnection) {
			CraftPlayer cplayer = ((PlayerConnection) packetplayinlistener).getPlayer();
			if (cplayer.getHandle().playerConnection.isDisconnected()) {
				return;
			}
			if ((c() == EnumPlayerDigType.DROP_ITEM) || (c() == EnumPlayerDigType.DROP_ALL_ITEMS)) {
				BlockDigPacketItemDropEvent event = new BlockDigPacketItemDropEvent(cplayer);
				Bukkit.getPluginManager().callEvent(event);
				if (event.isCancelled()) {
					return;
				}
			}
		}
		packetplayinlistener.a(this);
	}

}