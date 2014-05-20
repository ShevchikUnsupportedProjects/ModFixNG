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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private File configfile;

	public Config(ModFixNG main) {
		configfile = new File(main.getDataFolder(), "config.yml");
	}

	public boolean fixBagEnabled = true;
	public boolean fixBag19ButtonClickEnabled = true;
	public HashSet<String> fixBag19ButtonClickBagInventoryNames = new HashSet<String>();
	public boolean fixBagCropanalyzerFixEnabled = true;
	public boolean fixBagToolboxFixEnabled = true;

	public boolean fixFreecamEntitiesEnabled = true;
	public HashSet<Short> fixFreecamEntitiesEntitiesIDs = new HashSet<Short>();

	public boolean fixFreecamBlockCloseInventoryOnBreakCheckEnabled = true;
	public HashSet<String> fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>();

	public boolean validateActionsEnabled = true;

	public boolean fixSlotDesyncEnabled = true;

	public boolean restrict19Enabled = true;
	public HashSet<String> restrict19InvetoryNames = new HashSet<String>();

	public boolean restrictShiftEnabled = true;
	public HashSet<String> restrictShiftInvetoryNames = new HashSet<String>();

	public boolean microblockFixEnabled = true;
	public int microblockFixItemID = -1;
	public int microblockFixBlockID = -1;

	public void loadConfig() {
		YamlConfigurationWrapper config = YamlConfigurationWrapper.loadConfiguration(configfile);

		fixBagEnabled = config.getBoolean("BackPackFix.enabled", fixBagEnabled);
		fixBag19ButtonClickEnabled = config.getBoolean("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		fixBag19ButtonClickBagInventoryNames = new HashSet<String>(config.getStringList("BackPackFix.restrict19ButtonClick.inventoryNames"));
		fixBagCropanalyzerFixEnabled = config.getBoolean("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		fixBagToolboxFixEnabled = config.getBoolean("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);

		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("ProperlyCloseInventories.checkBlocks.enabled", fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = config.getHashSet(String.class, "ProperlyCloseInventories.checkBlocks.IDs");

		fixFreecamEntitiesEnabled = config.getBoolean("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = config.getHashSet(Short.class, "ProperlyCloseInventories.checkEntities.IDs");

		validateActionsEnabled = config.getBoolean("ValidateActions.enabled", validateActionsEnabled);

		fixSlotDesyncEnabled = config.getBoolean("ForceSyncSlots.enabled", fixSlotDesyncEnabled);

		restrict19Enabled = config.getBoolean("Restrict19ButtonClick.enabled", restrict19Enabled);
		restrict19InvetoryNames = config.getHashSet(String.class, "Restrict19ButtonClick.inventoryNames");

		restrictShiftEnabled = config.getBoolean("RestrictShiftButtonClick.enabled", restrictShiftEnabled);
		restrictShiftInvetoryNames = config.getHashSet(String.class, "RestrictShiftButtonClick.inventoryNames");

		microblockFixEnabled = config.getBoolean("MicroblockFix.enabled", microblockFixEnabled);
		microblockFixItemID = config.getInt("MicroblockFix.itemID", microblockFixItemID);
		microblockFixBlockID = config.getInt("MicroblockFix.blockID", microblockFixBlockID);

		saveConfig();
	}

	public void saveConfig() {
		YamlConfigurationWrapper config = new YamlConfigurationWrapper();

		config.set("BackPackFix.enabled", fixBagEnabled);
		config.set("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		config.set("BackPackFix.restrict19ButtonClick.inventoryNames", fixBag19ButtonClickBagInventoryNames);
		config.set("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		config.set("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);

		config.set("ProperlyCloseInventories.checkBlocks.enabled", fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.IDs", fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs);

		config.set("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		config.set("ProperlyCloseInventories.checkEntities.IDs", fixFreecamEntitiesEntitiesIDs);

		config.set("ValidateActions.enabled", validateActionsEnabled);

		config.set("ForceSyncSlots.enabled", fixSlotDesyncEnabled);

		config.set("Restrict19ButtonClick.enabled", restrict19Enabled);
		config.set("Restrict19ButtonClick.inventoryNames", restrict19InvetoryNames);

		config.set("RestrictShiftButtonClick.enabled", restrictShiftEnabled);
		config.set("RestrictShiftButtonClick.inventoryNames", restrictShiftInvetoryNames);

		config.set("MicroblockFix.enabled", microblockFixEnabled);
		config.set("MicroblockFix.itemID", microblockFixItemID);
		config.set("MicroblockFix.blockID", microblockFixBlockID);

		try {
			config.save(configfile);
		} catch (IOException e) {
		}
	}

	private static class YamlConfigurationWrapper extends YamlConfiguration {

		public static YamlConfigurationWrapper loadConfiguration(File file) {
			YamlConfigurationWrapper wrapper = new YamlConfigurationWrapper();
			try {
				wrapper.load(file);
			} catch (Exception e) {
			}
			return wrapper;
		}

		@SuppressWarnings("unchecked")
		private <T> HashSet<T> getHashSet(Class<T> t, String path) {
			try {
				HashSet<T> set = new HashSet<T>();
				List<?> list = getList(path, new ArrayList<T>());
				for (Object element : list) {
					set.add((T) element);
				}
				return set;
			} catch (Exception e) {
			}
			return new HashSet<T>();
		}

		@Override
		public void set(String path, Object obj) {
			if (obj instanceof HashSet) {
				ArrayList<Object> list = new ArrayList<Object>((HashSet<?>) obj);
				set(path, list);
			} else {
				super.set(path, obj);
			}
		}

	}

}
