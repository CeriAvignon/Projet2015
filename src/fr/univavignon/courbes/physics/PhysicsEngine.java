package fr.univavignon.courbes.physics;

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

import java.util.List;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur de 
 * demander au Moteur Physique d'effectuer différentes sortes de mises 
 * à jour.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public interface PhysicsEngine
{	
	/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * au début de chaque manche.
	 * <br/>
	 * Le Moteur Physique doit s'initialiser, et instancier un objet
	 * de type {@link Board} représentant l'aire de jeu de la manche
	 * qui va se dérouler ensuite. Il doit pour cela utiliser les
	 * valeurs passées en paramètres, puis renvoyer cet objet pour 
	 * que l'Interface Utilisateur puisse l'utiliser à son tour.
	 * 
	 * @param playerNbr
	 * 		Nombre de joueurs participants à la manche.
	 */
	public void init(int playerNbr);
	
	/**
	 * Méthode permettant d'initialiser un plateau de jeu destiné à tester
	 * l'intégration entre Moteur Graphique et Moteur Physique. Ce plateau
	 * doit respecter les contraintes suivantes :
	 * <ul>
	 * 	<li>Afficher tous les items existants</li>
	 * 	<li>Seulement deux joueurs : le premier est contrôlé normalement, le second est immobile</li>
	 * </ul>
	 */
	public void initDemo();
	
	/**
	 * Renvoie l'aire de jeu créée lors de l'initialisation de
	 * ce Moteur Physique.
	 *  
	 * @return
	 * 		Un objet représentant l'aire de jeu de la manche.
	 */
	public Board getBoard();
	
	/**
	 * Renvoie une copie de l'aire de jeu.
	 *  
	 * @return
	 * 		Copie de l'aire de jeu de la manche.
	 */
	public Board getBoardCopy();
	
	/**
	 * Initialise l'aire de jeu d'un Moteur Physique situé
	 * côté client, et qui doit donc s'aligner sur l'aire de
	 * jeu du serveur.
	 * 
	 * @param board
	 * 		Aire de jeu du serveur.
	 */
	public void setBoard(Board board);
	
	/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * à chaque itération d'une manche.
	 * <br/>
	 * Elle réalise la mise à jour des données physiques, en prenant en compte 
	 * le temps écoulé depuis la dernière mise à jour. Le Moteur Physique
	 * doit pour cela modifier l'objet de type {@link Board} représentant
	 * l'aire de jeu, qui avait été initialisé par {@link #init}
	 * (ou par {@link #forceUpdate(Board)}.
	 * <br/>
	 * Le paramètre {@code commands} représente les dernières commandes générées
	 * par les joueurs. La map associe un ID de joueur à sa commande.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour, exprimé en ms.
	 * @param commands
	 * 		Dernière commande générée par le chaque joueur.
	 */
	public void update(long elapsedTime, Direction commands[]);
	
	/**
	 * Renvoie la liste des numéros des joueurs éliminés lors de la dernière
	 * itéation  (dans l'ordre d'élimination).
	 * 
	 * @return
	 * 		Numéros des joueurs éliminés lors de cette itération (dans l'ordre d'élimination).
	 */
	public List<Integer> getEliminatedPlayers();
	
	/**
	 * Cette méthode est appelée par l'Interface Utilisateur côté client, lors 
	 * d'un jeu en réseau.
	 * <br/> 
	 * Elle remplace la manche courante par celle passée en paramètre. Elle 
	 * permet à l'Interface Utilisateur située côté client de remplacer l'objet 
	 * {@link Board} représentant la manche courante, par un nouvel objet envoyé 
	 * par le serveur, et représentant une version plus récente de l'aire de jeu.
	 * Autrement dit, dans le cas du jeu en réseau côté client, le Moteur
	 * Physique ne fait pas de calcul : il se contente de mettre à jour
	 * l'aire de jeu avec les informations qu'il reçoit. 
	 * <br/>
	 * Le remplacement doit se faire de manière à ne pas induire de modification 
	 * dans le traitement effectué par les autres composantes.
	 * 
	 * @param board
	 * 		Nouvelle aire de jeu, devant remplacer l'aire de jeu courante.
	 */
	public void forceUpdate(Board board);
}
