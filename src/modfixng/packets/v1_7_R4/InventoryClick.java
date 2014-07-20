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

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import net.minecraft.server.v1_7_R4.ItemStack;
import net.minecraft.server.v1_7_R4.PacketPlayInListener;
import net.minecraft.server.v1_7_R4.PacketPlayInWindowClick;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class InventoryClick extends PacketPlayInWindowClick {

	private int fielda;
	private int fieldslot;
	private int fieldbutton;
	private short fieldd;
	private ItemStack fielditem;
	private int fieldshift;

	private Player player;

	public InventoryClick(Player player, Object originalPacket) {
		this.player = player;
		PacketPlayInWindowClick packet = (PacketPlayInWindowClick) originalPacket;
		fielda = packet.c();
		fieldslot = packet.d();
		fieldbutton = packet.e();
		fieldd = packet.f();
		fielditem = packet.g();
		fieldshift = packet.h();
	}

	@Override
	public int c() {
		return fielda;
	}

	@Override
	public int d() {
		return fieldslot;
	}

	@Override
	public int e() {
		return fieldbutton;
	}

	@Override
	public short f() {
		return fieldd;
	}

	@Override
	public ItemStack g() {
		return fielditem;
	}

	@Override
	public int h() {
		return fieldshift;
	}

	@Override
	public void a(PacketPlayInListener paramPacketPlayInListener) {
		CraftPlayer cplayer = (CraftPlayer) player;
		if (cplayer.getHandle().playerConnection.isDisconnected()) {
			return;
		}
		ClickInventoryPacketClickInventoryEvent event = new ClickInventoryPacketClickInventoryEvent(player, fielda, fieldslot, fieldshift, fieldbutton);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		paramPacketPlayInListener.a(this);
	}

}
