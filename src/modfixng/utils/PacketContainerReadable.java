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

package modfixng.utils;

public class PacketContainerReadable {

	public static class InventoryClick {
		public class PacketIndex {
			public static final int INVENTORY_ID = 0;
			public static final int BUTTON = 2;
			public static final int MODE = 3;
		}
		public class Mode {
			public static final int MOUSE_CLICK = 0;
			public static final int SHIFT_MOUSE_CLICK = 1;
			public static final int NUMBER_KEY_PRESS = 2;
			public static final int MIDDLE_MOUSE_CLICK = 3;
			public static final int DROP = 4;
			public static final int DRAG = 5;
			public static final int DOUBLE_MOUSE_CLICK = 6;
		}
	}

	public static class InventoryClose {
		public class PacketIndex {
			public static final int INVENTORY_ID = 0;
		}
	}

}
