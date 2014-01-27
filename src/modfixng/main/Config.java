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
	@SuppressWarnings("unused")
	private ModFixNG main;
	private File configfile;
	public Config(ModFixNG main) {
		this.main = main;
		configfile = new File(main.getDataFolder(),"config.yml");
	}

	public boolean fixBagEnabled = true;
	public boolean fixBagRestrictInteractIfInventoryOpen = true;
	public boolean fixBag19ButtonClickEnabled = true;
	public HashSet<Integer> fixBag19ButtonClickBagIDs = new HashSet<Integer>();
	public boolean fixBagShiftBlockRestrictEnabled = true;
	public HashSet<Integer> fixBagShiftBlockRestrictBagIDs = new HashSet<Integer>();
	public boolean fixBagCropanalyzerFixEnabled = true;
	public boolean fixBagToolboxFixEnabled = true;

	public boolean fixFreecamEntitiesEnabled = true;
	public HashSet<Short> fixFreecamEntitiesEntitiesIDs = new HashSet<Short>();

	public boolean fixFreecamBlockCloseInventoryOnBreakCheckEnabled = true;
	public HashSet<String> fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>();
	public boolean fixFreecamBlockZeroItemsCheckEnabled = true;
	
	public boolean restrictBlockBreakWhileOpenEnabled = true;
	public HashSet<String> restrictBlockBreakWhileOpenIDs = new HashSet<String>();
	public boolean restrictBlockBreakWhileOpenClearDropIfBlockBroken = true;
	
	public boolean fixHopperMinecart = true;
	
	public boolean fixSlotDesyncEnabled = true;
	
	public boolean fixIC2EnergyStorage = true;
	
	public void loadConfig()
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		fixBagEnabled = config.getBoolean("BackPackFix.enabled",fixBagEnabled);
		fixBag19ButtonClickEnabled = config.getBoolean("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		fixBag19ButtonClickBagIDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.restrict19ButtonClick.BagIDs"));
		fixBagCropanalyzerFixEnabled = config.getBoolean("BackPackFix.fixCropanalyzer.enabled", fixBagCropanalyzerFixEnabled);
		fixBagToolboxFixEnabled = config.getBoolean("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);
		fixBagRestrictInteractIfInventoryOpen = config.getBoolean("BackPackFix.restrictInteractIfInventoryOpen.enabled", fixBagRestrictInteractIfInventoryOpen);

		fixFreecamBlockZeroItemsCheckEnabled = config.getBoolean("ProperlyCloseInventories.removeZeroSizeItems.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("ProperlyCloseInventories.checkBlocks.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>(config.getStringList("ProperlyCloseInventories.checkBlocks.IDs"));
		
		fixFreecamEntitiesEnabled = config.getBoolean("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = new HashSet<Short>(config.getShortList("ProperlyCloseInventories.checkEntities.IDs"));
		
		restrictBlockBreakWhileOpenEnabled = config.getBoolean("RestrictBlockBreakWhileOpen.enabled", restrictBlockBreakWhileOpenEnabled);
		restrictBlockBreakWhileOpenIDs = new HashSet<String>(config.getStringList("RestrictBlockBreakWhileOpen.IDs"));
		restrictBlockBreakWhileOpenClearDropIfBlockBroken = config.getBoolean("RestrictBlockBreakWhileOpen.clearDropIfBlockWasBrokenSomehow", restrictBlockBreakWhileOpenClearDropIfBlockBroken);
		
		fixHopperMinecart = config.getBoolean("HopperMinecartFix.enabled",fixHopperMinecart);
		
		fixSlotDesyncEnabled = config.getBoolean("ForceSyncSlots.enabled", fixSlotDesyncEnabled);
		
		fixIC2EnergyStorage = config.getBoolean("IC2EnergyStorageFix.enabled", fixIC2EnergyStorage);
		
		saveConfig();
	}
	
	public void saveConfig()
	{
		FileConfiguration config = new YamlConfiguration();
		
		config.set("BackPackFix.enabled",fixBagEnabled);
		config.set("BackPackFix.restrict19ButtonClick.enabled", fixBag19ButtonClickEnabled);
		config.set("BackPackFix.restrict19ButtonClick.BagIDs", new ArrayList<Integer>(fixBag19ButtonClickBagIDs));
		config.set("BackPackFix.restrictBlockShiftClick.enabled", fixBagShiftBlockRestrictEnabled);
		config.set("BackPackFix.restrictBlockShiftClick.BagIDs",new ArrayList<Integer>(fixBagShiftBlockRestrictBagIDs));
		config.set("BackPackFix.fixCropanalyzer.enabled",fixBagCropanalyzerFixEnabled);
		config.set("BackPackFix.fixToolbox.enabled", fixBagToolboxFixEnabled);
		config.set("BackPackFix.restrictInteractIfInventoryOpen.enabled", fixBagRestrictInteractIfInventoryOpen);

		config.set("ProperlyCloseInventories.removeZeroSizeItems.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.IDs",new ArrayList<String>(fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs));

		config.set("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		config.set("ProperlyCloseInventories.checkEntities.IDs",new ArrayList<Short>(fixFreecamEntitiesEntitiesIDs));
		
		config.set("RestrictBlockBreakWhileOpen.enabled", restrictBlockBreakWhileOpenEnabled);
		config.set("RestrictBlockBreakWhileOpen.IDs", new ArrayList<String>(restrictBlockBreakWhileOpenIDs));
		config.set("RestrictBlockBreakWhileOpen.clearDropIfBlockWasBrokenSomehow", restrictBlockBreakWhileOpenClearDropIfBlockBroken);
		
		config.set("HopperMinecartFix.enabled",fixHopperMinecart);
		
		config.set("ForceSyncSlots.enabled", fixSlotDesyncEnabled);
		
		config.set("IC2EnergyStorageFix.enabled", fixIC2EnergyStorage);
		
		try {config.save(configfile);} catch (IOException e) {}
	}
}
