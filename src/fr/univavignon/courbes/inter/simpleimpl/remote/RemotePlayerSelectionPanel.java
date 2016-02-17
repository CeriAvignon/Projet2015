package fr.univavignon.courbes.inter.simpleimpl.remote;

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

import fr.univavignon.courbes.inter.simpleimpl.MainWindow;

/**
 * Panel représentant une liste de joueurs distants.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface RemotePlayerSelectionPanel
{	
	/**
	 * Renvoie la fenêtre principale contenant ce panel.
	 * 
	 * @return
	 * 		Fenêtre principale contenant le panel.
	 */
	public MainWindow getMainWindow();
	
	/**
	 * Renvoie le nombre de joueurs actuellement sélectionnés.
	 * 
	 * @return
	 * 		Nombre de joueurs sélectionnés.
	 */
	public int getSelectedProfileCount();
	
	/**
	 * Indique au panel principal qu'il doit retirer le joueur
	 * dont le numéro est indiqué (suite à un kick).
	 * 
	 * @param playerId
	 * 		Numéro du joueur à dégager.
	 */
	public void kickPlayer(int playerId);
	
	/**
	 * Renvoie la largeur (en pixels) accordée aux noms des joueurs.
	 * 
	 * @return
	 * 		Largeur dédiée aux noms des joueurs.
	 */
	public int getNameWidth();
	
	/**
	 * Renvoie la largeur (en pixels) accordée aux rangs ELO des joueurs.
	 * 
	 * @return
	 * 		Largeur dédiée aux rangs ELO des joueurs.
	 */
	public int getEloWidth();
	
	/**
	 * Renvoie la largeur (en pixels) accordée aux boutons de rejet des joueurs.
	 * 
	 * @return
	 * 		Largeur dédiée aux boutons de rejet des joueurs.
	 */
	public int getKickWidth();
}
