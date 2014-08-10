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

import org.bukkit.Bukkit;

public class NMSUtilsAccess {

	private static NMSUtilsInterface nmsutils;

	private static Throwable error;

	public static Throwable getError() {
		return error;
	}

	public static boolean init() {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		String nmspackageversion = packageName.substring(packageName.lastIndexOf('.') + 1);
		try {
			String versioned = NMSUtilsAccess.class.getPackage().getName()+"."+nmspackageversion+".";
			nmsutils = (NMSUtilsInterface) Class.forName(versioned+"NMSUtils").newInstance();
		} catch (Throwable t) {
			error = t;
			return false;
		}
		return true;
	}

	public static NMSUtilsInterface getNMSUtils() {
		return nmsutils;
	}

}