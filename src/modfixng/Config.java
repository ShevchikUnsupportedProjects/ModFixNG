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
	protected HashSet<Integer> fixFreecamBlockCloseInventoryOnBreakWrenchesIDs = new HashSet<Integer>();
	protected boolean fixFreecamBlockZeroItemsCheckEnabled = true;
	
	protected boolean restrictBlockBreakWhileOpenEnabled = true;
	protected HashSet<String> restrictBlockBreakWhileOpenIDs = new HashSet<String>();
	protected HashSet<Integer> restrictBlockBreakWhileOpenWrehchesIDs = new HashSet<Integer>();
	
	protected boolean fixHopperMinecart = true;

	protected boolean fixBagFrameInsertEnabled = true;
	protected HashSet<Integer> fixBagFrameInsertBagIDs = new HashSet<Integer>();
	protected HashSet<String> fixBagFrameInsertGregIDs = new HashSet<String>();
	protected short fixBagFrameInsertFrameentityID = 18;
	
	protected boolean fixSlotDesyncEnabled = true;
	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		fixBag19Enabled = config.getBoolean("BackPackFix.enabled",fixBag19Enabled);
		fixBag19BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		fixBag19CropanalyzerFixEnabled = config.getBoolean("BackPackFix.CropanalyzerFix.enabled",fixBag19CropanalyzerFixEnabled);
		fixBag19CropanalyzerID = config.getInt("BackPackFix.CropanalyzerFix.ID",fixBag19CropanalyzerID);

		fixFreecamBlockZeroItemsCheckEnabled = config.getBoolean("ProperlyCloseInventories.removeZeroSizeItems.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("ProperlyCloseInventories.checkBlocks.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>(config.getStringList("ProperlyCloseInventories.checkBlocks.IDs"));
		fixFreecamBlockCloseInventoryOnBreakWrenchesIDs = new HashSet<Integer>(config.getIntegerList("ProperlyCloseInventories.checkBlocks.WrenchesIDs"));
		
		fixFreecamEntitiesEnabled = config.getBoolean("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = new HashSet<Short>(config.getShortList("ProperlyCloseInventories.checkEntities.IDs"));
		
		restrictBlockBreakWhileOpenEnabled = config.getBoolean("RestrictBlockBreakWhileOpen.enabled", restrictBlockBreakWhileOpenEnabled);
		restrictBlockBreakWhileOpenIDs = new HashSet<String>(config.getStringList("RestrictBlockBreakWhileOpen.IDs"));
		restrictBlockBreakWhileOpenWrehchesIDs = new HashSet<Integer>(config.getIntegerList("RestrictBlockBreakWhileOpen.WrenchesIDs"));
		
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
		config.set("BackPackFix.CropanalyzerFix.ID",fixBag19CropanalyzerID);

		config.set("ProperlyCloseInventories.removeZeroSizeItems.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("ProperlyCloseInventories.checkBlocks.IDs",new ArrayList<String>(fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs));
		config.set("ProperlyCloseInventories.checkBlocks.WrenchesIDs", new ArrayList<Integer>(fixFreecamBlockCloseInventoryOnBreakWrenchesIDs));

		config.set("ProperlyCloseInventories.checkEntities.enabled", fixFreecamEntitiesEnabled);
		config.set("ProperlyCloseInventories.checkEntities.IDs",new ArrayList<Short>(fixFreecamEntitiesEntitiesIDs));
		
		config.set("RestrictBlockBreakWhileOpen.enabled", restrictBlockBreakWhileOpenEnabled);
		config.set("RestrictBlockBreakWhileOpen.IDs", new ArrayList<String>(restrictBlockBreakWhileOpenIDs));
		config.set("RestrictBlockBreakWhileOpen.WrenchesIDs", new ArrayList<Integer>(restrictBlockBreakWhileOpenWrehchesIDs));
		
		config.set("HopperMinecartFix.enabled",fixHopperMinecart);

		config.set("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
		config.set("BagFrameInsertFix.bagIDs",new ArrayList<Integer>(fixBagFrameInsertBagIDs));
		config.set("BagFrameInsertFix.frameentity",fixBagFrameInsertFrameentityID);
		config.set("BagFrameInsertFix.gregIDs",new ArrayList<String>(fixBagFrameInsertGregIDs));
		
		config.set("ForceSyncSlots.enabled", fixSlotDesyncEnabled);
		
		try {config.save(configfile);} catch (IOException e) {}
	}
}
