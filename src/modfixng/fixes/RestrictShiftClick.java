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

package modfixng.fixes;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.PacketContainerReadable;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.async.AsyncListenerHandler;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class RestrictShiftClick implements Feature {

	private Config config;

	public RestrictShiftClick(Config config) {
		this.config = config;
	}

	private AsyncListenerHandler listener;

	private void initShiftInventoryClickListener() {
		listener = ModFixNG.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(ModFixNG.getInstance(), PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (e.getPlayer() == null) {
						return;
					}

					final Player player = e.getPlayer();
					// check click type(checking for button)
					if (e.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.MODE) == PacketContainerReadable.InventoryClick.Mode.SHIFT_MOUSE_CLICK) {
						String invname = ModFixNGUtils.getOpenInventoryName(player);
						if (config.restrictShiftInvetoryNames.contains(invname)) {
							e.setCancelled(true);
							player.updateInventory();
						}
					}
				}
			}
		);
		listener.syncStart();
	}

	@Override
	public void load() {
		initShiftInventoryClickListener();
	}

	@Override
	public void unload() {
		ModFixNG.getProtocolManager().getAsynchronousManager().unregisterAsyncHandler(listener);
	}

	@Override
	public String getName() {
		return "InventoryShiftClickRestrict";
	}

}
