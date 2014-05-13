package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.PacketContainerReadable;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.async.AsyncListenerHandler;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class Restrict19Click implements Feature {

	private Config config;

	public Restrict19Click(Config config) {
		this.config = config;
	}

	private LinkedList<AsyncListenerHandler> listeners = new LinkedList<AsyncListenerHandler>();

	private HashSet<String> knownInvNames = new HashSet<String>(
		Arrays.asList(
			"ic2.core.block.wiring.ContainerElectricBlock"
		)
	);
	private void init19ButtonInventoryClickListener() {
		AsyncListenerHandler listener = ModFixNG.getProtocolManager().getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(ModFixNG.getInstance(), PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (e.getPlayer() == null) {
						return;
					}

					final Player player = e.getPlayer();
					// check click type(checking for button)
					if (e.getPacket().getIntegers().getValues().get(PacketContainerReadable.InventoryClick.PacketIndex.MODE) == PacketContainerReadable.InventoryClick.Mode.NUMBER_KEY_PRESS) {
						String invname = ModFixNGUtils.getOpenInventoryName(player);
						if (knownInvNames.contains(invname) || config.restrict19InvetoryNames.contains(invname)) {
							e.setCancelled(true);
							player.updateInventory();
						}
					}
				}
			}
		);
		listener.syncStart();
		listeners.add(listener);
	}

	@Override
	public void load() {
		init19ButtonInventoryClickListener();
	}

	@Override
	public void unload() {
		for (AsyncListenerHandler listener : listeners) {
			ModFixNG.getProtocolManager().getAsynchronousManager().unregisterAsyncHandler(listener);
		}
	}

}
