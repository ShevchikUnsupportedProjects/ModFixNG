package modfixng.fixes;

import java.util.HashMap;

import modfixng.main.Config;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class ForgeMultipartPlaceFix implements Listener {

	private Config config;
	public ForgeMultipartPlaceFix(Config config) {
		this.config = config;
	}

	private HashMap<String, Block> blocksPlaced = new HashMap<String, Block>();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (!config.microblockFixEnabled) {
			return;
		}

		Block placed = event.getBlockPlaced();
		
		if (!event.isCancelled()) {
			if (placed.getTypeId() == config.microblockFixBlockID) {
				blocksPlaced.put(event.getPlayer().getName(), placed);
			}
		}

		if (event.isCancelled()) {
			ItemStack item = event.getPlayer().getItemInHand();
			if (item.getTypeId() == config.microblockFixItemID) {
				if (placed.getTypeId() != config.microblockFixBlockID) {
					blocksPlaced.get(event.getPlayer().getName()).setType(Material.AIR);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		blocksPlaced.remove(event.getPlayer().getName());
	}

}
