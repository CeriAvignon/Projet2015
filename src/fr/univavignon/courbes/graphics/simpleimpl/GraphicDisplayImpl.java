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

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.graphics.GraphicDisplay;

/**
 * Implémentation de l'interface {@link GraphicDisplay}.
 * Autrement dit, la classe principale du moteur graphique
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class GraphicDisplayImpl implements GraphicDisplay
{	
	@Override
	public void init(Round round)
	{	Board board = round.board;
		
		cacheImage = new BufferedImage(board.width,board.height,BufferedImage.TYPE_INT_ARGB);
		currentImage = new BufferedImage(board.width,board.height,BufferedImage.TYPE_INT_ARGB);
		try
		{	boardDrawer = new BoardDrawer(board);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		boardPanel = new BoardPanel(board);
		
		scorePanel = new ScorePanel(round.players);
	}

	/** Panel utilisé pour afficher l'aire de jeu */
	private BoardPanel boardPanel;
	/** Image dans laquelle on dessine l'aire de jeu (i.e. cache écran) */
	private BufferedImage cacheImage;
	/** Image actuellement utilisée par le {@link BoardPanel} */
	private BufferedImage currentImage;
	/** Objet utilisé pour dessiner dans l'image cache */
	private BoardDrawer boardDrawer;
	/** Panel utilisé pour afficher le score */
	private ScorePanel scorePanel;
	
	@Override
	public void update(Round round)
	{	// mise à jour du score
		scorePanel.updateData(round.pointLimit, round.players);
		
		// mise à jour de l'image cache
		Graphics g = cacheImage.getGraphics();
		boardDrawer.drawBoard(round.board, g);
		boardPanel.setImage(cacheImage);
		BufferedImage temp = currentImage;
		currentImage = cacheImage;
		cacheImage = temp;
	}
	
	@Override
	public JPanel getBoardPanel()
	{	return boardPanel;
	}
	
	@Override
	public JPanel getScorePanel()
	{	return scorePanel;	
	}
	
	@Override
	public void end()
	{	
		// TODO rien de spécial à faire pour le moment. 
		// libérer ressources GUI ? (mais le GC s'en occupe...)
	}
}

// TODO reproduire l'animation d'apparition des items

// TODO faut représenter les sens de déplacement/directions possibles juste avant le temps d'entrée
//      peut être utiliser un autre mode (présentation ?) pour ça
