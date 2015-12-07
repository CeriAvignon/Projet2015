package fr.univavignon.courbes.physics.groupe10;

import java.io.Serializable;

/**
 * Cette classe permet de représenter la position d'un
 * pixel dans un repère à deux dimensions.
 */
public class Position implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/** Position sur l'axe des abscisses */
	public int x;
	/** Position sur l'axe des ordonnées */
	public int y;
	
	public Position(int x, int y)
	{
		this.x = x; 
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public Position getPosition()
	{
		Position pos = new Position(this.x,this.y);
		return pos;
	}

}