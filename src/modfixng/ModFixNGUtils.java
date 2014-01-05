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

package modfixng;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;

public class ModFixNGUtils {

	
	public static String getIDstring(Block bl)
	{
		String blstring = String.valueOf(bl.getTypeId());
		if (bl.getData() !=0) {blstring += ":"+bl.getData();}
		return blstring;
	}
	
	public static String getIDstring(ItemStack item)
	{
		String blstring = String.valueOf(item.getTypeId());
		if (item.getDurability() !=0) {blstring += ":"+item.getDurability();}
		return blstring;
	}
	
    public static void updateSlot(ProtocolManager protocolManager, Player player, int inventory, int slot, ItemStack item)
    {
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
	
	public static boolean hasInventory(Block b)
	{
		try {
			World world = b.getWorld();
			Method getTEMethod = world.getClass().getDeclaredMethod("getTileEntityAt", int.class, int.class, int.class);
			getTEMethod.setAccessible(true);
			Object te = getTEMethod.invoke(world, b.getX(), b.getY(), b.getZ());
			Class<?> invclass = MinecraftReflection.getMinecraftClass("IInventory");
			if (te != null && invclass.isAssignableFrom(te.getClass()))
			{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isWrench(ItemStack i)
	{
		if (isRunningMCPC())
		{
			Object onmsi = MinecraftReflection.getMinecraftItemStack(i);
			if (onmsi != null)
			{
				net.minecraft.server.v1_5_R3.ItemStack nmsi = CraftItemStack.asNMSCopy(i);
				if (nmsi.getName().toLowerCase().contains("wrench"))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isInventoryOpen(Player p)
	{
		if (isRunningMCPC())
		{
			return !getPlayerContainer(p).getClass().getName().equals("net.minecraft.inventory.ContainerPlayer");
		} else 
		{
			if (p.getGameMode() != GameMode.CREATIVE)
			{
				return p.getOpenInventory().getType() != InventoryType.CRAFTING;
			} else
			{
				return p.getOpenInventory().getType() != InventoryType.CREATIVE;
			}
		}
	}
    
    public static boolean isCropanalyzerOpen(Player p)
    {
    	if (isRunningMCPC())
		{
			return getPlayerContainer(p).getClass().getName().equals("ic2.core.item.tool.ContainerCropnalyzer");
		}
    	return false;
    }
    
    public static void findAndFixOpenCropanalyzer(Player p, List<ItemStack> drops) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
	    net.minecraft.server.v1_5_R3.Container container = getPlayerContainer(p);
		Field cropanalyzerField = container.getClass().getDeclaredField("cropnalyzer");
		cropanalyzerField.setAccessible(true);
		Object cropanalyzer = cropanalyzerField.get(container);
		Field itemStackField = cropanalyzer.getClass().getDeclaredField("itemStack");
		itemStackField.setAccessible(true);
		Field inventoryField = cropanalyzer.getClass().getDeclaredField("inventory");
		inventoryField.setAccessible(true);
		net.minecraft.server.v1_5_R3.ItemStack[] oldcropanalyzerinventory = (net.minecraft.server.v1_5_R3.ItemStack[]) inventoryField.get(cropanalyzer);
		net.minecraft.server.v1_5_R3.ItemStack oldcropanalyzeritemstack = (net.minecraft.server.v1_5_R3.ItemStack) itemStackField.get(cropanalyzer);
		int cropanalyzeritemstackuid = oldcropanalyzeritemstack.getTag().getInt("uid");
		for (ItemStack item : drops)
		{
			if (item.getTypeId() == oldcropanalyzeritemstack.id)
			{
				net.minecraft.server.v1_5_R3.ItemStack nmsi = getNMSItemStack(item);
				if (nmsi.getTag().hasKey("uid"))
				{
					int nmsiuid = nmsi.getTag().getInt("uid");
					if (nmsiuid == cropanalyzeritemstackuid)
					{
						net.minecraft.server.v1_5_R3.NBTTagCompound cropanalyzeritemstacktagcompound = new net.minecraft.server.v1_5_R3.NBTTagCompound();
						net.minecraft.server.v1_5_R3.NBTTagList taglist = new net.minecraft.server.v1_5_R3.NBTTagList();
					    for (int i = 0; i < oldcropanalyzerinventory.length; i++)
					    {
					    	net.minecraft.server.v1_5_R3.ItemStack itemstack = oldcropanalyzerinventory[i];
					    	if (itemstack != null)
					    	{
								net.minecraft.server.v1_5_R3.NBTTagCompound nbtTagCompoundSlot = new net.minecraft.server.v1_5_R3.NBTTagCompound();
								nbtTagCompoundSlot.setByte("Slot", (byte) i);
								itemstack.save(nbtTagCompoundSlot);
								taglist.add(nbtTagCompoundSlot);
					    	}
					    }
						cropanalyzeritemstacktagcompound.set("Items", taglist);
						nmsi.setTag(cropanalyzeritemstacktagcompound);
						return;
					}
				}	
			}
		}
    }
    
    
    private static boolean isRunningMCPC()
    {
    	return (MinecraftReflection.getEntityPlayerClass().getName().equals("net.minecraft.entity.player.EntityPlayerMP"));
    }
    
    private static net.minecraft.server.v1_5_R3.Container getPlayerContainer(Player p)
    {
    	org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer cplayer = (org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer) p;
		net.minecraft.server.v1_5_R3.EntityPlayer nmsplayer = cplayer.getHandle();
		net.minecraft.server.v1_5_R3.EntityHuman nmshuman = (net.minecraft.server.v1_5_R3.EntityHuman) nmsplayer;
		return nmshuman.activeContainer;
    }
    
    private static net.minecraft.server.v1_5_R3.ItemStack getNMSItemStack(ItemStack i) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
		Field handleField = i.getClass().getDeclaredField("handle");
		handleField.setAccessible(true);
		return  (net.minecraft.server.v1_5_R3.ItemStack) handleField.get(i);
    }
    
}