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

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class ModFixNG extends JavaPlugin {

	private Config config;
	
	private Commands commandl;
	private FixBag19 bagl;
	private ForceInventoryCloseOnChunkChange chunkl;
	private FixFreecamEntities mpl;
	private FixFreecamBlocks fciol;
	private ForbidHopperMinecartEnter hpl;
	private FixBagFrameInsert bfil;
	
	public ProtocolManager protocolManager = null;
	
	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		//init config
		config = new Config(this);
		config.loadConfig();
		//init command listener
		commandl = new Commands(this,config);
		getCommand("modfix").setExecutor(commandl);
		getServer().getPluginManager().registerEvents(commandl, this);
		//init bag bugfix listener
		bagl = new FixBag19(this,config);
		getServer().getPluginManager().registerEvents(bagl, this);
		//init chunk bugfix listener
		chunkl = new ForceInventoryCloseOnChunkChange(this,config);
		getServer().getPluginManager().registerEvents(chunkl, this);
		//init minecart bugfix listener
		mpl = new FixFreecamEntities(this,config);
		getServer().getPluginManager().registerEvents(mpl, this);
		//init freecam fix listener
		fciol = new FixFreecamBlocks(this,config);
		getServer().getPluginManager().registerEvents(fciol, this);
		//init hopperminecart fix listener
		hpl = new ForbidHopperMinecartEnter(this,config);
		getServer().getPluginManager().registerEvents(hpl, this);
		//init bag insert into frame fix listener
		bfil = new FixBagFrameInsert(this,config);
		getServer().getPluginManager().registerEvents(bfil, this);
	}	
	@Override
	public void onDisable() {
		for (Player p : getServer().getOnlinePlayers())
		{
			p.closeInventory();
		}
		config = null;
		commandl = null;
		HandlerList.unregisterAll(this);
		mpl = null;
		fciol = null;
		hpl = null;
		bfil = null;
		protocolManager.getAsynchronousManager().unregisterAsyncHandlers(this);
		protocolManager = null;
	}
	
	
	
}
