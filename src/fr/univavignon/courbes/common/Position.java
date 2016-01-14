package fr.univavignon.courbes.common;

import java.io.Serializable;

/**
 * Cette classe permet de représenter la position d'un
 * pixel dans un repère à deux dimensions.
 */
<<<<<<< HEAD
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
=======
public class Position implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
	
	/** Position sur l'axe des abscisses */
	public int x;
	/** Position sur l'axe des ordonnées */
	public int y;
<<<<<<< HEAD
	
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
=======
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
}
