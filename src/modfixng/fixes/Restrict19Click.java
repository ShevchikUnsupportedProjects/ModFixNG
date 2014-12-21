package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Restrict19Click implements Feature, Listener {

	private Config config;

	public Restrict19Click(Config config) {
		this.config = config;
	}

	private HashSet<String> knownInvNames = new HashSet<String>(
		Arrays.asList(
			"ic2.core.block.wiring.ContainerElectricBlock"
		)
	);

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getClick() == ClickType.NUMBER_KEY) {
			Player player = (Player) event.getWhoClicked();
			String invname = NMSUtilsAccess.getNMSUtils().getOpenInventoryName(player);
			if (knownInvNames.contains(invname) || config.restrict19InvetoryNames.contains(invname)) {
				event.setCancelled(true);
				player.updateInventory();
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
		return "Inventory19ClickRestrict";
	}

}