package fr.univavignon.courbes.common;

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

import java.io.Serializable;

/**
 * Cette classe correspond à l'ensemble des informations propres à 
 * une manche en cours de déroulement. Elle comporte l'aire de jeu
 * (classe {@link Board}) et la liste des joueurs participants (classe {@link Player}).
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Round implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructeur nécessaire pour le mode réseau utilisant
	 * Kryonet. Ne pas utiliser ce constructeur.
	 */
	public Round()
	{
		// rien à faire
	}
	
	/**
	 * Initialise une manche avec les dimensions indiquées
	 * pour l'aire de jeu (qui sera crée ultérieurement).
	 * 
	 * @param boardWidth
	 * 		Largeur de l'aire de jeu (en pixels).
	 * @param boardHeight
	 * 		Hauteur de l'aire de jeu (en pixels).
	 */
	public Round(int boardWidth, int boardHeight)
	{	this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
	}
	
	/** Aire de jeu de la manche courante */
	public Board board;
	/** Largeur de l'aire de jeu à créer */
	public int boardWidth;
	/** Hauteur de l'aire de jeu à créer */
	public int boardHeight;
	
	/** Tableau des joueurs, dans l'ordre de leur {@code playerId} */
	public Player[] players;
	
	/** Limite de points courante pour la manche actuelle */
	public int pointLimit;
}
