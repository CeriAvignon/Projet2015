package fr.univavignon.courbes.common;

import java.io.Serializable;

/**
 * Type énuméré représentant les différents types
 * d'items présents dans le jeu. Chacun possède un
 * champ représentant la durée de sont effet exprimée 
 * en ms.
<<<<<<< HEAD
=======
 * <br/>
 * <b>Remarque :</b> les durées sont arbitrairement fixées à 
 * 3 secondes, il faudra les estimer plus précisément
 * en observant le jeu. TODO
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
 */
public enum Item implements Serializable
{	/** Le joueur qui ramasse l'item accélère (bonus) */
	USER_SPEED(3000l),
	/** Le joueur qui ramasse l'item ralentit (bonus) */
<<<<<<< HEAD
	USER_SLOW(10000l),
	/** Le joueur qui ramasse l'item laisse de plus gros trous dans sa trainée (bonus) */
	USER_BIG_HOLE(6000l),
=======
	USER_SLOW(3000l),
	/** Le joueur qui ramasse l'item laisse de plus gros trous dans sa trainée (bonus) */
	USER_BIG_HOLE(3000l),
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
	
	/** Les autres joueurs accélèrent (malus) */
	OTHERS_SPEED(3000l),
	/** Les autres joueurs laissent des trainées plus épaisses (malus) */
<<<<<<< HEAD
	OTHERS_THICK(7000l),
	/** Les autres joueurs ralentissent (malus) */
	OTHERS_SLOW(5000l),
	/** Les commandes des autres joueurs sont inversées (malus) */
	OTHERS_REVERSE(5000l),
	
	/** La probabilité d'apparition d'un item augmente */
	COLLECTIVE_THREE_CIRCLES(60000l),
	/** Tous les joueurs peuvent traverser les murs d'enceinte */
	COLLECTIVE_TRAVERSE_WALL(10000l),
	/** L'aire de jeu est réinitialisée (les trainées existantes sont effacées) */
	COLLECTIVE_ERASER(1l);
=======
	OTHERS_THICK(3000l),
	/** Les autres joueurs ralentissent (malus) */
	OTHERS_SLOW(3000l),
	/** Les commandes des autres joueurs sont inversées (malus) */
	OTHERS_REVERSE(3000l),
	
	/** La probabilité d'apparition d'un item augmente */
	COLLECTIVE_THREE_CIRCLES(3000l),
	/** Tous les joueurs peuvent traverser les murs d'enceinte */
	COLLECTIVE_TRAVERSE_WALL(3000l),
	/** L'aire de jeu est réinitialisée (les trainées existantes sont effacées) */
	COLLECTIVE_ERASER(3000l);
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
	
	/**
	 * Intialise un type d'item avec la durée appropriée.
	 * 
	 * @param duration
	 * 		Durée de l'effet de l'item, en ms.
	 */
	Item(long duration)
	{	this.duration = duration;
	}

	/** Durée associée à l'effet de l'item, exprimée en ms */
	public double duration;
}
