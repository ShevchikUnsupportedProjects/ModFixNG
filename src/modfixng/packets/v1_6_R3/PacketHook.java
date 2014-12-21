package modfixng.packets.v1_6_R3;

import java.lang.reflect.Field;
import java.util.Map;

import modfixng.packets.PacketHookInterface;
import net.minecraft.server.v1_6_R3.Packet;

public class PacketHook implements PacketHookInterface {

	@Override
	public void initInBlockDigListener() {
		hookPacket(BlockDig.class, BlockDig.getPacketID());
	}

	@SuppressWarnings("unchecked")
	private void hookPacket(Class<?> packetclass, int packetid) {
		Packet.l.a(packetid, packetclass);
		Field[] packetFields = Packet.class.getDeclaredFields();
		for (Field field : packetFields) {
			try {
				if (Map.class.isAssignableFrom(field.getType())) {
					field.setAccessible(true);
					@SuppressWarnings({ "rawtypes" })
					Map packets = (Map) field.get(null);
					packets.put(packetclass, packetid);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}
