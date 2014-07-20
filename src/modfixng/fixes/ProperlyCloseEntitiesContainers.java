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
import java.util.HashMap;
import java.util.HashSet;

import modfixng.events.CloseInventoryPacketCloseInventoryEvent;
import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

public class ProperlyCloseEntitiesContainers implements Listener, Feature {

	private Config config;
	public ProperlyCloseEntitiesContainers(Config config) {
		this.config = config;
	}

	private HashMap<String, Entity> playerOpenEntity = new HashMap<String, Entity>(200);

	private HashSet<EntityType> knownEntityTypes  = new HashSet<EntityType>(
		Arrays.asList(
			new EntityType[] {
				//vanilla entities that has inventories
				EntityType.MINECART_CHEST,
				EntityType.MINECART_FURNACE,
				EntityType.MINECART_HOPPER,
				EntityType.VILLAGER,
				EntityType.HORSE
			}
		)
	);
	// add player to list when he opens entity inventory
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerOpenedEntity(PlayerInteractEntityEvent e) {
		Player player = e.getPlayer();
		String playername = player.getName();

		if (playerOpenEntity.containsKey(playername)) {
			if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(player)) {
				e.setCancelled(true);
				return;
			}
		}

		final Entity entity = e.getRightClicked();
		if (config.properlyCloseEntitiesContainersEntitiesTypes.contains(entity.getType().toString()) || knownEntityTypes.contains(entity.getType())) {
			playerOpenEntity.put(playername, entity);
		}
	}

	private PacketListener plistener;
	// remove player from list when he closes inventory
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPacketInInventoryClose(CloseInventoryPacketCloseInventoryEvent event) {
		removeData(event.getPlayer().getName());
	}
	private void initServerCloseInventoryFixListener() {
		plistener = new PacketAdapter(
			PacketAdapter
			.params(ModFixNG.getInstance(), PacketType.Play.Server.CLOSE_WINDOW)
		) {
			@Override
			public void onPacketSending(PacketEvent e) {
				removeData(e.getPlayer().getName());
			}
		};
		ModFixNG.getProtocolManager().addPacketListener(plistener);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e) {
		removeData(e.getPlayer().getName());
	}

	private void removeData(String name) {
		playerOpenEntity.remove(name);
	}

	private BukkitTask task;
	// check if entity is not valid or player is too far away from it, if yes -  force close inventory
	private void initEntitiesCheck() {
		task = Bukkit.getScheduler().runTaskTimer(
			ModFixNG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					for (Player player : Bukkit.getOnlinePlayers()) {
						String playername = player.getName();
						if (playerOpenEntity.containsKey(playername)) {
							Entity entity = playerOpenEntity.get(playername);
							if (!entity.isValid() || entity.getWorld() != player.getWorld() || entity.getLocation().distanceSquared(player.getLocation()) > 36) {
								playerOpenEntity.remove(playername);
								player.closeInventory();
							}
						}
					}
				}
			},
			0, 1
		);
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
		initServerCloseInventoryFixListener();
		initEntitiesCheck();
	}

	@Override
	public void unload() {
		task.cancel();
		ModFixNG.getProtocolManager().removePacketListener(plistener);
		HandlerList.unregisterAll(this);
		for (Player player : Bukkit.getOnlinePlayers()) {
			String playername = player.getName();
			if (playerOpenEntity.containsKey(playername)) {
				playerOpenEntity.remove(player.getName());
				player.closeInventory();
			}
		}
	}

	@Override
	public String getName() {
		return "ProperlyCloseEntitiesContainers";
	}

}
