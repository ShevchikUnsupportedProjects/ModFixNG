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

package modfixng.packets.v1_7_R3;

import modfixng.events.BlockDigPacketItemDropEvent;
import net.minecraft.server.v1_7_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_7_R3.PacketPlayInListener;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockDig extends PacketPlayInBlockDig {

	private int fielda;
	private int fieldb;
	private int fieldc;
	private int fieldface;
	private int fielde;

	private Player player;

	protected BlockDig(Player player, Object originalPacket) {
		this.player = player;
		PacketPlayInBlockDig packet = (PacketPlayInBlockDig) originalPacket;
		fielda = packet.c();
		fieldb = packet.d();
		fieldc = packet.e();
		fieldface = packet.f();
		fielde = packet.g();
	}

	@Override
	public int c() {
		return fielda;
	}

	@Override
	public int d() {
		return fieldb;
	}

	@Override
	public int e() {
		return fieldc;
	}

	@Override
	public int f() {
		return fieldface;
	}

	@Override
	public int g() {
		return fielde;
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
