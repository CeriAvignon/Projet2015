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

import javax.swing.JPanel;

import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;

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
	 * en cours de manche.
	 */
	public BoardPanel()
	{	// on fixe les dimensions du panel
		int boardWidth = SettingsManager.getBoardWidth();
		int boardHeight = SettingsManager.getBoardHeight();
		Dimension dim = new Dimension(boardWidth, boardHeight);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
	}
}
