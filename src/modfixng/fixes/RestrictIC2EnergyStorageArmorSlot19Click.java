package modfixng.fixes;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import modfixng.main.Config;
import modfixng.main.ModFixNG;
import modfixng.utils.ModFixNGUtils;

public class RestrictIC2EnergyStorageArmorSlot19Click {

	private ModFixNG main;
	private Config config;

	public RestrictIC2EnergyStorageArmorSlot19Click(ModFixNG main, Config config) {
		this.main = main;
		this.config = config;
		init19ButtonInventoryClickListener();
	}
	
	private void init19ButtonInventoryClickListener() {
		main.protocolManager.addPacketListener(new PacketAdapter(
				PacketAdapter
				.params(main, PacketType.Play.Client.WINDOW_CLICK)
				.clientSide()
				.listenerPriority(ListenerPriority.HIGHEST)
				.optionIntercept()
			) {
			@SuppressWarnings("deprecation")
			@Override
			public void onPacketReceiving(PacketEvent e) {
				if (!config.fixIC2EnergyStorage) {
					return;
				}
				
				if (e.getPlayer() == null) {
					return;
				}

				final Player player = e.getPlayer();
				// check click type(checking for button)
				if (e.getPacket().getIntegers().getValues().get(3) == 2) {
					//check inventory(checking for ic2 electric inventory)
					if (ModFixNGUtils.isElectricContainerOpen(player)) {
						//check clicked slot(checking for non empty inventory armor slot)
						if (ModFixNGUtils.isClickedNonEmptyArmorSlot(player, e.getPacket().getIntegers().getValues().get(1))) {
							e.setCancelled(true);
							player.updateInventory();
						}
					}
				}
			}
		});
	}
	
}
