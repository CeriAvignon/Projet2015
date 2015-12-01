package fr.univavignon.courbes.common;

import java.io.Serializable;

/**
 * Cette classe représente les commandes possibles qu'un utilisateur
 * peut générer : tourner à gauche, tourner à droite, ou ne rien faire
 * du tout.
 */
public enum Direction implements Serializable
{	/** L'utilisateur veut tourner à gauche */
	LEFT,
	
	/** L'utilisateur veut tourner à droite */
	RIGHT,
	
	/** L'utilisateur ne change pas de direction */
	NONE;
}