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

import modfixng.events.BlockDigPacketItemDropEvent;
import net.minecraft.server.v1_7_R4.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R4.PacketPlayInListener;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockDig extends PacketPlayInBlockDig {

	private int a;
	private int b;
	private int c;
	private int face;
	private int e;

	private Player player;

	protected BlockDig(Player player, Object originalPacket) {
		this.player = player;
		PacketPlayInBlockDig packet = (PacketPlayInBlockDig) originalPacket;
		a = packet.c();
		b = packet.d();
		c = packet.e();
		face = packet.f();
		e = packet.g();
	}

	@Override
	public int c() {
		return a;
	}

	@Override
	public int d() {
		return b;
	}

	@Override
	public int e() {
		return c;
	}

	@Override
	public int f() {
		return face;
	}

	@Override
	public int g() {
		return e;
	}

	@Override
	public void a(PacketPlayInListener paramPacketPlayInListener) {
		CraftPlayer cplayer = (CraftPlayer) player;
		if (cplayer.getHandle().playerConnection.isDisconnected()) {
			return;
		}
		BlockDigPacketItemDropEvent event = new BlockDigPacketItemDropEvent(player);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
	    paramPacketPlayInListener.a(this);
	}

}
