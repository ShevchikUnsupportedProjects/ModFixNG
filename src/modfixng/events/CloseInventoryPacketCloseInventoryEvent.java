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

package modfixng.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CloseInventoryPacketCloseInventoryEvent extends PlayerEvent implements Cancellable {

	private int id;

	public CloseInventoryPacketCloseInventoryEvent(Player who, int id) {
		super(who);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	private static final HandlerList handlers = new HandlerList();

	private boolean cancelled = false;

    @Override
	public boolean isCancelled() {
        return cancelled;
    }

    @Override
	public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
	public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}