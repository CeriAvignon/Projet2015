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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.io.IOException;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
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
		
		image = null;
		try
		{	boardDrawer = new BoardDrawer(board);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		boardPanel = new BoardPanel(board);
		
		scorePanel = new ScorePanel(round.players,Constants.SCORE_WIDTH);
	}

	/** Panel utilisé pour afficher l'aire de jeu */
	private BoardPanel boardPanel;
	/** Image dans laquelle on dessine */
	private Image image;
	/** Objet utilisé pour dessiner dans l'image cache */
	private BoardDrawer boardDrawer;
	/** Panel utilisé pour afficher le score */
	private ScorePanel scorePanel;
	
	@Override
	public void update(Round round)
	{	if(image==null)
			initImage();
		
		// mise à jour du score
		scorePanel.updateData(round);
		
		// on réinitialise l'image buffer
		boolean again;
		do
		{	GraphicsConfiguration gc = boardPanel.getGraphicsConfiguration();
			int valid = ((VolatileImage)image).validate(gc);
			if(valid == VolatileImage.IMAGE_INCOMPATIBLE)
				initImage();
			Graphics2D g = (Graphics2D)image.getGraphics();
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// on dessine le fond
			g.setColor(Color.BLACK);
			Dimension dim = boardPanel.getPreferredSize();
			g.fillRect(0,0,dim.width,dim.height);
			// on dessine l'aire de jeu
			boardDrawer.drawBoard(round, g);
			g.dispose();
			// on recopie sur le panel
			Graphics gp = boardPanel.getGraphics();
			if(gp!=null)
			{	gp.drawImage(image, 0, 0, null);
				gp.dispose();
			}
			again = ((VolatileImage)image).contentsLost();
		}
		while(again);
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
	
	/**
	 * Crée l'image cache dans laquelle on va dessiner.
	 */
	private void initImage()
	{	int width = boardPanel.getPreferredSize().width;
		int height = boardPanel.getPreferredSize().height;
		GraphicsConfiguration gc = boardPanel.getGraphicsConfiguration();
		VolatileImage temp = gc.createCompatibleVolatileImage(width,height,Transparency.OPAQUE);
		int valid = temp.validate(gc);
		if (valid == VolatileImage.IMAGE_INCOMPATIBLE)
			temp = gc.createCompatibleVolatileImage(width,height,Transparency.OPAQUE);
		image = temp;
	}
}

// TODO reproduire l'animation d'apparition des items

// TODO faut représenter les sens de déplacement/directions possibles juste avant le temps d'entrée
//      peut être utiliser un autre mode (présentation ?) pour ça
