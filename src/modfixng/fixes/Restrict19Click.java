package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import modfixng.events.ClickInventoryPacketClickInventoryEvent.Mode;
import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

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
	public void onPacketInInventoryClick(ClickInventoryPacketClickInventoryEvent event) {
		if (event.getMode() == Mode.NUMBER_KEY_PRESS) {
			String invname = ModFixNGUtils.getOpenInventoryName(event.getPlayer());
			if (knownInvNames.contains(invname) || config.restrict19InvetoryNames.contains(invname)) {
				event.setCancelled(true);
				event.getPlayer().updateInventory();
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