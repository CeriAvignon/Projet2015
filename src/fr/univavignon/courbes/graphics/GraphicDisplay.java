package fr.univavignon.courbes.graphics;

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

import fr.univavignon.courbes.common.Round;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur de 
 * demander au Moteur Graphique de rafraichir l'affichage du jeu.
 * <br/>
 * Chaque binôme de la composante Moteur Graphique doit définir une classe
 * implémentant cette interface, qui sera instanciée par l'Interface Utilisateur. 
 */
public interface GraphicDisplay
{	
	/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * au début de chaque manche.
	 * 
	 * @param round
	 * 		Données de la partie en cours, incluant l'aire de jeu et les joueurs.
	 */
	public void init(Round round);
	
	/**
	 * Renvoie le panel de l'aire de jeu, initialisé par le Moteur Graphique.
	 * 
	 * @return
	 * 		Le panel graphique utilisé comme support pour dessiner l'aire de jeu.
	 */
	public JPanel getBoardPanel();
	
	/**
	 * Renvoie le panel des scores, initialisé par le Moteur Graphique.
	 * 
	 * @return
	 * 		Le panel graphique utilisé comme support pour dessiner les scores.
	 */
	public JPanel getScorePanel();
	
	/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * à chaque itération d'une manche. Elle raffraichit la représentation
	 * graphique du jeu.
	 * 
	 * @param round
	 * 		Objet représentant la partie courante.
	 */
	public void update(Round round);
	
	/**
	 * L’Interface Utilisateur invoque cette fonction à la fin de la manche,
	 * pour demander l'affichage de son gagnant. Si la partie est terminée,
	 * il faut plutot afficher le classement final et les scores définitifs.
	 */
	public void end();
}
