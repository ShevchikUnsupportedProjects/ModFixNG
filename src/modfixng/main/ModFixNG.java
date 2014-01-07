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

import modfixng.fixes.FixBag19;
import modfixng.fixes.FixBagFrameInsert;
import modfixng.fixes.FixFreecamBlocks;
import modfixng.fixes.FixFreecamEntities;
import modfixng.fixes.FixHopperMinecart;
import modfixng.fixes.FixSlotDesync;
import modfixng.fixes.RestrictBreakWhileOpen;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

//warning: this plugin requires ProtocolLib to run
public class ModFixNG extends JavaPlugin {

	private Config config;
	
	private Commands commandl;
	private FixBag19 bagl;
	private FixFreecamEntities mpl;
	private FixFreecamBlocks fciol;
	private FixHopperMinecart hpl;
	private FixBagFrameInsert bfil;
	private RestrictBreakWhileOpen rbwol;
	@SuppressWarnings("unused")
	private FixSlotDesync fsl;
	
	public ProtocolManager protocolManager = null;
	
	@Override
	public void onEnable() 
	{
		protocolManager = ProtocolLibrary.getProtocolManager();
		config = new Config(this);
		config.loadConfig();
		commandl = new Commands(this,config);
		getCommand("modfix").setExecutor(commandl);
		getServer().getPluginManager().registerEvents(commandl, this);
		bagl = new FixBag19(this,config);
		getServer().getPluginManager().registerEvents(bagl, this);
		mpl = new FixFreecamEntities(this,config);
		getServer().getPluginManager().registerEvents(mpl, this);
		fciol = new FixFreecamBlocks(this,config);
		getServer().getPluginManager().registerEvents(fciol, this);
		hpl = new FixHopperMinecart(this,config);
		getServer().getPluginManager().registerEvents(hpl, this);
		bfil = new FixBagFrameInsert(this,config);
		getServer().getPluginManager().registerEvents(bfil, this);
		fsl = new FixSlotDesync(this, config);
		rbwol = new RestrictBreakWhileOpen(this, config);
		getServer().getPluginManager().registerEvents(rbwol, this);
	}	
	@Override
	public void onDisable() 
	{
		for (Player p : getServer().getOnlinePlayers())
		{
			p.closeInventory();
		}
		config = null;
		commandl = null;
		mpl = null;
		fciol = null;
		hpl = null;
		bfil = null;
		fsl = null;
		protocolManager.removePacketListeners(this);
		protocolManager = null;
	}
	
	
	
}
