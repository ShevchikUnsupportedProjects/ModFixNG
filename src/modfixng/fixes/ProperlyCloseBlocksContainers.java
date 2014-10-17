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

import modfixng.events.ClickInventoryPacketClickInventoryEvent;
import modfixng.events.CloseInventoryPacketCloseInventoryEvent;
import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

public class ProperlyCloseBlocksContainers implements Listener, Feature {

	private Config config;
	public ProperlyCloseBlocksContainers(Config config) {
		this.config = config;
	}

	private HashMap<String, BlockState> playerOpenBlock = new HashMap<String, BlockState>(200);

	private HashSet<Material> knownBlockMaterials  = new HashSet<Material>(
		Arrays.asList(
			new Material[] {
				//some vanilla minecraft item that has gui but doesn't implement IInventory
				Material.ANVIL,
				Material.ENCHANTMENT_TABLE,
			}
		)
	);
	// add player to list when he opens block inventory
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerOpenedBlock(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Player player = e.getPlayer();
		String playername = player.getName();

		if (playerOpenBlock.containsKey(playername)) {
			if (NMSUtilsAccess.getNMSUtils().isInventoryOpen(player)) {
				e.setCancelled(true);
				return;
			}
		}

		final Block b = e.getClickedBlock();
		if (config.properlyCloseBlocksContainersBlocksMaterials.contains(ModFixNGUtils.getMaterialString(b)) || NMSUtilsAccess.getNMSUtils().hasInventory(b) || knownBlockMaterials.contains(b.getType())) {
			playerOpenBlock.put(playername, b.getState());
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
		playerOpenBlock.remove(name);
	}

	//check valid on inventory click
	@EventHandler
	public void onClick(ClickInventoryPacketClickInventoryEvent event) {
		Player player = event.getPlayer();
		BlockState blockstate = playerOpenBlock.get(player.getName());
		Block block = blockstate.getBlock();
		if (!isValid(player, blockstate, block)) {
			event.setCancelled(true);
			removeData(player.getName());
			player.closeInventory();
		}
	}

	private BukkitTask task;
	// check if block is broken or player is too far away from it or the block is broken, if yes - force close inventory
	private void initBlockCheck() {
		task = Bukkit.getScheduler().runTaskTimer(
			ModFixNG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					for (Player player : Bukkit.getOnlinePlayers()) {
						String playername = player.getName();
						if (playerOpenBlock.containsKey(playername)) {
							BlockState blockstate = playerOpenBlock.get(playername);
							Block block = blockstate.getBlock();
							if (!isValid(player, blockstate, block)) {
								removeData(playername);
								player.closeInventory();
							}
						}
					}
				}
			},
			0, 1
		);
	}

	private boolean isValid(Player player, BlockState bs, Block b) {
		if (!b.getWorld().equals(player.getWorld()) || b.getLocation().distanceSquared(player.getLocation()) > 36) {
			return false;
		}
		if ((bs.getType() == Material.FURNACE) || (bs.getType() == Material.BURNING_FURNACE)) {
			return (b.getType() == Material.FURNACE) || (b.getType() == Material.BURNING_FURNACE);
		}
		return bs.getType() == b.getType();
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
		initServerCloseInventoryFixListener();
		initBlockCheck();
	}

	@Override
	public void unload() {
		task.cancel();
		ModFixNG.getProtocolManager().removePacketListener(plistener);
		HandlerList.unregisterAll(this);
		for (Player player : Bukkit.getOnlinePlayers()) {
			String playername = player.getName();
			if (playerOpenBlock.containsKey(playername)) {
				playerOpenBlock.remove(playername);
				player.closeInventory();
			}
		}
	}

	@Override
	public String getName() {
		return "ProperlyCloseBlocksContainers";
	}

}