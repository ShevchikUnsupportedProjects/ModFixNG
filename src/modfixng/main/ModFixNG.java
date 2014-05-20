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

import modfixng.utils.ModFixNGUtils;

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

	@Override
	public void onEnable() {
		instance = this;
		protocolManager = ProtocolLibrary.getProtocolManager();
		config = new Config(this);
		config.loadConfig();
		commandl = new Commands(config);
		getServer().getPluginManager().registerEvents(commandl, this);
		getCommand("modfixng").setExecutor(commandl);
		ModFixNGUtils.checkMCPC();
		loader = new FeatureLoader(config);
		loader.loadAll();
	}

	@Override
	public void onDisable() {
		for (Player p : getServer().getOnlinePlayers()) {
			p.closeInventory();
		}
		loader.unloadAll();
		loader = null;
		instance = null;
		protocolManager = null;
	}

}
