package fr.univavignon.courbes.common;

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

}
