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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
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

	public static void updateSlot(ProtocolManager protocolManager, Player player, int inventory, int slot, ItemStack item) {
		PacketContainer updateslot = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
		updateslot.getIntegers().write(0, inventory);
		updateslot.getIntegers().write(1, slot);
		updateslot.getItemModifier().write(0, item);
		try {
			protocolManager.sendServerPacket(player, updateslot);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static boolean hasInventory(Block b) {
		try {
			World world = b.getWorld();
			Method getTEMethod = world.getClass().getDeclaredMethod("getTileEntityAt", int.class, int.class, int.class);
			getTEMethod.setAccessible(true);
			Object te = getTEMethod.invoke(world, b.getX(), b.getY(), b.getZ());
			Class<?> invclass = MinecraftReflection.getMinecraftClass("IInventory");
			if (te != null && invclass.isAssignableFrom(te.getClass())) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
	
	public static boolean isClickValid(int invid, Player p) {
		if (isRunningMCPC()) {
			if (PlainNMSUtils.getPlayerContainer(p).windowId != invid) {
				return false;
			}
		} else {
			if (ModFixNGUtils.isInventoryOpen(p)) {
				if (invid == 0) {
					return false;
				}
			} else {
				if (invid != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isCropanalyzerOpen(Player p) {
		if (isRunningMCPC()) {
			return PlainNMSUtils.getPlayerContainer(p).getClass().getName().equals("ic2.core.item.tool.ContainerCropnalyzer");
		}
		return false;
	}

	public static boolean isTryingToDropOpenCropanalyzer(Player p, ItemStack item) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return PlainNMSUtils.isTryingToDropOpenCropanalyzer(p, item);
	}

	public static void findAndFixOpenCropanalyzer(Player p, List<ItemStack> drops) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		PlainNMSUtils.findAndFixOpenCropanalyzer(p, drops);
	}

	public static boolean isToolboxOpen(Player p) {
		if (isRunningMCPC()) {
			return PlainNMSUtils.getPlayerContainer(p).getClass().getName().equals("ic2.core.item.tool.ContainerToolbox");
		}
		return false;
	}

	public static boolean isTryingToDropOpenToolBox(Player p, ItemStack item)throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		return PlainNMSUtils.isTryingToDropOpenToolBox(p, item);
	}

	public static boolean isElectricContainerOpen(Player p) {
		if (isRunningMCPC()) {
			return PlainNMSUtils.getPlayerContainer(p).getClass().getName().equals("ic2.core.block.wiring.ContainerElectricBlock");
		}
		return false;
	}

	private static boolean isRunningMCPC() {
		return (MinecraftReflection.getEntityPlayerClass().getName().equals("net.minecraft.entity.player.EntityPlayerMP"));
	}

}