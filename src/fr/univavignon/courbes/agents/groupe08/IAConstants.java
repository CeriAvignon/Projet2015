package fr.univavignon.courbes.agents.groupe08;

/**
 * Class contenant les constantes que l'IA utilies dans son backtracking
 *
 * @author Sabri
 * @author Charlie
 * @author Alexandre
 */
public class IAConstants {
	
	//---------------------------------------------------
	//----- ARBRE DE RECHERCHE --------------------------
	//---------------------------------------------------
	
	//DEFENSIF
	/**	la profondeur de la recherche **/
	public static final int PROFONDEUR = 3;
	/** la durée d'un pas dans le BT **/
	public static final long PETIT_PAS_DUREE = 30;//(long) AbstractRoundPanel.PHYS_DELAY;
	/** le nombre de pas dans le BT **/
	public static final int NB_PETIT_PAS = 10;
	/** valeur à partir de laquelle on considére un changement de poids critique **/
	public static final int CHANGEMENT_CRITIQUE = 2000;
	
	//---------------------------------------------------
	//--------- POIDS --------- -------------------------
	//---------------------------------------------------
	/** le poids si l'IA meurt dans le BT **/
	public static final int MORT_IA = -1000;
	/** le poids si l'ennemi meurt dans le BT **/
	public static final int MORT_ENNEMI = 0;	
	public static final int BRANCHE = 0;
	//---------------------------------------------------
	//--------- POIDS DES ITEMS -------------------------
	//---------------------------------------------------
	
	/** Le joueur qui ramasse l'item accélère (bonus) */
	public static final int USER_FAST = -500;
	/** Le joueur qui ramasse l'item ralentit (bonus) */
	public static final int USER_SLOW = 2000;
	/** Le joueur qui ramasse l'item vole au dessus des obstacles (bonus) */
	public static final int USER_FLY = 2000;
	////	EFFET SUR LES AUTRES JOUEURS
	/** Les autres joueurs accélèrent (malus) */
	public static final int OTHERS_FAST = 2000;
	/** Les autres joueurs laissent des trainées plus épaisses (malus) */
	public static final int OTHERS_THICK = 2000;
	/** Les autres joueurs ralentissent (malus) */
	public static final int OTHERS_SLOW = 2000;
	/** Les commandes des autres joueurs sont inversées (malus) */
	public static final int OTHERS_REVERSE = 2000;
	////	EFFET SUR TOUS
	/** La probabilité d'apparition d'un item augmente */
	public static final int COLLECTIVE_WEALTH = 500;
	/** Tous les joueurs peuvent traverser les murs d'enceinte */
	public static final int COLLECTIVE_TRAVERSE = 1000;
	/** L'aire de jeu est réinitialisée (les trainées existantes sont effacées) */
	public static final int COLLECTIVE_CLEAN = 2000;
}
