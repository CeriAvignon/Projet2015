package fr.univavignon.courbes.graphics.simpleimpl;

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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;

/**
 * Contient les méthodes permettant de charger les images des items
 * puis de les dessiner pendant le jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ItemDrawer
{	
	/**
	 * Initialise les données nécessaires au tracé des items.
	 * En particulier, charge les images des items, si pas déjà fait.
	 * 
	 * @param board
	 * 		Aire de jeu de la partie actuelle. 
	 * 
	 * @throws IOException 
	 * 		Problème lors du chargement d'une image.
	 */
	public ItemDrawer(Board board) throws IOException
	{	// on ne charge les images que si nécessaire (i.e. la 1ère fois)
		if(IMAGES.isEmpty())
		{	String imageFolder = "res" + File.separator + "images" + File.separator;
			for(ItemType item: ItemType.values())
			{	String fileName = imageFolder + item.toString() + ".png";
				File imageFile = new File(fileName);
				BufferedImage image = ImageIO.read(imageFile);
				IMAGES.put(item, image);
			}
		}
	}
	
	/** Map contenant les images représentant les items, chargées une seule fois */
	private static final Map<ItemType,BufferedImage> IMAGES = new HashMap<ItemType, BufferedImage>();
	
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
	private void drawItem(ItemType item, int x, int y, Graphics g)
	{	BufferedImage image = IMAGES.get(item);
		g.drawImage(image,x-Constants.ITEM_RADIUS,y-Constants.ITEM_RADIUS,null);
	}
	
	/**
	 * Trace tous les items contenus dans la map spécifiée, sur l'objet
	 * graphique passé en paramètre.
	 * 
	 * @param board
	 * 		Aire de jeu à afficher.
	 * @param g
	 * 		Objet graphique sur lequel il faut dessiner.
	 */
	public void drawItems(Board board, Graphics g)
	{	for(ItemInstance item: board.items)
			drawItem(item.type, item.x, item.y, g);
	}
}
