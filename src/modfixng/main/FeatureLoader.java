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

import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.plugin.PluginManager;

import modfixng.fixes.Feature;
import modfixng.fixes.FixBag;
import modfixng.fixes.ProperlyCloseBlocksContainers;
import modfixng.fixes.ProperlyCloseEntitiesContainers;
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

	public void registerOnce() {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvents(new FixBag(plugin, config), plugin);
		pm.registerEvents(new ProperlyCloseEntitiesContainers(plugin, config), plugin);
		pm.registerEvents(new ProperlyCloseBlocksContainers(plugin, config), plugin);
	}

	public void loadAll() {
		if (config.microblockFixEnabled) {
			loadFeature(new ForgeMultipartPlaceFix(config));
		}
		if (config.fixSlotDesyncEnabled) {
			loadFeature(new FixPlayerArmorSlotDesync());
		}
		if (config.validateActionsEnabled) {
			loadFeature(new ValidateActions());
		}
		if (config.restrict19Enabled) {
			loadFeature(new Restrict19Click(config));
		}
		if (config.restrictShiftEnabled) {
			loadFeature(new RestrictShiftClick(config));
		}
	}

	public void loadFeature(Feature feature) {
		feature.load();
		loadedFeatures.add(feature);
	}

	public void unloadAll() {
		Iterator<Feature> fit = loadedFeatures.iterator();
		while (fit.hasNext()) {
			fit.next().unload();
			fit.remove();
		}
	}

	public void unloadFeature(Feature feature) {
		feature.unload();
		loadedFeatures.remove(feature);
	}

}
