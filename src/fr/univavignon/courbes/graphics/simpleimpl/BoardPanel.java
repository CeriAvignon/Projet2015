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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;

/**
 * Panel utilisé pour afficher l'aire de jeu. Il se contente d'afficher une image,
 * qui est elle-même mise à jour à chaque itération par le Moteur Graphique.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class BoardPanel extends JPanel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée un panel permettant d'afficher l'aire de jeu
	 * en cours de partie.
	 * 
	 * @param board
	 * 		L'aire de jeu à afficher.
	 */
	public BoardPanel(Board board)
	{	// on fixe les dimensions du panel
		Dimension dim = new Dimension(board.width, board.height);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
	}
	
	/** L'image affichée par ce panel */
	private BufferedImage image = null;

	/**
	 * Méthode utilisée par le thread de mise à jour de
	 * l'image, afin de répercuter les dernières modifications dans
	 * le panel. La méthode doit être synchronisée, car l'image
	 * accédée est aussi utilisée (en lecture) par un autre thread: le thread
	 * Swing, qui est chargé de l'affichage du panel.
	 * 
	 * @param image
	 * 		Nouvelle image à afficher dans ce panel.
	 */
	public synchronized void setImage(BufferedImage image)
	{	this.image = image;
	}
	
	/**
	 * Renvoie l'image à afficher, de façon synchronisée
	 * afin d'éviter les conflits avec le thread qui fait
	 * la mise à jour de l'image. En effet, cette méthode
	 * est utilisée par le thread Swing, qui est distinct.
	 * 
	 * @return
	 * 		L'image à afficher (ou {@code null} si pas encore initialisée).
	 */
	private synchronized BufferedImage getImage()
	{	return image;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{	super.paintComponent(g);
	
		// on n'accède pas directement à l'image : on passe par une méthode synchronisée,
		// afin d'éviter tout conflit avec le thread du Moteur Graphique (on est ici dans le thread Swing)
		BufferedImage img = getImage();
		if(img!=null)
			g.drawImage(image, 0, 0, null);
    }
}
