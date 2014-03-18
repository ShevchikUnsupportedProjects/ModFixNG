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
import modfixng.fixes.RestrictIC2EnergyStorageArmorSlot19Click;
import modfixng.fixes.ValidateActions;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class ModFixNG extends JavaPlugin {

	private Config config;

	private Commands commandl;
	private FixBag bagl;
	private FixFreecamEntities mpl;
	private FixFreecamBlocks fciol;
	private ValidateActions val;
	@SuppressWarnings("unused")
	private FixPlayerArmorSlotDesync fsl;
	@SuppressWarnings("unused")
	private RestrictIC2EnergyStorageArmorSlot19Click fic2esasl;

	public ProtocolManager protocolManager;

	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		config = new Config(this);
		config.loadConfig();
		commandl = new Commands(this, config);
		getCommand("modfix").setExecutor(commandl);
		getServer().getPluginManager().registerEvents(commandl, this);
		bagl = new FixBag(this, config);
		getServer().getPluginManager().registerEvents(bagl, this);
		mpl = new FixFreecamEntities(this, config);
		getServer().getPluginManager().registerEvents(mpl, this);
		fciol = new FixFreecamBlocks(this, config);
		getServer().getPluginManager().registerEvents(fciol, this);
		val = new ValidateActions(this, config);
		getServer().getPluginManager().registerEvents(val, this);
		fsl = new FixPlayerArmorSlotDesync(this, config);
		fic2esasl = new RestrictIC2EnergyStorageArmorSlot19Click(this, config);
	}

	@Override
	public void onDisable() {
		for (Player p : getServer().getOnlinePlayers()) {
			p.closeInventory();
		}
		config = null;
		commandl = null;
		mpl = null;
		fciol = null;
		val = null;
		fsl = null;
		fic2esasl = null;
		protocolManager.removePacketListeners(this);
		protocolManager.getAsynchronousManager().unregisterAsyncHandlers(this);
		protocolManager = null;
	}

}
