package modfixng.fixes;

import java.util.Arrays;
import java.util.HashSet;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.PacketContainerReadable;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class Restrict19Click {

	private ModFixNG main;
	private Config config;

	public Restrict19Click(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		init19ButtonInventoryClickListener();
	}

	private HashSet<String> knownInvNames = new HashSet<String>(
		Arrays.asList(
			"ic2.core.block.wiring.ContainerElectricBlock"
		)
	);
	private void init19ButtonInventoryClickListener() {
		main.protocolManager.getAsynchronousManager().registerAsyncHandler(
			new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
			) {
				@SuppressWarnings("deprecation")
				@Override
				public void onPacketReceiving(PacketEvent e) {
					if (!config.restrict19Enabled) {
						return;
					}

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
		).syncStart();
	}

}
