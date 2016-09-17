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

package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.nms.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FixBag implements Listener, Feature {

	private Config config;

	public FixBag(Config config) {
		this.config = config;
	}

	// close inventory on death and fix dropped items
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();

		p.closeInventory();

		List<ItemStack> drops = event.getDrops();
		drops.clear();
		try {
			for (ItemStack item : p.getInventory().getContents()) {
				if (item != null) {
					drops.add(item);
				}
			}
			for (ItemStack armor : p.getInventory().getArmorContents()) {
				if (armor != null) {
					drops.add(armor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// restrict using 1-9 buttons in bags inventories if it will move bag to another slot
	private final HashSet<String> knownInventoryNames = new HashSet<String>(
		Arrays.asList(
			new String[] {
				//forestry
				"forestry.storage.gui.ContainerNaturalistBackpack",
				"forestry.storage.gui.ContainerBackpack",
				"forestry.mail.gui.ContainerLetter",
				"forestry.apiculture.gui.ContainerBeealyzer",
				"forestry.arboriculture.gui.ContainerTreealyzer",
				"forestry.lepidopterology.gui.ContainerFlutterlyzer"
			}
		)
	);

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInInvetory19Click(InventoryClickEvent event) {
		if (!config.fixBag19ButtonClickEnabled) {
			return;
		}

		if (event.getClick() == ClickType.NUMBER_KEY) {
			Player player = (Player) event.getWhoClicked();
			final int heldslot = player.getInventory().getHeldItemSlot();
			if (heldslot == event.getSlot()) {
				String inventoryName = NMSUtilsAccess.getNMSUtils().getOpenInventoryName(player);
				if (config.fixBag19ButtonClickBagInventoryNames.contains(inventoryName) || knownInventoryNames.contains(inventoryName)) {
					event.setCancelled(true);
					player.updateInventory();
				}
			}
		}
	}

	// close inventory if trying to drop opened toolbox or cropnalyzer(q button in inventory)
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPacketInInventoryDropClick(InventoryClickEvent event) {
		if (!config.fixBagCropanalyzerFixEnabled && !config.fixBagToolboxFixEnabled) {
			return;
		}
		if ((event.getClick() == ClickType.DROP) || (event.getClick() == ClickType.CONTROL_DROP)) {
			Player player = (Player) event.getWhoClicked();
			if (isInvalidDropInventory(player, event.getRawSlot())) {
				event.setCancelled(true);
				player.updateInventory();
			}
		}
	}

	private boolean isInvalidDropInventory(Player player, int slot) {
		if (config.fixBagCropanalyzerFixEnabled) {
			try {
				if (NMSUtilsAccess.getNMSUtils().isTryingToDropOpenCropanalyzer(player, slot)) {
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (config.fixBagToolboxFixEnabled) {
			try {
				if (NMSUtilsAccess.getNMSUtils().isTryingToDropOpenToolBox(player, slot)) {
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return false;
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
		return "BagFix";
	}

}