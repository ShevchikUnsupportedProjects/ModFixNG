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
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class InventoryClick extends PacketPlayInWindowClick {

	private int a;
	private int slot;
	private int button;
	private short d;
	private ItemStack item;
	private int shift;

	private Player player;

	public InventoryClick(Player player, Object originalPacket) {
		this.player = player;
		PacketPlayInWindowClick packet = (PacketPlayInWindowClick) originalPacket;
		a = packet.c();
		slot = packet.d();
		button = packet.e();
		d = packet.f();
		item = packet.g();
		shift = packet.h();
	}

	@Override
	public int c() {
		return a;
	}

	@Override
	public int d() {
		return slot;
	}

	@Override
	public int e() {
		return button;
	}

	@Override
	public short f() {
		return d;
	}

	@Override
	public ItemStack g() {
		return item;
	}

	@Override
	public int h() {
		return shift;
	}

	@Override
	public void a(PacketPlayInListener paramPacketPlayInListener) {
		CraftPlayer cplayer = (CraftPlayer) player;
		if (cplayer.getHandle().playerConnection.isDisconnected()) {
			return;
		}
		ClickInventoryPacketClickInventoryEvent event = new ClickInventoryPacketClickInventoryEvent(player, a, slot, shift, button);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		paramPacketPlayInListener.a(this);
	}

}
