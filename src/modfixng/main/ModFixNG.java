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

import modfixng.fixes.FixBag;
import modfixng.fixes.FixFreecamBlocks;
import modfixng.fixes.FixFreecamEntities;
import modfixng.fixes.FixPlayerArmorSlotDesync;
import modfixng.fixes.Restrict19Click;
import modfixng.fixes.ValidateActions;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class ModFixNG extends JavaPlugin {

	private Config config;

	private Commands commandl;

	public ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		config = new Config(this);
		config.loadConfig();
		commandl = new Commands(this, config);
		getCommand("modfix").setExecutor(commandl);
		getServer().getPluginManager().registerEvents(commandl, this);
		getServer().getPluginManager().registerEvents(new FixBag(this, config), this);
		getServer().getPluginManager().registerEvents(new FixFreecamEntities(this, config), this);
		getServer().getPluginManager().registerEvents(new FixFreecamBlocks(this, config), this);
		getServer().getPluginManager().registerEvents(new ValidateActions(this, config), this);
		new Restrict19Click(this, config);
		new FixPlayerArmorSlotDesync(this, config);
	}

	@Override
	public void onDisable() {
		for (Player p : getServer().getOnlinePlayers()) {
			p.closeInventory();
		}
		config = null;
		commandl = null;
		protocolManager.removePacketListeners(this);
		protocolManager.getAsynchronousManager().unregisterAsyncHandlers(this);
		protocolManager = null;
	}

}
