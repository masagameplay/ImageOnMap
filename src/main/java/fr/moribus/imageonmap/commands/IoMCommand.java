/*
 * Copyright (C) 2013 Moribus
 * Copyright (C) 2015 ProkopyL <prokopylmc@gmail.com>
 * Copyright (C) 2018 Masa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.moribus.imageonmap.commands;

import fr.moribus.imageonmap.map.ImageMap;
import fr.moribus.imageonmap.map.MapManager;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public abstract class IoMCommand extends Command
{
	protected ImageMap getMapFromArgs() throws CommandException
	{
		return getMapFromArgs(playerSender());
	}

	private ImageMap getMapFromArgs(Player player) throws CommandException
	{
		if(args.length <= 0) throwInvalidArgument(I.t("You need to give a map name."));

		ImageMap map;
		StringBuilder mapName = new StringBuilder(args[0]);

		for(int i = 1, c = args.length; i < c; i++)
		{
			mapName.append(" ").append(args[i]);
		}

		mapName = new StringBuilder(mapName.toString().trim());

		map = MapManager.getMap(player.getUniqueId(), mapName.toString());

		if(map == null) error(I.t("This map does not exist."));

		return map;
	}

	protected List<String> getMatchingMapNames(Player player, String prefix)
	{
		return getMatchingMapNames(MapManager.getMapList(player.getUniqueId()), prefix);
	}

	private List<String> getMatchingMapNames(Iterable<? extends ImageMap> maps, String prefix)
	{
		List<String> matches = new ArrayList<>();

		for(ImageMap map : maps)
		{
			if(map.getId().startsWith(prefix)) matches.add(map.getId());
		}

		return matches;
	}
}
