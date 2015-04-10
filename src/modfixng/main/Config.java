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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
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

	public boolean properlyCloseEntitiesContainersEnabled = true;
	public HashSet<String> properlyCloseEntitiesContainersEntitiesTypes = new HashSet<String>();

	public boolean properlyCloseBlocksContainers = true;
	public HashSet<String> properlyCloseBlocksContainersBlocksMaterials = new HashSet<String>();

	public boolean validateActionsEnabled = true;

	public boolean fixSlotDesyncEnabled = true;

	public boolean restrict19Enabled = true;
	public HashSet<String> restrict19InvetoryNames = new HashSet<String>();

	public boolean restrictShiftEnabled = true;
	public HashSet<String> restrictShiftInvetoryNames = new HashSet<String>();

	public boolean fixMultipartEnabled = true;
	public Material fixMultipartItemMaterial = Material.AIR;
	public Material fixMultipartBlockMaterial = Material.AIR;

	public boolean fixForestryCraftingContainers = true;

	public boolean validateBeaconEffectsChoiceEnabled = true;

	public boolean cleanupBookMeta = true;

	public void loadConfig() {
		Configuration config = Configuration.loadConfiguration(configfile);

		fixBagEnabled = config.get("BackPackFix.enabled", fixBagEnabled);
		fixBag19ButtonClickEnabled = config.get("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		fixBag19ButtonClickBagInventoryNames = config.getHashSet("BackPackFix.restrict19ButtonClick.inventoryNames", fixBag19ButtonClickBagInventoryNames);
		fixBagCropanalyzerFixEnabled = config.get("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		fixBagToolboxFixEnabled = config.get("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);

		properlyCloseBlocksContainers = config.get("ProperlyCloseInventories.checkBlocks.enabled", properlyCloseBlocksContainers);
		properlyCloseBlocksContainersBlocksMaterials = config.getHashSet("ProperlyCloseInventories.checkBlocks.materials", properlyCloseBlocksContainersBlocksMaterials);

		properlyCloseEntitiesContainersEnabled = config.get("ProperlyCloseInventories.checkEntities.enabled", properlyCloseEntitiesContainersEnabled);
		properlyCloseEntitiesContainersEntitiesTypes = config.getHashSet("ProperlyCloseInventories.checkEntities.types", properlyCloseEntitiesContainersEntitiesTypes);

		fixSlotDesyncEnabled = config.get("ForceSyncSlots.enabled", fixSlotDesyncEnabled);

		fixMultipartEnabled = config.get("MultipartFix.enabled", fixMultipartEnabled);
		fixMultipartItemMaterial = config.getMaterial("MultipartFix.itemMaterial", fixMultipartItemMaterial);
		fixMultipartBlockMaterial = config.getMaterial("MultipartFix.blockMaterial", fixMultipartBlockMaterial);

		validateActionsEnabled = config.get("ValidateActions.enabled", validateActionsEnabled);

		restrict19Enabled = config.get("Restrict19ButtonClick.enabled", restrict19Enabled);
		restrict19InvetoryNames = config.getHashSet("Restrict19ButtonClick.inventoryNames", restrict19InvetoryNames);

		restrictShiftEnabled = config.get("RestrictShiftButtonClick.enabled", restrictShiftEnabled);
		restrictShiftInvetoryNames = config.getHashSet("RestrictShiftButtonClick.inventoryNames", restrictShiftInvetoryNames);

		fixForestryCraftingContainers = config.get("ForestryCraftingContainersFix.enabled", fixForestryCraftingContainers);

		validateBeaconEffectsChoiceEnabled = config.get("ValidateBeaconEffectsChoice.enabled", validateBeaconEffectsChoiceEnabled);

		cleanupBookMeta = config.get("CleanupBookMeta.enabled", cleanupBookMeta);

		saveConfig();
	}

	public void saveConfig() {
		Configuration config = new Configuration();

		config.set("BackPackFix.enabled", fixBagEnabled);
		config.set("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		config.set("BackPackFix.restrict19ButtonClick.inventoryNames", fixBag19ButtonClickBagInventoryNames);
		config.set("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		config.set("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);

		config.set("ProperlyCloseInventories.checkBlocks.enabled", properlyCloseBlocksContainers);
		config.set("ProperlyCloseInventories.checkBlocks.materials", properlyCloseBlocksContainersBlocksMaterials);

		config.set("ProperlyCloseInventories.checkEntities.enabled", properlyCloseEntitiesContainersEnabled);
		config.set("ProperlyCloseInventories.checkEntities.types", properlyCloseEntitiesContainersEntitiesTypes);

		config.set("ValidateActions.enabled", validateActionsEnabled);

		config.set("ForceSyncSlots.enabled", fixSlotDesyncEnabled);

		config.set("Restrict19ButtonClick.enabled", restrict19Enabled);
		config.set("Restrict19ButtonClick.inventoryNames", restrict19InvetoryNames);

		config.set("RestrictShiftButtonClick.enabled", restrictShiftEnabled);
		config.set("RestrictShiftButtonClick.inventoryNames", restrictShiftInvetoryNames);

		config.set("MultipartFix.enabled", fixMultipartEnabled);
		config.set("MultipartFix.itemMaterial", fixMultipartItemMaterial);
		config.set("MultipartFix.blockMaterial", fixMultipartBlockMaterial);

		config.set("ForestryCraftingContainersFix.enabled", fixForestryCraftingContainers);

		config.set("ValidateBeaconEffectsChoice.enabled", validateBeaconEffectsChoiceEnabled);

		config.set("CleanupBookMeta.enabled", cleanupBookMeta);

		try {
			config.save(configfile);
		} catch (IOException e) {
		}
	}

	private static class Configuration {

		private YamlConfiguration config;

		public Configuration(YamlConfiguration config) {
			this.config = config;
		}

		public Configuration() {
			this.config = new YamlConfiguration();
		}

		public static Configuration loadConfiguration(File file) {
			YamlConfiguration config = new YamlConfiguration();
			try {
				config.load(file);
			} catch (FileNotFoundException e) {
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			return new Configuration(config);
		}

		@SuppressWarnings("unchecked")
		private <T> T get(String path, T defaultValue) {
			try {
				Object obj = config.get(path);
				if (obj != null) {
					return (T) obj;
				}
			} catch (Throwable e) {
			}
			return defaultValue;
		}

		private Material getMaterial(String path, Material defaultValue) {
			try {
				String value = config.getString(path);
				Material mat = Material.getMaterial(value);
				if (mat != null) {
					return mat;
				}
			} catch (Throwable e) {
			}
			return defaultValue;
		}

		@SuppressWarnings("unchecked")
		private <T> HashSet<T> getHashSet(String path, HashSet<T> defaultValue) {
			try {
				HashSet<T> set = new HashSet<T>();
				List<?> list = config.getList(path, new ArrayList<T>());
				for (Object element : list) {
					set.add((T) element);
				}
				return set;
			} catch (Exception e) {
			}
			return defaultValue;
		}

		public void set(String path, Object obj) {
			if (obj instanceof HashSet) {
				ArrayList<Object> list = new ArrayList<Object>((HashSet<?>) obj);
				config.set(path, list);
			} else if (obj instanceof Material) {
				config.set(path, ((Material) obj).toString());
			} else {
				config.set(path, obj);
			}
		}

		public void save(File configfile) throws IOException {
			config.save(configfile);
		}

	}

}