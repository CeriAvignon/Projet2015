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

import java.awt.BasicStroke;
import java.awt.Color; 
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

/**
 * Contient les méthodes permettant de dessiner les serpents.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class SnakeDrawer
{	
	/**
	 * Initialise les données nécessaires au tracé des snakes.
	 * 
	 * @param board 
	 * 		Aire de jeu de la partie en cours.
	 */
	public SnakeDrawer(Board board)
	{	
		// TODO on pourrait, par exemple, rajouter un tirage au sort des couleurs des joueurs.
	}
	
	/**
	 * Trace tous les serpents présents dans l'aire de jeu.
	 * 
	 * @param board
	 * 		Aire de jeu à afficher.
	 * @param g
	 * 		Objet graphique sur lequel on trace les serpents.
	 */
	public void drawSnakes(Board board, Graphics g)
	{	Snake snakes[] = board.snakes;
		
		// on boucle sur chaque joueur
		for(int playerId=0;playerId<snakes.length;playerId++)
		{	// on récupère la couleur du joueur dans la partie
			Snake snake = snakes[playerId];
			Color playerColor;
			if(snake.connected)
				playerColor = Constants.PLAYER_COLORS[playerId];
			else
				playerColor = Constants.DISC_PLAYER_COLOR;
			g.setColor(playerColor);
			
			// on boucle sur chaque position, i.e. chaque pixel
			for(Position position: snake.trail)
				g.drawLine(position.x, position.y, position.x, position.y);
			
			// si le serpent est vivant, on trace sa tête
			if(snake.eliminatedBy==null)
			{	Graphics2D g2 = (Graphics2D)g;
				g2.fillOval(snake.currentX-snake.headRadius, snake.currentY-snake.headRadius*2,
					snake.headRadius*2, snake.headRadius*2);
			}
		}
	}
	
	/**
	 * Méthode chargée de tracer les auréoles représentant le temps d'effet restant pour
	 * chaque item affectant actuellement un serpent. Cette méthode doit être appelée après
	 * les autres, car les auréoles se superposent aux serpents et aux items parsement l'aire 
	 * de jeu.
	 *  
	 * @param board
	 * 		Aire de jeu à afficher.
	 * @param g
	 * 		Objet graphique sur lequel on trace les auréoles.
	 */
	public void drawAureolas(Board board, Graphics g)
	{	Snake snakes[] = board.snakes;
		
		// on change l'épaisseur du stylo
		Graphics2D g2 = (Graphics2D) g;
		Stroke oldStroke = g2.getStroke();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(new BasicStroke(Constants.AUREOLA_THICKNESS));
		
		// on traite chaque serpent individuellement
		for(Snake snake: snakes)
		{	// on récupère le centre des auréoles (i.e. centre de la tête)
			int x = snake.currentX;
			int y = snake.currentY;
			
			// initialisation de l'espace entre la tête et l'auréole à tracer
			int offset = snake.headRadius + Constants.AUREOLA_SPACE*2;
			
			// on traite séparément chaque item qui affecte actuellement le serpent
			// chacun sera représenté sous la forme d'une auréole
			for(ItemInstance item: snake.currentItems)
			{	// on détermine la couleur en fonction du type d'item
				ItemType type = item.type;
				Color color = type.color;
				g.setColor(color);
				
				// on trace un arc de cercle centré sur la tête
				// sa longueur est proportionnelle au temps d'effet restant à l'item 
				int radius = offset + Constants.AUREOLA_THICKNESS/2;
				double startAngle = Math.toDegrees(snake.currentAngle); // angle de déplacement du serpent
				double extentAngle = 360*item.remainingTime/(double)type.duration;
				Arc2D arc = new Arc2D.Double(x-radius, y-radius, 2*radius, 2*radius, startAngle, extentAngle, Arc2D.OPEN);
				g2.draw(arc);
				
				// on met à jour l'espace avec l'auréole suivante
				offset = offset + Constants.AUREOLA_THICKNESS + Constants.AUREOLA_SPACE;
			}
		}
		
		// on rétablit l'épaisseur normale du stylo
		g2.setStroke(oldStroke);
	}
}
