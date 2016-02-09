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

import java.awt.Graphics2D;
import java.io.IOException;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Round;
 
/**
 * Contient les méthodes permettant de dessiner l'aire de jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class BoardDrawer
{	
	/**
	 * Initialise les données nécessaires au tracé de l'aire de jeu.
	 * 
	 * @param board
	 * 		Aire de jeu de la manche courante. 
	 * 
	 * @throws IOException
	 * 		Problème lors de l'initialisation des objets chargés du tracé.
	 */
	public BoardDrawer(Board board) throws IOException
	{	snakesDrawer = new SnakeDrawer(board);
		itemDrawer = new ItemDrawer(board);
	}
	
	/** Objet utilisé pour dessiner les serpents et leurs auréoles */
	private SnakeDrawer snakesDrawer;
	/** Objet utilisé pour dessiner les items présents sur l'aire de jeu */
	private ItemDrawer itemDrawer;
	
	
	/**
	 * Trace la bordure de l'aire de jeu, qui empêche les serpents
	 * de traverser les limites de l'aire de jeu pour réapparaitre
	 * de l'autre côté.
	 * 
	 * @param board
	 * 		Aire de jeu à afficher.
	 * @param g
	 * 		Objet graphique sur lequel on trace la bordure.
	 */
	private void drawBorder(Board board, Graphics2D g)
	{	if(board.hasBorder)
		{	g.setColor(Constants.BORDER_COLOR);
			// haut
			g.fillRect(
				0, 
				0, 
				Constants.BOARD_WIDTH, 
				Constants.BORDER_THICKNESS
			);
			// bas
			g.fillRect(
				0, 
				Constants.BOARD_HEIGHT-Constants.BORDER_THICKNESS, 
				Constants.BOARD_WIDTH, 
				Constants.BORDER_THICKNESS
			);
			// gauche
			g.fillRect(
				0, 
				Constants.BORDER_THICKNESS, 
				Constants.BORDER_THICKNESS, 
				Constants.BOARD_HEIGHT-2*Constants.BORDER_THICKNESS
			);
			// droite
			g.fillRect(
				Constants.BOARD_WIDTH-Constants.BORDER_THICKNESS, 
				Constants.BORDER_THICKNESS, 
				Constants.BORDER_THICKNESS, 
				Constants.BOARD_HEIGHT-2*Constants.BORDER_THICKNESS
			);
		}
	}
	
	/**
	 * Trace l'aire de jeu.
	 * 
	 * @param round
	 * 		La manche en cours.
	 * @param g
	 * 		Objet graphique sur lequel on trace les serpents.
	 */
	public void drawBoard(Round round, Graphics2D g)
    {	// on dessine d'abord les serpents
		snakesDrawer.drawSnakes(round, g);
		
		// puis la bordure
		drawBorder(round.board, g);
				
        // puis les items
		itemDrawer.drawItems(round.board, g);
		
		// puis les auréoles des serpents
		snakesDrawer.drawAureolas(round.board, g);
    }
}
