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

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import net.minecraft.server.v1_6_R3.Connection;
import net.minecraft.server.v1_6_R3.Packet102WindowClick;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class InventoryClick extends Packet102WindowClick {

	private Player player;

	public InventoryClick(Player player) {
		this.player = player;
	}

	@Override
	public void handle(Connection paramConnection) {
		CraftPlayer cplayer = (CraftPlayer) player;
		if (cplayer.getHandle().playerConnection.disconnected) {
			return;
		}
		ClickInventoryPacketClickInventoryEvent event = new ClickInventoryPacketClickInventoryEvent(player, a, slot, shift, button);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		paramConnection.a(this);
	}

}
