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

package modfixng.utils.v1_7_R2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import modfixng.utils.NMSUtilsInterface;
import net.minecraft.server.v1_7_R2.Container;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.IInventory;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.PlayerInventory;
import net.minecraft.server.v1_7_R2.Slot;
import net.minecraft.server.v1_7_R2.TileEntity;

import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;

public class NMSUtils implements NMSUtilsInterface {

	@Override
	public boolean hasInventory(org.bukkit.block.Block b) {
		CraftWorld cworld = (CraftWorld) b.getWorld();
		TileEntity te = cworld.getTileEntityAt(b.getX(), b.getY(), b.getZ());
		if ((te != null) && (te instanceof IInventory)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isInventoryOpen(org.bukkit.entity.Player p) {
		return getOpenInventoryId(p) != 0;
	}

	@Override
	public String getOpenInventoryName(org.bukkit.entity.Player p) {
		return getPlayerContainer(p).getClass().getName();
	}

	@Override
	public ArrayList<org.bukkit.inventory.ItemStack> getTopInvetnoryItems(org.bukkit.entity.Player p) {
		ArrayList<org.bukkit.inventory.ItemStack> items = new ArrayList<org.bukkit.inventory.ItemStack>();
		Container container = getPlayerContainer(p);
		@SuppressWarnings("unchecked")
		List<Slot> slots = container.c;
		for (Slot slot : slots) {
			if ((slot.getItem() != null) && !(slot.inventory instanceof PlayerInventory)) {
				items.add(CraftItemStack.asCraftMirror(slot.getItem()));
			}
		}
		return items;
	}

	@Override
	public boolean isContainerValid(org.bukkit.entity.Player p, int invid) {
		return getOpenInventoryId(p) == invid;
	}

	@Override
	public int getOpenInventoryId(org.bukkit.entity.Player p) {
		return getPlayerContainer(p).windowId;
	}

	@Override
	public boolean isTryingToDropOpenCropanalyzer(org.bukkit.entity.Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (!getOpenInventoryName(p).equals("ic2.core.item.tool.ContainerCropnalyzer")) {
			return false;
		}
		ItemStack clickeditem = (ItemStack) getPlayerContainer(p).b.get(minecraftslot);
		if (clickeditem.hasTag() && clickeditem.getTag().hasKey("uid")) {
			int clickeduid = clickeditem.getTag().getInt("uid");
			Container container = getPlayerContainer(p);
			Field cropanalyzerField = container.getClass().getDeclaredField("cropnalyzer");
			cropanalyzerField.setAccessible(true);
			Object cropanalyzer = cropanalyzerField.get(container);
			Field itemStackField = cropanalyzer.getClass().getDeclaredField("itemStack");
			itemStackField.setAccessible(true);
			ItemStack opencropanalyzeritemstack = (ItemStack) itemStackField.get(cropanalyzer);
			int openuid = opencropanalyzeritemstack.getTag().getInt("uid");
			return openuid == clickeduid;
		}
		return false;
	}

	@Override
	public boolean isTryingToDropOpenToolBox(org.bukkit.entity.Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (!getOpenInventoryName(p).equals("ic2.core.item.tool.ContainerToolbox")) {
			return false;
		}
		ItemStack clickeditem = (ItemStack) getPlayerContainer(p).b.get(minecraftslot);
		if (clickeditem.hasTag() && clickeditem.getTag().hasKey("uid")) {
			int clickeduid = clickeditem.getTag().getInt("uid");
			Container container = getPlayerContainer(p);
			Field tooboxField = container.getClass().getDeclaredField("Toolbox");
			tooboxField.setAccessible(true);
			Object toolbox = tooboxField.get(container);
			Field itemStackField = toolbox.getClass().getSuperclass().getDeclaredField("itemStack");
			itemStackField.setAccessible(true);
			ItemStack opentoolbox = (ItemStack) itemStackField.get(toolbox);
			int openuid = opentoolbox.getTag().getInt("uid");
			return openuid == clickeduid;
		}
		return false;
	}

	private Container getPlayerContainer(org.bukkit.entity.Player p) {
		CraftPlayer cplayer = (CraftPlayer) p;
		EntityHuman nmshuman = cplayer.getHandle();
		return nmshuman.activeContainer;
	}

}