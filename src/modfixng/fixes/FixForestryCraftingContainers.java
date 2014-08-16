package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import modfixng.main.ModFixNG;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class FixForestryCraftingContainers implements Listener, Feature {

	private HashSet<String> forestryInventoryNames = new HashSet<String>(
		Arrays.asList(
			new String[] {
				"forestry.factory.gui.ContainerWorktable"
			}
		)
	);

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInInventoryClick(ClickInventoryPacketClickInventoryEvent event) {
		if (forestryInventoryNames.contains(NMSUtilsAccess.getNMSUtils().getOpenInventoryName(event.getPlayer()))) {
			int slot = event.getSlot();
			if (NMSUtilsAccess.getNMSUtils().isTopInventoryClick(event.getPlayer(), slot)) {
				for (ItemStack item : NMSUtilsAccess.getNMSUtils().getTopInvetnoryItems(event.getPlayer())) {
					item.setItemMeta(null);
				}
			}
		}
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
	}

	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public String getName() {
		return "ForestryCraftingTableFix";
	}

}