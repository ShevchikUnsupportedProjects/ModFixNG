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

import org.bukkit.entity.Player;

public class PlainNMSUtils {

	protected static boolean isInventoryOpen(Player p) {
		return getOpenInventoryId(p) != 0;
	}

	protected static String getOpenInventoryName(Player p) {
		return getPlayerContainer(p).getClass().getName();
	}

	protected static int getOpenInventoryId(Player p) {
		return getPlayerContainer(p).windowId;
	}

	protected static boolean isTryingToDropOpenCropanalyzer(Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		net.minecraft.server.v1_6_R3.ItemStack clickeditem = (net.minecraft.server.v1_6_R3.ItemStack) getPlayerContainer(p).b.get(minecraftslot);
		if (clickeditem.hasTag() && clickeditem.getTag().hasKey("uid")) {
			int clickeduid = clickeditem.getTag().getInt("uid");
			net.minecraft.server.v1_6_R3.Container container = getPlayerContainer(p);
			Field cropanalyzerField = container.getClass().getDeclaredField("cropnalyzer");
			cropanalyzerField.setAccessible(true);
			Object cropanalyzer = cropanalyzerField.get(container);
			Field itemStackField = cropanalyzer.getClass().getDeclaredField("itemStack");
			itemStackField.setAccessible(true);
			net.minecraft.server.v1_6_R3.ItemStack opencropanalyzeritemstack = (net.minecraft.server.v1_6_R3.ItemStack) itemStackField.get(cropanalyzer);
			int openuid = opencropanalyzeritemstack.getTag().getInt("uid");
			return openuid == clickeduid;
		}
		return false;
	}

	protected static boolean isTryingToDropOpenToolBox(Player p, int minecraftslot) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		net.minecraft.server.v1_6_R3.ItemStack clickeditem = (net.minecraft.server.v1_6_R3.ItemStack) getPlayerContainer(p).b.get(minecraftslot);
		if (clickeditem.hasTag() && clickeditem.getTag().hasKey("uid")) {
			int clickeduid = clickeditem.getTag().getInt("uid");
			net.minecraft.server.v1_6_R3.Container container = getPlayerContainer(p);
			Field tooboxField = container.getClass().getDeclaredField("Toolbox");
			tooboxField.setAccessible(true);
			Object toolbox = tooboxField.get(container);
			Field itemStackField = toolbox.getClass().getSuperclass().getDeclaredField("itemStack");
			itemStackField.setAccessible(true);
			net.minecraft.server.v1_6_R3.ItemStack opentoolbox = (net.minecraft.server.v1_6_R3.ItemStack) itemStackField.get(toolbox);
			int openuid = opentoolbox.getTag().getInt("uid");
			return openuid == clickeduid;
		}
		return false;
	}

	private static net.minecraft.server.v1_6_R3.Container getPlayerContainer(Player p) {
		org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer cplayer = (org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer) p;
		net.minecraft.server.v1_6_R3.EntityHuman nmshuman = cplayer.getHandle();
		return nmshuman.activeContainer;
	}

}
