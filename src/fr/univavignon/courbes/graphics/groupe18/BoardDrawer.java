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
import java.io.IOException;

import fr.univavignon.courbes.common.Board;
 
/**
 * Contient les méthodes permettant de dessiner l'aire de jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class BoardDrawer
{	
//    BoardDrawer(int X, int Y, Board board) {
//        setPreferredSize(new Dimension(X, Y));
//        this.board = board;
//    }
    
	/**
	 * Initialise les données nécessaires au tracé de l'aire de jeu.
	 * 
	 * @throws IOException
	 * 		Problème lors de l'initialisation des objets chargés du tracé.
	 */
	public static void initialize() throws IOException
	{
		ItemDrawer.initialize();
		SnakesDrawer.initialize();
	}
	
	/**
	 * Trace l'aire de jeu. L'objet reçu est supposé être une copie
	 * de l'instance mise à jour simultanément par le moteur physique,
	 * sinon des erreurs de concurrence peuvent se produire.
	 * 
	 * @param board
	 * 		Copie de l'aire de jeu à tracer.
	 * @param g
	 * 		Objet graphique sur lequel on trace les serpents.
	 */
	public static void drawBoard(Board board, Graphics g)
    {	
// TODO a priori, ceci est inutile : c'est le MP qui doit gérer ça      
//        for(int i = 0; i < board.snakes.length; i++) {
//        	g.setColor(Constants.PLAYER_COLORS[board.snakes[i].playerId]);
//        	int x = (int)Math.round(board.snakes[i].headRadius);
//        	g.drawOval(board.snakes[i].currentX-(x/2), board.snakes[i].currentY-(x/2), x, x);
//        	g.fillOval(board.snakes[i].currentX-(x/2), board.snakes[i].currentY-(x/2), x, x);
//        	
//        }
        
        // on dessine les snakes
		SnakesDrawer.drawSnakes(board.snakes, board.snakesMap, g);
		
		// on dessine les items
		ItemDrawer.drawItems(board.itemsMap, g);
    }          
}
