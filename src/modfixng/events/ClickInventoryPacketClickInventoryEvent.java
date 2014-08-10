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

public class ClickInventoryPacketClickInventoryEvent extends PlayerEvent implements Cancellable {

	private int id;
	private int slot;
	private int mode;
	private int button;

	public ClickInventoryPacketClickInventoryEvent(Player who, int id, int slot, int mode, int button) {
		super(who);
		this.id = id;
		this.slot = slot;
		this.mode = mode;
		this.button = button;
	}

	public int getId() {
		return id;
	}

	public int getSlot() {
		return slot;
	}

	public Mode getMode() {
		return Mode.getByInt(mode);
	}

	public int getButton() {
		return button;
	}

	public static enum Mode {
		MOUSE_CLICK, SHIFT_MOUSE_CLICK, NUMBER_KEY_PRESS, DROP, UNKNOWN;
		public static Mode getByInt(int mode) {
			switch (mode) {
				case 0: {
					return MOUSE_CLICK;
				}
				case 1: {
					return SHIFT_MOUSE_CLICK;
				}
				case 2: {
					return NUMBER_KEY_PRESS;
				}
				case 4: {
					return DROP;
				}
				default: {
					return UNKNOWN;
				}
			}
		}
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