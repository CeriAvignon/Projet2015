<<<<<<< HEAD
package fr.univavignon.courbes.common;

import java.io.Serializable;

/**
 * Type énuméré représentant les différents types
 * d'items présents dans le jeu. Chacun possède un
 * champ représentant la durée de sont effet exprimée 
 * en ms.
 * <br/>
 * <b>Remarque :</b> les durées sont arbitrairement fixées à 
 * 3 secondes, il faudra les estimer plus précisément
 * en observant le jeu. TODO
 */
public enum Item implements Serializable
{	/** Le joueur qui ramasse l'item accélère (bonus) */
	USER_SPEED(3000l),
	/** Le joueur qui ramasse l'item ralentit (bonus) */
	USER_SLOW(3000l),
	/** Le joueur qui ramasse l'item laisse de plus gros trous dans sa trainée (bonus) */
	USER_BIG_HOLE(3000l),
	
	/** Les autres joueurs accélèrent (malus) */
	OTHERS_SPEED(3000l),
	/** Les autres joueurs laissent des trainées plus épaisses (malus) */
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
=======
package fr.univavignon.courbes;

abstract class Item 
{
	protected Position apparition;
}

abstract class IndividualEffectItem extends Item
{
	// ????
}

public class BonusItem extends IndividualEffectItem
{
	private boolean earth=false;
	private boolean sonicSlow=false;
	private boolean sonicFast=false;
	
	
	
}

public class MalusItem extends IndividualEffectItem
{
	private boolean feed=false;
	private boolean invert=false;
	private boolean hole=false;
	private boolean passkey=false;
	private boolean sonicSlow=false;
	private boolean sonicFast=false;
}




public class GeneralEffectItem extends Item
{
	private boolean clean=false;
	private boolean earth=false;
	private boolean moreloops=false;
}




>>>>>>> 7c32b7e412d00ee50a38af320ead8d73b18e127f
