package modfixng.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlainNMSUtils {

	protected static boolean isInventoryOpen(Player p)
	{
		return !getPlayerContainer(p).getClass().getName().equals("net.minecraft.inventory.ContainerPlayer");
	}
	
    protected static void findAndFixOpenCropanalyzer(Player p, List<ItemStack> drops) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
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
	
	
	protected static net.minecraft.server.v1_5_R3.Container getPlayerContainer(Player p)
    {
    	org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer cplayer = (org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer) p;
		net.minecraft.server.v1_5_R3.EntityPlayer nmsplayer = cplayer.getHandle();
		net.minecraft.server.v1_5_R3.EntityHuman nmshuman = (net.minecraft.server.v1_5_R3.EntityHuman) nmsplayer;
		return nmshuman.activeContainer;
    }
	
    protected static net.minecraft.server.v1_5_R3.ItemStack getNMSItemStack(ItemStack i) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
    {
		Field handleField = i.getClass().getDeclaredField("handle");
		handleField.setAccessible(true);
		return  (net.minecraft.server.v1_5_R3.ItemStack) handleField.get(i);
    }
	
	
}
