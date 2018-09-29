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

package fr.moribus.imageonmap.image;

import fr.moribus.imageonmap.ImageOnMap;
import fr.moribus.imageonmap.map.ImageMap;
import fr.zcraft.zlib.components.worker.Worker;
import fr.zcraft.zlib.components.worker.WorkerAttributes;
import fr.zcraft.zlib.components.worker.WorkerRunnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;


@WorkerAttributes (name = "Image IO")
public class ImageIOExecutor extends Worker
{
    static void loadImage(final File file, final Renderer mapRenderer)
    {
        submitQuery(new WorkerRunnable<Void>()
        {
            @Override
            public Void run() throws Exception
            {
                BufferedImage image = ImageIO.read(file);
                mapRenderer.setImage(image);
                return null;
            }
        });
    }
    
    private static void saveImage(final File file, final BufferedImage image)
    {
        submitQuery(new WorkerRunnable<Void>()
        {
            @Override
            public Void run() throws Throwable
            {
                ImageIO.write(image, "png", file);
                return null;
            }
        });
    }
    
    static void saveImage(short mapID, BufferedImage image)
    {
        saveImage(ImageOnMap.getPlugin().getImageFile(mapID), image);
    }
    
    static void saveImage(short[] mapsIDs, PosterImage image)
    {
        for(int i = 0, c = mapsIDs.length; i < c; i++)
        {
            ImageIOExecutor.saveImage(ImageOnMap.getPlugin().getImageFile(mapsIDs[i]), image.getImageAt(i));
        }
    }
    
    static public void deleteImage(ImageMap map)
    {
        short[] mapsIDs = map.getMapsIDs();
        for (short mapsID : mapsIDs) {
            deleteImage(ImageOnMap.getPlugin().getImageFile(mapsID));
        }
    }
    
    private static void deleteImage(final File file)
    {
        submitQuery(new WorkerRunnable<Void>()
        {
            @Override
            public Void run() throws Throwable
            {
                Files.delete(file.toPath());
                return null;
            }
        });
    }
}
