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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;

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
import com.comphenix.protocol.async.AsyncListenerHandler;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

public class ProperlyCloseEntitiesContainers implements Listener, Feature {

	private Config config;
	public ProperlyCloseEntitiesContainers(Config config) {
		this.config = config;
	}

	private LinkedHashMap<Player, Entity> playerOpenEntity = new LinkedHashMap<Player, Entity>(200);

	private HashSet<EntityType> knownEntityTypes  = new HashSet<EntityType>(
		Arrays.asList(
			new EntityType[] {
				//vanilla entities that has inventories
				EntityType.MINECART_CHEST,
				EntityType.MINECART_FURNACE,
				EntityType.MINECART_HOPPER,
				EntityType.VILLAGER
			}
		)
	);
	// add player to list when he opens entity inventory
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerOpenedEntity(PlayerInteractEntityEvent e) {
		final Player player = e.getPlayer();

		if (playerOpenEntity.containsKey(player)) {
			if (ModFixNGUtils.isInventoryOpen(player)) {
				e.setCancelled(true);
				return;
			}
		}

		final Entity entity = e.getRightClicked();
		if (config.fixFreecamEntitiesEntitiesIDs.contains(entity.getType().getTypeId()) || knownEntityTypes.contains(entity.getType()) || entity.getType().toString().equals("HORSE")) {
			playerOpenEntity.put(player, entity);
		}
	}

	private AsyncListenerHandler alistener;
	private PacketListener plistener;
	// remove player from list when he closes inventory
	private void initClientCloseInventoryFixListener() {
		alistener = ModFixNG.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(ModFixNG.getInstance(), PacketType.Play.Client.CLOSE_WINDOW)
			) {
				@Override
				public void onPacketReceiving(final PacketEvent e) {
					if (e.getPlayer() == null) {
						return;
					}

					removeData(e.getPlayer());
				}
			}
		);
		alistener.syncStart();
	}
	private void initServerCloseInventoryFixListener() {
		plistener = new PacketAdapter(
			PacketAdapter
			.params(ModFixNG.getInstance(), PacketType.Play.Server.CLOSE_WINDOW)
		) {
			@Override
			public void onPacketSending(PacketEvent e) {
				removeData(e.getPlayer());
			}
		};
		ModFixNG.getProtocolManager().addPacketListener(plistener);
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e) {
		removeData(e.getPlayer());
	}

	private void removeData(Player player) {
		playerOpenEntity.remove(player);
	}

	private BukkitTask task;
	// check if entity is not valid or player is too far away from it, if yes -  force close inventory
	private void initEntitiesCheck() {
		task = Bukkit.getScheduler().runTaskTimer(
			ModFixNG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					Iterator<Entry<Player, Entity>> it = playerOpenEntity.entrySet().iterator();
					while (it.hasNext()) {
						Entry<Player, Entity> entry = it.next();
						Player player = entry.getKey();
						Entity entity = entry.getValue();
						if (!entity.isValid() || entity.getWorld() != player.getWorld() || entity.getLocation().distanceSquared(player.getLocation()) > 36) {
							it.remove();
							player.closeInventory();
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
		initClientCloseInventoryFixListener();
		initServerCloseInventoryFixListener();
		initEntitiesCheck();
	}

	@Override
	public void unload() {
		task.cancel();
		ModFixNG.getProtocolManager().getAsynchronousManager().unregisterAsyncHandler(alistener);
		ModFixNG.getProtocolManager().removePacketListener(plistener);
		HandlerList.unregisterAll(this);
		Iterator<Player> it = playerOpenEntity.keySet().iterator();
		while (it.hasNext()) {
			Player player = it.next();
			it.remove();
			player.closeInventory();
		}
	}

}
