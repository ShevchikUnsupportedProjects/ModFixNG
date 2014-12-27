package modfixng.fixes;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import modfixng.main.ModFixNG;
import modfixng.utils.NMSUtilsAccess;

public class FixBeaconCrash implements Feature {

	private PacketListener listener;

	private void initCustomPayloadListener() {
		listener = new PacketAdapter(
			PacketAdapter
			.params(ModFixNG.getInstance(), PacketType.Play.Client.CUSTOM_PAYLOAD)
			.listenerPriority(ListenerPriority.HIGHEST)
		) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
				Player player = e.getPlayer();
				if (player == null) {
					return;
				}
				PacketContainer packet = e.getPacket();
				if (packet.getStrings().read(0).equals("MC|Beacon")) {
					try {
						if (!NMSUtilsAccess.getNMSUtils().isBeaconEffectsChoiceValid(packet)) {
							e.setCancelled(true);
						}
					} catch (Throwable t) {
						t.printStackTrace();
						e.setCancelled(true);
					}
				}
			}
		};
		ModFixNG.getProtocolManager().addPacketListener(listener);
	}

	@Override
	public String getName() {
		return "FixBeaconCrash";
	}

	@Override
	public void load() {
		initCustomPayloadListener();
	}

	@Override
	public void unload() {
		ModFixNG.getProtocolManager().removePacketListener(listener);
	}

}
