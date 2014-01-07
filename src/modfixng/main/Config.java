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

	public boolean fixBag19Enabled = true;
	public HashSet<Integer> fixBag19BackPacks19IDs = new HashSet<Integer>();
	public boolean fixBag19CropanalyzerFixEnabled = true;

	public boolean fixFreecamEntitiesEnabled = true;
	public HashSet<Short> fixFreecamEntitiesEntitiesIDs = new HashSet<Short>();

	public boolean fixFreecamBlockCloseInventoryOnBreakCheckEnabled = true;
	public HashSet<String> fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>();
	public boolean fixFreecamBlockZeroItemsCheckEnabled = true;
	
	public boolean restrictBlockBreakWhileOpenEnabled = true;
	public HashSet<String> restrictBlockBreakWhileOpenIDs = new HashSet<String>();
	public boolean restrictBlockBreakWhileOpenClearDropIfBlockBroken = true;
	
	public boolean fixHopperMinecart = true;

	public boolean fixBagFrameInsertEnabled = true;
	public HashSet<Integer> fixBagFrameInsertBagIDs = new HashSet<Integer>();
	public HashSet<String> fixBagFrameInsertGregIDs = new HashSet<String>();
	public short fixBagFrameInsertFrameentityID = 18;
	
	public boolean fixSlotDesyncEnabled = true;
	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		fixBag19Enabled = config.getBoolean("BackPackFix.enabled",fixBag19Enabled);
		fixBag19BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		fixBag19CropanalyzerFixEnabled = config.getBoolean("BackPackFix.CropanalyzerFix.enabled",fixBag19CropanalyzerFixEnabled);

		fixFreecamBlockZeroItemsCheckEnabled = config.getBoolean("ProperlyCloseInventories.removeZeroSizeItems.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("ProperlyCloseInventories.checkBlocks.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>(config.getStringList("ProperlyCloseInventories.checkBlocks.IDs"));
		
		fixFreecamEntitiesEnabled = config.getBoolean("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = new HashSet<Short>(config.getShortList("ProperlyCloseInventories.checkEntities.IDs"));
		
		restrictBlockBreakWhileOpenEnabled = config.getBoolean("RestrictBlockBreakWhileOpen.enabled", restrictBlockBreakWhileOpenEnabled);
		restrictBlockBreakWhileOpenIDs = new HashSet<String>(config.getStringList("RestrictBlockBreakWhileOpen.IDs"));
		restrictBlockBreakWhileOpenClearDropIfBlockBroken = config.getBoolean("RestrictBlockBreakWhileOpen.clearDropIfBlockWasBrokenSomehow", restrictBlockBreakWhileOpenClearDropIfBlockBroken);
		
		fixHopperMinecart = config.getBoolean("HopperMinecartFix.enabled",fixHopperMinecart);
		
		fixBagFrameInsertEnabled = config.getBoolean("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
		fixBagFrameInsertBagIDs = new HashSet<Integer>(config.getIntegerList("BagFrameInsertFix.bagIDs"));
		fixBagFrameInsertFrameentityID = (short) config.getInt("BagFrameInsertFix.frameentity",fixBagFrameInsertFrameentityID);
		fixBagFrameInsertGregIDs = new HashSet<String>(config.getStringList("BagFrameInsertFix.gregIDs"));
		
		fixSlotDesyncEnabled = config.getBoolean("ForceSyncSlots.enabled", fixSlotDesyncEnabled);
		
		saveConfig();
	}
	
	public void saveConfig()
	{
		FileConfiguration config = new YamlConfiguration();
		
		config.set("BackPackFix.enabled",fixBag19Enabled);
		config.set("BackPackFix.19BlockIDs",new ArrayList<Integer>(fixBag19BackPacks19IDs));
		config.set("BackPackFix.CropanalyzerFix.enabled",fixBag19CropanalyzerFixEnabled);

		config.set("ProperlyCloseInventories.removeZeroSizeItems.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.IDs",new ArrayList<String>(fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs));

		config.set("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		config.set("ProperlyCloseInventories.checkEntities.IDs",new ArrayList<Short>(fixFreecamEntitiesEntitiesIDs));
		
		config.set("RestrictBlockBreakWhileOpen.enabled", restrictBlockBreakWhileOpenEnabled);
		config.set("RestrictBlockBreakWhileOpen.IDs", new ArrayList<String>(restrictBlockBreakWhileOpenIDs));
		config.set("RestrictBlockBreakWhileOpen.clearDropIfBlockWasBrokenSomehow", restrictBlockBreakWhileOpenClearDropIfBlockBroken);
		
		config.set("HopperMinecartFix.enabled",fixHopperMinecart);

		config.set("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
		config.set("BagFrameInsertFix.bagIDs",new ArrayList<Integer>(fixBagFrameInsertBagIDs));
		config.set("BagFrameInsertFix.frameentity",fixBagFrameInsertFrameentityID);
		config.set("BagFrameInsertFix.gregIDs",new ArrayList<String>(fixBagFrameInsertGregIDs));
		
		config.set("ForceSyncSlots.enabled", fixSlotDesyncEnabled);
		
		try {config.save(configfile);} catch (IOException e) {}
	}
}
