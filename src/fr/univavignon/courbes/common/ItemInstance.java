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
 * Cette classe permet de représenter une instance d'item,
 * caractérisée par une position, un type et une durée restante.
 * Pour un item encore en jeu, cette durée exprimée en ms correspond 
 * au temps restant avant que l'item ne disparaisse. Pour un item
 * qui a été ramassé, elle représente le temps restant avant que l'item
 * ne cesse de faire effet sur le joueur qui l'a ramassé. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public abstract class ItemInstance implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////
	////	POSITION
	////////////////////////////////////////////////////////////////
	/** Position sur l'axe des abscisses */
	public int x;
	/** Position sur l'axe des ordonnées */
	public int y;
	
	////////////////////////////////////////////////////////////////
	////	AUTRES
	////////////////////////////////////////////////////////////////
	/** Type d'item */
	public ItemType type;
	/** Durée restante (en ms) : soit d'affichage si l'item est en jeu, soit d'effet si l'item a été ramassé */
	public long remainingTime;
}
