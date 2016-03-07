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
import org.bukkit.block.Block;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import modfixng.main.ModFixNG;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;

public class ModFixNGUtils {

	@SuppressWarnings("deprecation")
	public static String getMaterialString(Block bl) {
		String blstring = String.valueOf(bl.getType().toString());
		if (bl.getData() != 0) {
			blstring += ":" + bl.getData();
		}
		return blstring;
	}

	public static void updateSlot(Player player, int inventory, int minecraftslot, ItemStack item) {
		PacketContainer updateslot = ModFixNG.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
		updateslot.getIntegers().write(0, inventory);
		updateslot.getIntegers().write(1, minecraftslot);
		updateslot.getItemModifier().write(0, item);
		try {
			ModFixNG.getProtocolManager().sendServerPacket(player, updateslot);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static boolean isBeaconEffectValid(int effectId) {
		switch (effectId) {
			case 0:
			case 1:
			case 3:
			case 11:
			case 8:
			case 5:
			case 10: {
				return true;
			}
			default: {
				return false;
			}
		}
	}

	private static Method getOnlinePlayers;
	@SuppressWarnings("unchecked")
	public static Collection<Player> getOnlinePlayers() {
		if (getOnlinePlayers == null) {
			try {
				getOnlinePlayers = Bukkit.class.getDeclaredMethod("getOnlinePlayers");
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException("WTF?", e);
			}
		}
		try {
			Object obj = getOnlinePlayers.invoke(null);
			if (obj instanceof Collection) {
				return (Collection<Player>) obj;
			} else if (obj.getClass().isArray()) {
				return Arrays.asList((Player[]) obj);
			} else {
				throw new RuntimeException("Unknown return type of getOnlinePlayers method");
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("WTF?", e);
		}
	}

	private static boolean runningMCPC = false;
	public static void checkMCPC() {
		runningMCPC = MinecraftReflection.getEntityPlayerClass().getName().equals("net.minecraft.entity.player.EntityPlayerMP");
	}
	public static boolean isRunningMCPC() {
		return runningMCPC;
	}

}