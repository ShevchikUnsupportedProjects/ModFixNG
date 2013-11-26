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

package modfixng;

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
	Config(ModFixNG main) {
		this.main = main;
		configfile = new File(main.getDataFolder(),"config.yml");
	}

	protected boolean fixBag19Enabled = true;
	protected HashSet<Integer> fixBag19BackPacks19IDs = new HashSet<Integer>();
	protected boolean fixBag19CropanalyzerFixEnabled = true;
	protected int fixBag19CropanalyzerID = 30122;

	protected boolean fixFreecamEntitiesEnabled = true;
	protected HashSet<Short> fixFreecamEntitiesEntitiesIDs = new HashSet<Short>();
	protected boolean fixFreecamBlockCloseInventoryOnBreakCheckEnabled = true;
	protected HashSet<String> fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>();
	protected boolean fixFreecamBlockZeroItemsCheckEnabled = true;
	
	protected boolean ejectPlayerInHopperMinecartOnLeaveEnabled = true;
	protected short ejectPlayerInHopperMinecartOnLeaveHopperMinecartID = 46;

	protected boolean fixBagFrameInsertEnabled = true;
	protected HashSet<Integer> fixBagFrameInsertBagIDs = new HashSet<Integer>();
	protected HashSet<String> fixBagFrameInsertGregIDs = new HashSet<String>();
	protected short fixBagFrameInsertFrameentityID = 18;
	protected int fixBagFrameInsertFixType = 2;
	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		fixBag19Enabled = config.getBoolean("BackPackFix.enable",fixBag19Enabled);
		fixBag19BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		fixBag19CropanalyzerFixEnabled = config.getBoolean("BackPackFix.CropanalyzerFix.enable",fixBag19CropanalyzerFixEnabled);
		fixBag19CropanalyzerID = config.getInt("BackPackFix.CropanalyzerFix.ID",fixBag19CropanalyzerID);

		fixFreecamBlockZeroItemsCheckEnabled = config.getBoolean("ProperlyCloseInventories.removeZeroSizeItems.enable",fixFreecamBlockZeroItemsCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("ProperlyCloseInventories.blocks.enable",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>(config.getStringList("ProperlyCloseInventories.blocks.IDs"));
		fixFreecamEntitiesEnabled = config.getBoolean("ProperlyCloseInventories.entities.enable", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = new HashSet<Short>(config.getShortList("ProperlyCloseInventories.entities.IDs"));
		
		ejectPlayerInHopperMinecartOnLeaveEnabled = config.getBoolean("HopperMinecartFix.enabled",ejectPlayerInHopperMinecartOnLeaveEnabled);
		ejectPlayerInHopperMinecartOnLeaveHopperMinecartID = (short) config.getInt("HopperMinecartFix.HopperMinecartID",ejectPlayerInHopperMinecartOnLeaveHopperMinecartID);
		
		fixBagFrameInsertEnabled = config.getBoolean("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
		fixBagFrameInsertFixType = config.getInt("BagFrameInsertFix.fixType",fixBagFrameInsertFixType);
		fixBagFrameInsertBagIDs = new HashSet<Integer>(config.getIntegerList("BagFrameInsertFix.bagIDs"));
		fixBagFrameInsertFrameentityID = (short) config.getInt("BagFrameInsertFix.frameentity",fixBagFrameInsertFrameentityID);
		fixBagFrameInsertGregIDs = new HashSet<String>(config.getStringList("BagFrameInsertFix.gregIDs"));
				
		saveConfig();
	}
	
	public void saveConfig()
	{
		FileConfiguration config = new YamlConfiguration();
		
		config.set("BackPackFix.enable",fixBag19Enabled);
		config.set("BackPackFix.19BlockIDs",new ArrayList<Integer>(fixBag19BackPacks19IDs));
		config.set("BackPackFix.CropanalyzerFix.enable",fixBag19CropanalyzerFixEnabled);
		config.set("BackPackFix.CropanalyzerFix.ID",fixBag19CropanalyzerID);

		config.set("ProperlyCloseInventories.removeZeroSizeItems.enable",fixFreecamBlockZeroItemsCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.enable",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.IDs",new ArrayList<String>(fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs));
		config.set("ProperlyCloseInventories.checkEntities.enable", fixFreecamEntitiesEnabled);
		config.set("ProperlyCloseInventories.checkEntities.IDs",new ArrayList<Short>(fixFreecamEntitiesEntitiesIDs));
		
		config.set("HopperMinecartFix.enabled",ejectPlayerInHopperMinecartOnLeaveEnabled);
		config.set("HopperMinecartFix.HopperMinecartID",ejectPlayerInHopperMinecartOnLeaveHopperMinecartID);

		config.set("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
		config.set("BagFrameInsertFix.bagIDs",new ArrayList<Integer>(fixBagFrameInsertBagIDs));
		config.set("BagFrameInsertFix.frameentity",fixBagFrameInsertFrameentityID);
		config.set("BagFrameInsertFix.gregIDs",new ArrayList<String>(fixBagFrameInsertGregIDs));
		config.set("BagFrameInsertFix.fixType",fixBagFrameInsertFixType);
		
		try {
			config.save(configfile);
		} catch (IOException e) {}
	}
}
