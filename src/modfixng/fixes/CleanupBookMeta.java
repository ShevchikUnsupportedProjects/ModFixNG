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

package modfixng.fixes;

import modfixng.main.ModFixNG;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class CleanupBookMeta implements Listener, Feature {

	@EventHandler
	public void onBookEdit(PlayerEditBookEvent event) {
		BookMeta oldmeta = event.getNewBookMeta();
		BookMeta newmeta = (BookMeta) Bukkit.getItemFactory().getItemMeta(Material.BOOK_AND_QUILL);
		if (oldmeta.hasAuthor()) {
			newmeta.setAuthor(oldmeta.getAuthor());
		}
		if (oldmeta.hasTitle()) {
			newmeta.setTitle(oldmeta.getTitle());
		}
		if (oldmeta.hasPages()) {
			newmeta.setPages(oldmeta.getPages());
		}
		event.setNewBookMeta(newmeta);
	}

	@Override
	public String getName() {
		return "CleanupBookMeta";
	}

	@Override
	public void load() {
		Bukkit.getPluginManager().registerEvents(this, ModFixNG.getInstance());
	}

	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
	}

}
