package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;

import modfixng.main.ModFixNG;
import modfixng.nms.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FixForestryCraftingContainers implements Listener, Feature {

	private HashSet<String> forestryInventoryNames = new HashSet<String>(
		Arrays.asList(
			new String[] {
				"forestry.factory.gui.ContainerWorktable",
				"forestry.factory.gui.ContainerCarpenter"
			}
		)
	);

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (forestryInventoryNames.contains(NMSUtilsAccess.getNMSUtils().getOpenInventoryName(player))) {
			for (ItemStack item : NMSUtilsAccess.getNMSUtils().getTopInvetnoryItems(player)) {
				item.setItemMeta(null);
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