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

	protected boolean enableBackPackFix = true;
	protected HashSet<Integer> BackPacks19IDs = new HashSet<Integer>();
	protected boolean enableCropanalyzerFix = true;
	protected int CropanalyzerID = 30122;
	protected boolean enableChunkUnloadFixTP = true;
	protected boolean enableChunkUnloadFixMove = true;
	protected boolean enableTablesFixExtendedCheck = true;
	protected boolean enableFreecamEntityFix = true;
	protected HashSet<Short> minecartsIDs = new HashSet<Short>();
	protected boolean enablefreecamzeroitemscheck = true;
	protected boolean enablefreecaminvclosecheck = true;
	protected HashSet<String> blocksWithInvIDs = new HashSet<String>();
	protected boolean enablehopperminecartfix = true;
	protected short hopperminecartid = 46;
	protected boolean enableBagFrameInsertfix = true;
	protected HashSet<Integer> bagids = new HashSet<Integer>();
	protected HashSet<String> frameids = new HashSet<String>();
	protected short frameentity = 18;
	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		enableBackPackFix = config.getBoolean("BackPackFix.enable",enableBackPackFix);
		BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		enableCropanalyzerFix = config.getBoolean("BackPackFix.CropanalyzerFix.enable",enableCropanalyzerFix);
		CropanalyzerID = config.getInt("BackPackFix.CropanalyzerFix.ID",CropanalyzerID);
		
		enableChunkUnloadFixTP = config.getBoolean("ChunkUnloadFix.enable.teleport",enableChunkUnloadFixTP);
		enableChunkUnloadFixMove = config.getBoolean("ChunkUnloadFix.enable.movement",enableChunkUnloadFixMove);
		
		enableTablesFixExtendedCheck = config.getBoolean("TablesFix.ExtendedCheck.enable",enableTablesFixExtendedCheck);
		enableFreecamEntityFix = config.getBoolean("MinecartPortalFix.enable", enableFreecamEntityFix);
		minecartsIDs = new HashSet<Short>(config.getShortList("MinecartPortalFix.cartsIDs"));
		
		enablefreecamzeroitemscheck = config.getBoolean("FreeCamInvFix.zeroItemsCheck.enabled",enablefreecamzeroitemscheck);
		enablefreecaminvclosecheck = config.getBoolean("FreeCamInvFix.forceCloseInvOnBreak.enabled",enablefreecaminvclosecheck);
		blocksWithInvIDs = new HashSet<String>(config.getStringList("FreeCamInvFix.forceCloseInvOnBreak.BlockIDs"));
		
		enablehopperminecartfix = config.getBoolean("HopperMinecartFix.enabled",enablehopperminecartfix);
		hopperminecartid = (short) config.getInt("HopperMinecartFix.HopperMinecartID",hopperminecartid);
		
		enableBagFrameInsertfix = config.getBoolean("BagFrameInsertFix.enabled",enableBagFrameInsertfix);
		bagids = new HashSet<Integer>(config.getIntegerList("BagFrameInsertFix.bagIDs"));
		frameentity = (short) config.getInt("BagFrameInsertFix.frameentity",frameentity);
		frameids = new HashSet<String>(config.getStringList("BagFrameInsertFix.gregIDs"));
				
		saveConfig();
	}
	
	public void saveConfig()
	{
		FileConfiguration config = new YamlConfiguration();
		
		config.set("BackPackFix.enable",enableBackPackFix);
		config.set("BackPackFix.19BlockIDs",new ArrayList<Integer>(BackPacks19IDs));
		config.set("BackPackFix.CropanalyzerFix.enable",enableCropanalyzerFix);
		config.set("BackPackFix.CropanalyzerFix.ID",CropanalyzerID);

		config.set("ChunkUnloadFix.enable.teleport",enableChunkUnloadFixTP);
		config.set("ChunkUnloadFix.enable.movement",enableChunkUnloadFixMove);

		config.set("MinecartPortalFix.enable", enableFreecamEntityFix);
		config.set("MinecartPortalFix.cartsIDs",new ArrayList<Short>(minecartsIDs));

		config.set("FreeCamInvFix.zeroItemsCheck.enabled",enablefreecamzeroitemscheck);
		config.set("FreeCamInvFix.forceCloseInvOnBreak.enabled",enablefreecaminvclosecheck);
		config.set("FreeCamInvFix.forceCloseInvOnBreak.BlockIDs",new ArrayList<String>(blocksWithInvIDs));
		
		config.set("HopperMinecartFix.enabled",enablehopperminecartfix);
		config.set("HopperMinecartFix.HopperMinecartID",hopperminecartid);

		config.set("BagFrameInsertFix.enabled",enableBagFrameInsertfix);
		config.set("BagFrameInsertFix.bagIDs",new ArrayList<Integer>(bagids));
		config.set("BagFrameInsertFix.frameentity",frameentity);
		config.set("BagFrameInsertFix.gregIDs",new ArrayList<String>(frameids));
		
		try {
			config.save(configfile);
		} catch (IOException e) {}
	}
}
