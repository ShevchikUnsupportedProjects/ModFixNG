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

package modfix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ModFixConfig {
	@SuppressWarnings("unused")
	private Main main;
	ModFixConfig(Main main)
	{
		this.main = main;
	}
	
	protected boolean enableVillagersFix = true;
	protected boolean enableBackPackFix = true;
	protected HashSet<Integer> BackPacks19IDs = new HashSet<Integer>();
	protected boolean enableCropanalyzerFix = true;
	protected int CropanalyzerID = 30122;
	protected boolean enableChunkUnloadFixTP = true;
	protected boolean enableChunkUnloadFixMove = true;
	protected boolean enableTablesFix = true;
	protected HashSet<String> IntTablesIDs= new HashSet<String>();
	protected HashSet<String> BrkTablesIDs= new HashSet<String>();
	protected boolean enableTablesFixExtendedCheck = true;
	protected boolean enableExpFix = true;
	protected HashSet<String> furnSlotIDs= new HashSet<String>();
	protected boolean enableMinecartFix = true;
	protected boolean enableExpFixExtendedCheck = true;
	protected HashSet<Short> minecartsIDs = new HashSet<Short>();
	protected boolean enableRailsFix = true;
	protected HashSet<Integer> RailsIDs = new HashSet<Integer>();
	protected boolean enableRP2wiresfix = true;
	protected HashSet<String> RP2WiresIDs = new HashSet<String>();
	protected boolean enableFreecamFix = true;
	protected HashSet<String> freecamBlockIDs = new HashSet<String>();
	protected boolean enablefreecamzeroitemscheck = true;
	protected boolean enablehopperminecartfix = true;
	protected short hopperminecartid = 46;
	protected boolean enableBagFrameInsertfix = true;
	protected HashSet<Integer> bagids = new HashSet<Integer>();
	protected HashSet<String> frameids = new HashSet<String>();
	protected short frameentity = 18;
	
	public void loadConfig(){
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File("plugins/ModFix/config.yml"));
		enableBackPackFix = config.getBoolean("BackPackFix.enable",enableBackPackFix);
		BackPacks19IDs = new HashSet<Integer>(config.getIntegerList("BackPackFix.19BlockIDs"));
		enableCropanalyzerFix = config.getBoolean("BackPackFix.CropanalyzerFix.enable",enableCropanalyzerFix);
		CropanalyzerID = config.getInt("BackPackFix.CropanalyzerFix.ID",CropanalyzerID);
		enableVillagersFix = config.getBoolean("VillagersFix.enable",enableVillagersFix);
		enableChunkUnloadFixTP = config.getBoolean("ChunkUnloadFix.enable.teleport",enableChunkUnloadFixTP);
		enableChunkUnloadFixMove = config.getBoolean("ChunkUnloadFix.enable.movement",enableChunkUnloadFixMove);
		enableTablesFix = config.getBoolean("TablesFix.enable",enableTablesFix);
		IntTablesIDs = new HashSet<String>(config.getStringList("TablesFix.InteractBlockIDs"));
		BrkTablesIDs = new HashSet<String>(config.getStringList("TablesFix.BreakBlockIDs"));
		IntTablesIDs.addAll(BrkTablesIDs);
		enableTablesFixExtendedCheck = config.getBoolean("TablesFix.ExtendedCheck.enable",enableTablesFixExtendedCheck);
		enableExpFix = config.getBoolean("ExpFix.enable",enableExpFix);
		furnSlotIDs = new HashSet<String>(config.getStringList("ExpFix.FurnaceIds"));
		enableExpFixExtendedCheck = config.getBoolean("ExpFix.ExtendedCheck.enable",enableExpFixExtendedCheck);
		enableMinecartFix = config.getBoolean("MinecartPortalFix.enable", enableMinecartFix);
		minecartsIDs = new HashSet<Short>(config.getShortList("MinecartPortalFix.cartsIDs"));
		enableRailsFix = config.getBoolean("RailsFix.enable", enableRailsFix);
		RailsIDs = new HashSet<Integer>(config.getIntegerList("RailsFix.railsIDs"));
		enableRP2wiresfix = config.getBoolean("RP2WiresFix.enable", enableRP2wiresfix);
		RP2WiresIDs = new HashSet<String>(config.getStringList("RP2WiresFix.wiresIDs"));
		enableFreecamFix = config.getBoolean("FreeCamInvFix.enable",enableFreecamFix);
		freecamBlockIDs = new HashSet<String>(config.getStringList("FreeCamInvFix.checkBlockIDs"));
		enablefreecamzeroitemscheck = config.getBoolean("FreeCamInvFix.zeroItemsCheck.enabled",enablefreecamzeroitemscheck);
		enablehopperminecartfix = config.getBoolean("HopperMinecartFix.enabled",enablehopperminecartfix);
		hopperminecartid = (short) config.getInt("HopperMinecartFix.HopperMinecartID",hopperminecartid);
		enableBagFrameInsertfix = config.getBoolean("BagFrameInsertFix.enabled",enableBagFrameInsertfix);
		bagids = new HashSet<Integer>(config.getIntegerList("BagFrameInsertFix.bagIDs"));
		frameentity = (short) config.getInt("BagFrameInsertFix.frameentity");
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
		config.set("VillagersFix.enable",enableVillagersFix);
		config.set("ChunkUnloadFix.enable.teleport",enableChunkUnloadFixTP);
		config.set("ChunkUnloadFix.enable.movement",enableChunkUnloadFixMove);
		config.set("TablesFix.enable",enableTablesFix);
		config.set("TablesFix.InteractBlockIDs",new ArrayList<String>(IntTablesIDs));
		config.set("TablesFix.BreakBlockIDs",new ArrayList<String>(BrkTablesIDs));
		config.set("TablesFix.ExtendedCheck.enable",enableTablesFixExtendedCheck);
		config.set("ExpFix.enable",enableExpFix);
		config.set("ExpFix.FurnaceIds",new ArrayList<String>(furnSlotIDs));
		config.set("ExpFix.ExtendedCheck.enable",enableExpFixExtendedCheck);
		config.set("MinecartPortalFix.enable", enableMinecartFix);
		config.set("MinecartPortalFix.cartsIDs",new ArrayList<Short>(minecartsIDs));
		config.set("RailsFix.enable", enableRailsFix);
		config.set("RailsFix.railsIDs",new ArrayList<Integer>(RailsIDs));
		config.set("RP2WiresFix.enable", enableRP2wiresfix);
		config.set("RP2WiresFix.wiresIDs",new ArrayList<String>(RP2WiresIDs));
		config.set("FreeCamInvFix.enable",enableFreecamFix);
		config.set("FreeCamInvFix.checkBlockIDs",new ArrayList<String>(freecamBlockIDs));
		config.set("FreeCamInvFix.zeroItemsCheck.enabled",enablefreecamzeroitemscheck);
		config.set("HopperMinecartFix.enabled",enablehopperminecartfix);
		config.set("HopperMinecartFix.HopperMinecartID",hopperminecartid);
		config.set("BagFrameInsertFix.enabled",enableBagFrameInsertfix);
		config.set("BagFrameInsertFix.bagIDs",new ArrayList<Integer>(bagids));
		config.set("BagFrameInsertFix.frameentity",frameentity);
		config.set("BagFrameInsertFix.gregIDs",new ArrayList<String>(frameids));
		
		try {
			config.save(new File("plugins/ModFix/config.yml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
