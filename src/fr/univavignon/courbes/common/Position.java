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
 * Cette classe permet de représenter la position d'un
 * pixel dans un repère à deux dimensions.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Position implements Serializable, Comparable<Position>
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;

	/**
	 * Instancie une nouvelle position avec les
	 * valeurs passées en paramètres.
	 * 
	 * @param x
	 * 		Position en abscisse.
	 * @param y
	 * 		Position en ordonnée.
	 */
	
	public Position(int x, int y)
	{	this.x = x;
		this.y = y;
	}
	
	/**
	 * Instancie une nouvelle position en
	 * recopiant celle passée en paramètre.
	 * 
	 * @param position
	 * 		Position à recopier.
	 */
	
	public Position(Position position)
	{	this.x = position.x;
		this.y = position.y;
	}
	
	/**
	 * Empty constructor used by Kryonet network
	 */
	public Position(){}
	
	////////////////////////////////////////////////////////////////
	////	COORDONNEES
	////////////////////////////////////////////////////////////////
	/** Position sur l'axe des abscisses */
	public int x;
	/** Position sur l'axe des ordonnées */
	public int y;
	
	////////////////////////////////////////////////////////////////
	////	COMPARAISON
	////////////////////////////////////////////////////////////////
	@Override
	public int compareTo(Position position)
	{	int result = x - position.x;
		if(result==0)
			result = y - position.y;
		return result;
	}
	
	@Override
	public int hashCode()
	{	final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{	boolean result = false;
		if(this==obj)
			result = true;
		else if(obj!=null && obj instanceof Position)
		{	Position position = (Position) obj;
			result = compareTo(position)==0;
		}
		return result;
	}
	
	////////////////////////////////////////////////////////////////
	////	TEXTE
	////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "(" + x + ";" + y + ")";
		return result;
	}
}
