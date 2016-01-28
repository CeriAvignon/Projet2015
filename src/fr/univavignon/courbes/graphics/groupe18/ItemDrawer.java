package fr.univavignon.courbes.graphics.groupe18;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;

/**
 * Contient les méthodes permettant de charger les images des items
 * puis de les dessiner pendant le jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ItemDrawer
{	
	/** Map contenant les images représentant les items, chargées une seule fois */
	private static final Map<Item,BufferedImage> IMAGES = new HashMap<Item, BufferedImage>();
	
	/**
	 * Initialise les données nécessaires au tracé des items.
	 * En particulier, charge les images des items, si pas déjà fait.
	 * 
	 * @throws IOException 
	 * 		Problème lors du chargement d'une image.
	 */
	public static void initialize() throws IOException
	{	// on ne charge les images que si nécessaire (i.e. la 1ère fois)
		if(IMAGES.isEmpty())
		{	String imageFolder = "res" + File.separator + "images" + File.separator;
			for(Item item: Item.values())
			{	String fileName = imageFolder + item.toString();
				File imageFile = new File(fileName);
				BufferedImage image = ImageIO.read(imageFile);
				IMAGES.put(item, image);
			}
		}
	}
	
	/**
	 * Trace l'item spécifié à la position indiquée, sur l'objet
	 * graphique passé en paramètre.
	 * 
	 * @param item
	 * 		Type d'item à tracer.
	 * @param x
	 * 		Position du centre de l'item en abscisse.
	 * @param y
	 * 		Position du centre de l'item en ordonnée.
	 * @param g
	 * 		Objet graphique sur lequel il faut dessiner.
	 */
	private static void drawItem(Item item, int x, int y, Graphics g)
	{	BufferedImage image = IMAGES.get(item);
		g.drawImage(image,x-Constants.ITEM_SIZE/2,y-Constants.ITEM_SIZE/2,null);
	}
	
	/**
	 * Trace tous les items contenus dans la map spécifiée, sur l'objet
	 * graphique passé en paramètre.
	 * 
	 * @param itemsMap
	 * 		Map contenant les type des items à tracer, avec leurs positions respectives.
	 * @param g
	 * 		Objet graphique sur lequel il faut dessiner.
	 */
	public static void drawItems(Map<Position, Item> itemsMap, Graphics g)
	{	for(Entry<Position, Item> entry: itemsMap.entrySet())
		{	Position position = entry.getKey();
			Item item = entry.getValue();
			drawItem(item, position.x, position.y, g);
		}
	}
}
