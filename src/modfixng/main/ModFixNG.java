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

package modfixng.main;

import modfixng.packets.NMSPacketAccess;
import modfixng.packets.PacketReplaceListener;
import modfixng.utils.ModFixNGUtils;
import modfixng.utils.NMSUtilsAccess;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class ModFixNG extends JavaPlugin {

	private static ModFixNG instance;
	public static ModFixNG getInstance() {
		return instance;
	}

	private static ProtocolManager protocolManager;
	public static ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	private static FeatureLoader loader;
	public static FeatureLoader getFeatureLoader() {
		return loader;
	}

	private Config config;
	private Commands commandl;

	private boolean init = false;

	@Override
	public void onEnable() {
		//init nms access
		init = NMSPacketAccess.init() && NMSUtilsAccess.init();
		if (!init) {
			getLogger().severe("Can't load nms access");
			NMSPacketAccess.getError().printStackTrace();
			NMSUtilsAccess.getError().printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		//set instance
		instance = this;
		//get protocol access
		protocolManager = ProtocolLibrary.getProtocolManager();
		//init packet replacer
		PacketReplaceListener packetslistener = new PacketReplaceListener();
		packetslistener.initInBlockDigListener();
		packetslistener.initInCloseInventoryListener();
		packetslistener.initInClickInventoryListener();
		//init config
		config = new Config(this);
		config.loadConfig();
		//init commands
		commandl = new Commands(config);
		getServer().getPluginManager().registerEvents(commandl, this);
		getCommand("modfixng").setExecutor(commandl);
		//check platform
		ModFixNGUtils.checkMCPC();
		//init fixes
		loader = new FeatureLoader(config);
		loader.loadAll();
	}

	@Override
	public void onDisable() {
		if (!init) {
			return;
		}
		for (Player p : getServer().getOnlinePlayers()) {
			p.closeInventory();
		}
		loader.unloadAll();
		loader = null;
		instance = null;
		protocolManager = null;
	}

}
