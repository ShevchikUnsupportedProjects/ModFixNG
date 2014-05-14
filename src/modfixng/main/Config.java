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

import org.bukkit.configuration.file.FileConfiguration;
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
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		fixBagEnabled = config.getBoolean("BackPackFix.enabled", fixBagEnabled);
		fixBag19ButtonClickEnabled = config.getBoolean("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		fixBag19ButtonClickBagInventoryNames = new HashSet<String>(config.getStringList("BackPackFix.restrict19ButtonClick.inventoryNames"));
		fixBagCropanalyzerFixEnabled = config.getBoolean("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		fixBagToolboxFixEnabled = config.getBoolean("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);

		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("ProperlyCloseInventories.checkBlocks.enabled", fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>(config.getStringList("ProperlyCloseInventories.checkBlocks.IDs"));

		fixFreecamEntitiesEnabled = config.getBoolean("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = new HashSet<Short>(config.getShortList("ProperlyCloseInventories.checkEntities.IDs"));

		validateActionsEnabled = config.getBoolean("ValidateActions.enabled", validateActionsEnabled);

		fixSlotDesyncEnabled = config.getBoolean("ForceSyncSlots.enabled", fixSlotDesyncEnabled);

		restrict19Enabled = config.getBoolean("Restrict19ButtonClick.enabled", restrict19Enabled);
		restrict19InvetoryNames = new HashSet<String>(config.getStringList("Restrict19ButtonClick.inventoryNames"));

		restrictShiftEnabled = config.getBoolean("RestrictShiftButtonClick.enabled", restrictShiftEnabled);
		restrictShiftInvetoryNames = new HashSet<String>(config.getStringList("RestrictShiftButtonClick.inventoryNames"));

		microblockFixEnabled = config.getBoolean("MicroblockFix.enabled", microblockFixEnabled);
		microblockFixItemID = config.getInt("MicroblockFix.itemID", microblockFixItemID);
		microblockFixBlockID = config.getInt("MicroblockFix.blockID", microblockFixBlockID);

		saveConfig();
	}

	public void saveConfig() {
		FileConfiguration config = new YamlConfiguration();

		config.set("BackPackFix.enabled", fixBagEnabled);
		config.set("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		config.set("BackPackFix.restrict19ButtonClick.inventoryNames", new ArrayList<String>(fixBag19ButtonClickBagInventoryNames));
		config.set("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		config.set("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);

		config.set("ProperlyCloseInventories.checkBlocks.enabled", fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.IDs", new ArrayList<String>(fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs));

		config.set("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		config.set("ProperlyCloseInventories.checkEntities.IDs", new ArrayList<Short>(fixFreecamEntitiesEntitiesIDs));

		config.set("ValidateActions.enabled", validateActionsEnabled);

		config.set("ForceSyncSlots.enabled", fixSlotDesyncEnabled);

		config.set("Restrict19ButtonClick.enabled", restrict19Enabled);
		config.set("Restrict19ButtonClick.inventoryNames", new ArrayList<String>(restrict19InvetoryNames));

		config.set("RestrictShiftButtonClick.enabled", restrictShiftEnabled);
		config.set("RestrictShiftButtonClick.inventoryNames", new ArrayList<String>(restrictShiftInvetoryNames));

		config.set("MicroblockFix.enabled", microblockFixEnabled);
		config.set("MicroblockFix.itemID", microblockFixItemID);
		config.set("MicroblockFix.blockID", microblockFixBlockID);

		try {
			config.save(configfile);
		} catch (IOException e) {
		}
	}
}
