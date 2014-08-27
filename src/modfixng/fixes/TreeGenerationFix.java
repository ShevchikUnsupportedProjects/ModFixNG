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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import modfixng.main.ModFixNG;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

public class TreeGenerationFix implements Feature {

	private BukkitTask task;

	//reset cauldron captureTreeGeneration field every tick
	private void scheduleFieldResetTask() {
		task = Bukkit.getScheduler().runTaskTimer(
			ModFixNG.getInstance(),
			new Runnable() {
				@Override
				public void run() {
					for (World world : Bukkit.getWorlds()) {
						try {
							Method getHandleMethod = world.getClass().getDeclaredMethod("getHandle");
							getHandleMethod.setAccessible(true);
							Object nmsworld = getHandleMethod.invoke(world);
							Field captureTreeGenerationField = nmsworld.getClass().getField("captureTreeGeneration");
							captureTreeGenerationField.setAccessible(true);
							captureTreeGenerationField.set(nmsworld, false);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
							e.printStackTrace();
						}
					}
				}
			}, 
			0, 1
		);
	}

	@Override
	public void load() {
		scheduleFieldResetTask();
	}

	@Override
	public void unload() {
		task.cancel();
	}

	@Override
	public String getName() {
		return "TreeGenerationFix";
	}

}