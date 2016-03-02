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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import fr.univavignon.courbes.common.Board.State;

/**
 * Classe spécifiquement conçue pour représenter les données du jeu 
 * transmises à chaque itération. On ne transmet pas l'aire de jeu entière
 * afin d'optimiser les échanges : on se consacre sur ce qui change d'une 
 * itération à l'autre.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class SmallUpdate implements UpdateInterface
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialise les tableaux de cet objet.
	 * 
	 * @param playerNbr
	 * 		Nombre de joueurs à traiter.
	 */
	public SmallUpdate(int playerNbr)
	{	newItem = null;
		clearedTrail = new boolean[playerNbr];
		posX = new float[playerNbr];
		posY = new float[playerNbr];
		headRadiuses = new int[playerNbr];
		eliminatedBy = new Integer[playerNbr];
		connected = new boolean[playerNbr];
		fly = new boolean[playerNbr];
		newTrails = new ArrayList<TreeSet<Position>>();
		currentItems = new ArrayList<ItemInstance[]>();
	}
	
	/**
	 * Empty constructor user by Kryonet network
	 */
	public SmallUpdate(){}
	
	////////////////////////////////////////////////////////////////
	////	AIRE DE JEU
	////////////////////////////////////////////////////////////////
	/** Indique la phase du jeu : présentation, entrée ou normal */
	public State state;
	/** Indique si l'aire de jeu contient actuellement une bordure ou si celle-ci est absente */
	public boolean hasBorder;
	
	////////////////////////////////////////////////////////////////
	////	ITEMS
	////////////////////////////////////////////////////////////////
	/** Numéros des items supprimés de l'aire de jeu */
	public int[] removedItems;
	/** Items apparus dans l'aire de jeu */
	public ItemInstance newItem;
	
	////////////////////////////////////////////////////////////////
	////	SERPENTS
	////////////////////////////////////////////////////////////////
	/** Numéros des joueurs éliminés lors de la dernière itération */ 
	public int[] lastEliminated;
	/** Marqueur indiquant la traînée du serpent doit être supprimée */
	public boolean[] clearedTrail;
	/** Position en abscisse de chaque serpent */
	public float[] posX;
	/** Position en ordonnée de chaque serpent */
	public float[] posY;
	/** Rayon de la tête de chaque serpent */
	public int[] headRadiuses;
	/** Etat de vie de chaque serpent */
	public Integer[] eliminatedBy;
	/** Etat de connexion de chaque serpent  */
	public boolean[] connected;
	/** Mode avion  */
	public boolean[] fly;
	/** Dernière section de la trainée */
	public List<TreeSet<Position>> newTrails;
	/** Items affectant actuellement chaque serpent */
	public List<ItemInstance[]> currentItems;
}
