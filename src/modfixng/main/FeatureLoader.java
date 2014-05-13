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

import java.util.LinkedList;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import modfixng.fixes.Feature;
import modfixng.fixes.FixBag;
import modfixng.fixes.FixFreecamBlocks;
import modfixng.fixes.FixFreecamEntities;
import modfixng.fixes.FixPlayerArmorSlotDesync;
import modfixng.fixes.ForgeMultipartPlaceFix;
import modfixng.fixes.Restrict19Click;
import modfixng.fixes.RestrictShiftClick;
import modfixng.fixes.ValidateActions;

public class FeatureLoader {
	
	private ModFixNG plugin;
	private Config config;
	public FeatureLoader(ModFixNG plugin, Config config) {
		this.plugin = plugin;
		this.config = config;
	}

	private LinkedList<Feature> loadedFeatures = new LinkedList<Feature>();

	public void loadAll() {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new FixBag(plugin, config), plugin);
		pm.registerEvents(new FixFreecamEntities(plugin, config), plugin);
		pm.registerEvents(new FixFreecamBlocks(plugin, config), plugin);
		pm.registerEvents(new ValidateActions(plugin, config), plugin);
		pm.registerEvents(new ForgeMultipartPlaceFix(config), plugin); 
		new Restrict19Click(plugin, config);
		new RestrictShiftClick(plugin, config);
		new FixPlayerArmorSlotDesync(plugin, config);
	}

	public void unloadAll() {
		HandlerList.unregisterAll(plugin);
		plugin.protocolManager.removePacketListeners(plugin);
		plugin.protocolManager.getAsynchronousManager().unregisterAsyncHandlers(plugin);
	}

}
