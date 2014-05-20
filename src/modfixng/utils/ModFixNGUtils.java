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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import modfixng.main.ModFixNG;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;

public class ModFixNGUtils {

	public static String getIDstring(Block bl) {
		String blstring = String.valueOf(bl.getTypeId());
		if (bl.getData() != 0) {
			blstring += ":" + bl.getData();
		}
		return blstring;
	}

	public static String getIDstring(ItemStack item) {
		String blstring = String.valueOf(item.getTypeId());
		if (item.getDurability() != 0) {
			blstring += ":" + item.getDurability();
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

	private static Class<?> invclass = MinecraftReflection.getMinecraftClass("IInventory");
	public static boolean hasInventory(Block b) {
		try {
			World world = b.getWorld();
			Method getTEMethod = world.getClass().getDeclaredMethod("getTileEntityAt", int.class, int.class, int.class);
			getTEMethod.setAccessible(true);
			Object te = getTEMethod.invoke(world, b.getX(), b.getY(), b.getZ());
			if (te != null && invclass.isAssignableFrom(te.getClass())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getOpenInventoryName(Player p) {
		if (isRunningMCPC()) {
			return PlainNMSUtils.getOpenInventoryName(p);
		} else {
			if (isInventoryOpen(p)) {
				return p.getOpenInventory().getTopInventory().getHolder().getClass().getName();
			} else {
				return p.getInventory().getClass().getName();
			}
		}
	}

	public static boolean isInventoryOpen(Player p) {
		if (isRunningMCPC()) {
			return PlainNMSUtils.isInventoryOpen(p);
		} else {
			if (p.getGameMode() != GameMode.CREATIVE) {
				return p.getOpenInventory().getType() != InventoryType.CRAFTING;
			} else {
				return p.getOpenInventory().getType() != InventoryType.CREATIVE;
			}
		}
	}

	public static boolean isContainerValid(int invid , Player p) {
		if (isRunningMCPC()) {
			return PlainNMSUtils.getOpenInventoryId(p) == invid;
		} else {
			if (ModFixNGUtils.isInventoryOpen(p)) {
				return invid != 0;
			} else {
				return invid == 0;
			}
		}
	}

	public static ItemStack getItemStackWrapperCopy(ItemStack item) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return MinecraftReflection.getBukkitItemStack(getNMSItemStack(item));
	}

	public static boolean isTryingToDropOpenCropanalyzer(Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (isRunningMCPC() && PlainNMSUtils.getOpenInventoryName(p).equals("ic2.core.item.tool.ContainerCropnalyzer")) {
			return PlainNMSUtils.isTryingToDropOpenCropanalyzer(p, minecraftslot);
		}
		return false;
	}

	public static boolean isTryingToDropOpenToolBox(Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (isRunningMCPC() && PlainNMSUtils.getOpenInventoryName(p).equals("ic2.core.item.tool.ContainerToolbox")) {
			return PlainNMSUtils.isTryingToDropOpenToolBox(p, minecraftslot);
		}
		return false;
	}

	private static Object getNMSItemStack(ItemStack item) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = item.getClass().getDeclaredField("handle");
		f.setAccessible(true);
		return f.get(item);
	}

	private static boolean runningMCPC = false;
	public static void checkMCPC() {
		runningMCPC = MinecraftReflection.getEntityPlayerClass().getName().equals("net.minecraft.entity.player.EntityPlayerMP");
	}
	public static boolean isRunningMCPC() {
		return runningMCPC;
	}

}