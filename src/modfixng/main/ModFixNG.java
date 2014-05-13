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

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class ModFixNG extends JavaPlugin {

	private Config config;

	private Commands commandl;

	public ProtocolManager protocolManager;

	private FeatureLoader loader;

	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		config = new Config(this);
		config.loadConfig();
		commandl = new Commands(this, config);
		getServer().getPluginManager().registerEvents(commandl, this);
		getCommand("modfix").setExecutor(commandl);
		loader = new FeatureLoader(this, config);
		loader.loadAll();
	}

	@Override
	public void onDisable() {
		loader.unloadAll();
		for (Player p : getServer().getOnlinePlayers()) {
			p.closeInventory();
		}
		config = null;
		commandl = null;
		protocolManager = null;
	}

}
