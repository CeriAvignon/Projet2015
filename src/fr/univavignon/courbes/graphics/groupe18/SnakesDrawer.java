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

import java.awt.Color; 
import java.awt.Graphics;
import java.util.Map;
import java.util.Map.Entry;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

 
/**
 * Contient les méthodes permettant de dessiner les serpents.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class SnakesDrawer
{	
	/**
	 * Initialise les données nécessaires au tracé des snakes.
	 * Ca pourrait être (par exemple) un tirage au sort des couleurs.
	 */
	public static void initialize()
	{
		// rien à faire pour l'instant...
	}
	
	/**
	 * Trace tous les serpents présents dans l'aire de jeu.
	 * 
	 * @param snakes
	 * 		Tableau contenat tous les serpents.
	 * @param snakesMap
	 * 		Map contenant les pixels occupés par les serpents.
	 * @param g
	 * 		Objet graphique sur lequel on trace les serpents.
	 */
	public static void drawSnakes(Snake snakes[], Map<Position, Integer> snakesMap, Graphics g)
	{	// on boucle sur chaque entrée de la map, i.e. chaque pixel
		for(Entry<Position, Integer> entry: snakesMap.entrySet())
		{	// on récupère l'id du joueur dans la partie
			int playerId = entry.getValue();
			
			// on récupère la couleur du joueur dans la partie
			Snake snake = snakes[playerId];
			Color playerColor;
			if(snake.connected)
				playerColor = Constants.PLAYER_COLORS[playerId];
			else
				playerColor = Constants.DISCONNECTED_PLAYER;
			g.setColor(playerColor);
			
			// on trace le pixel
			Position position = entry.getKey();
			g.drawLine(position.x, position.y, position.x, position.y);
		}
	}
}
