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

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MFCommandListener implements  CommandExecutor,Listener{
	@SuppressWarnings("unused")
	private Main main;
	private ModFixConfig config;
	
	MFCommandListener(Main main, ModFixConfig config) {
		this.main = main;
		this.config = config;
	}

	
	private HashSet<String> pleinfoswitch = new HashSet<String>();
	private HashSet<String> plbinfoswitch = new HashSet<String>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2,
			String[] args) {
		Player player = null;
		
		//check permissions
		if ((sender instanceof Player)) {
		// Player, lets check if player isOp or have permission
		player = (Player) sender;
		if (!player.isOp() && !player.hasPermission("modfix.conf")) 
		{
		sender.sendMessage(ChatColor.BLUE+"Нет прав");
		return true;
		}
		} else if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
		// Success, this was from the Console or Remote Console
		} else {
		// Who are you people?
		sender.sendMessage(ChatColor.BLUE+"Ты вообще кто такой? давай до свиданья!");
		return true;
		}
		
		//now handle commands
		if (args.length == 0) {
			sender.sendMessage(ChatColor.BLUE+"Используйте команд /modfix help для получения списка комманд");
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
		{
			config.loadConfig();
			sender.sendMessage(ChatColor.BLUE+"Конфиг перезагружен");
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("help"))
		{
			displayHelp(sender);
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("iinfo"))
		{
			displayItemInfo(sender);
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("binfo"))
		{
			displayBlockInfo(sender);
			return true;
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("einfo"))
		{
			displayEntityInfo(sender);
			return true;
		}
		return false;
	}
	
	
	
	
	private void displayHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.AQUA+"/modfix reload "+ChatColor.WHITE+"-"+ChatColor.BLUE+" перезагрузить конфиг плагина");
		sender.sendMessage(ChatColor.AQUA+"/modfix iinfo "+ChatColor.WHITE+"-"+ChatColor.BLUE+" получить id и subid итема в руке");
		sender.sendMessage(ChatColor.AQUA+"/modfix einfo "+ChatColor.WHITE+"-"+ChatColor.BLUE+" получить Entity Type ID entity через ПКМ");
		sender.sendMessage(ChatColor.AQUA+"/modfix binfo "+ChatColor.WHITE+"-"+ChatColor.BLUE+" получить id и subid блока после ПКМ");
	}
	
	private void displayItemInfo(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			Player pl = (Player) sender;
			String msg =ChatColor.BLUE+"Item id: "+pl.getItemInHand().getTypeId();
			if (pl.getItemInHand().getDurability() !=0 )
			{
				msg+=", subid: "+pl.getItemInHand().getDurability();
			}
			pl.sendMessage(msg);
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"У консоли нет рук");
		}
	}
	
	private void displayBlockInfo(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			Player pl = (Player) sender;
			pl.sendMessage(ChatColor.BLUE+"Кликните правой кнопкой мыши по блоку, для того чтобы узнать его ID и subID");
			plbinfoswitch.add(pl.getName());
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"У консоли нет рук");
		}
	}
	
	private void displayEntityInfo(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			Player pl = (Player) sender;
			pl.sendMessage(ChatColor.BLUE+"Кликните правой кнопкой мыши по Entity, для того чтобы узнать её Type ID");
			pleinfoswitch.add(pl.getName());
		}
		else
		{
			sender.sendMessage(ChatColor.BLUE+"У консоли нет рук");
		}
	}
	

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerCheckEntityID(PlayerInteractEntityEvent e)
	{
		Player pl = e.getPlayer();
		if (pleinfoswitch.contains(pl.getName()))
		{
			pl.sendMessage(ChatColor.BLUE+"Entity Type ID: "+e.getRightClicked().getType().getTypeId());
			pleinfoswitch.remove(pl.getName());
			e.setCancelled(true);
		}
	}
	

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerCheckBlockID(PlayerInteractEvent e)
	{
		if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {return;}
		
		Player pl = e.getPlayer();
		if (plbinfoswitch.contains(pl.getName()))
		{
			String msg = ChatColor.BLUE+"Block id: "+e.getClickedBlock().getTypeId();
			if (e.getClickedBlock().getData() !=0)
			{
				msg+=", subid: "+e.getClickedBlock().getData();
			}
			pl.sendMessage(msg);
			plbinfoswitch.remove(pl.getName());
			e.setCancelled(true);
		}
		
	}

	
		
}


