package modfixng.fixes;

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import modfixng.main.ModFixNG;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FixForestryCraftingTable implements Listener, Feature {

	private String forestryCraftingTableInventoryName = "forestry.factory.gui.ContainerWorktable";

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInInventoryClick(ClickInventoryPacketClickInventoryEvent event) {
		if (NMSUtilsAccess.getNMSUtils().getOpenInventoryName(event.getPlayer()).equals(forestryCraftingTableInventoryName)) {
			for (ItemStack item : NMSUtilsAccess.getNMSUtils().getOpenInvetnoryItems(event.getPlayer())) {
				if (item.hasItemMeta()) {
					ItemMeta im = item.getItemMeta();
					if (im.hasLore() || im.hasDisplayName()) {
						item.setItemMeta(Bukkit.getItemFactory().getItemMeta(item.getType()));
					}
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
