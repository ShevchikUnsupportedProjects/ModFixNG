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

	protected boolean forceCloseInventoryOnChunkChangeTeleport = true;
	protected boolean forceCloseInventoryOnChunkChangeMove = true;

	protected boolean fixFreecamEntitiesEnabled = true;
	protected HashSet<Short> fixFreecamEntitiesEntitiesIDs = new HashSet<Short>();

	protected boolean fixFreecamBlockZeroItemsCheckEnabled = true;
	protected boolean fixFreecamBlockCloseInventoryOnBreakCheckEnabled = true;
	protected HashSet<String> fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>();
	
	protected boolean ejectPlayerInHopperMinecartOnLeaveEnabled = true;
	protected short ejectPlayerInHopperMinecartOnLeaveHopperMinecartID = 46;

	protected boolean fixBagFrameInsertEnabled = true;
	protected HashSet<Integer> fixBagFrameInsertBagIDs = new HashSet<Integer>();
	protected HashSet<String> fixBagFrameInsertGregIDs = new HashSet<String>();
	protected short fixBagFrameInsertFrameentityID = 18;
	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		fixBag19Enabled = config.getBoolean("BackPackFix.enable",fixBag19Enabled);
		fixBag19BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		fixBag19CropanalyzerFixEnabled = config.getBoolean("BackPackFix.CropanalyzerFix.enable",fixBag19CropanalyzerFixEnabled);
		fixBag19CropanalyzerID = config.getInt("BackPackFix.CropanalyzerFix.ID",fixBag19CropanalyzerID);
		
		forceCloseInventoryOnChunkChangeTeleport = config.getBoolean("ChunkUnloadFix.enable.teleport",forceCloseInventoryOnChunkChangeTeleport);
		forceCloseInventoryOnChunkChangeMove = config.getBoolean("ChunkUnloadFix.enable.movement",forceCloseInventoryOnChunkChangeMove);

		fixFreecamEntitiesEnabled = config.getBoolean("MinecartPortalFix.enable", fixFreecamEntitiesEnabled);
		fixFreecamEntitiesEntitiesIDs = new HashSet<Short>(config.getShortList("MinecartPortalFix.cartsIDs"));
		
		fixFreecamBlockZeroItemsCheckEnabled = config.getBoolean("FreeCamInvFix.zeroItemsCheck.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckEnabled = config.getBoolean("FreeCamInvFix.forceCloseInvOnBreak.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs = new HashSet<String>(config.getStringList("FreeCamInvFix.forceCloseInvOnBreak.BlockIDs"));
		
		ejectPlayerInHopperMinecartOnLeaveEnabled = config.getBoolean("HopperMinecartFix.enabled",ejectPlayerInHopperMinecartOnLeaveEnabled);
		ejectPlayerInHopperMinecartOnLeaveHopperMinecartID = (short) config.getInt("HopperMinecartFix.HopperMinecartID",ejectPlayerInHopperMinecartOnLeaveHopperMinecartID);
		
		fixBagFrameInsertEnabled = config.getBoolean("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
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

		config.set("ChunkUnloadFix.enable.teleport",forceCloseInventoryOnChunkChangeTeleport);
		config.set("ChunkUnloadFix.enable.movement",forceCloseInventoryOnChunkChangeMove);

		config.set("MinecartPortalFix.enable", fixFreecamEntitiesEnabled);
		config.set("MinecartPortalFix.cartsIDs",new ArrayList<Short>(fixFreecamEntitiesEntitiesIDs));

		config.set("FreeCamInvFix.zeroItemsCheck.enabled",fixFreecamBlockZeroItemsCheckEnabled);
		config.set("FreeCamInvFix.forceCloseInvOnBreak.enabled",fixFreecamBlockCloseInventoryOnBreakCheckEnabled);
		config.set("FreeCamInvFix.forceCloseInvOnBreak.BlockIDs",new ArrayList<String>(fixFreecamBlockCloseInventoryOnBreakCheckBlocksIDs));
		
		config.set("HopperMinecartFix.enabled",ejectPlayerInHopperMinecartOnLeaveEnabled);
		config.set("HopperMinecartFix.HopperMinecartID",ejectPlayerInHopperMinecartOnLeaveHopperMinecartID);

		config.set("BagFrameInsertFix.enabled",fixBagFrameInsertEnabled);
		config.set("BagFrameInsertFix.bagIDs",new ArrayList<Integer>(fixBagFrameInsertBagIDs));
		config.set("BagFrameInsertFix.frameentity",fixBagFrameInsertFrameentityID);
		config.set("BagFrameInsertFix.gregIDs",new ArrayList<String>(fixBagFrameInsertGregIDs));
		
		try {
			config.save(configfile);
		} catch (IOException e) {}
	}
}
