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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe contient uniquement les constantes du jeu, dont les valeurs
 * sont imposées à toutes les implémentations des composantes.
 * <br/>
 * N'hésitez pas à faire une demande sur le forum si vous estimez que certaines
 * valeurs devraient figurer ici.
 */
public class Constants
{	
	/** Vitesse de déplacement normale (i.e. sans l'effet d'un item) des serpents, exprimée en pixels par ms */
	public static final float REGULAR_MOVING_SPEED = 0.1f;
	/** Vitesse de déplacement rapide des serpents, exprimée en pixels par ms */
	public static final float FAST_MOVING_SPEED = 0.3f;
	/** Vitesse de déplacement lente des serpents, exprimée en pixels par ms */
	public static final float SLOW_MOVING_SPEED = 0.1f; //TODO valeur à estimer
	
	/** Vitesse normale à laquelle le serpent change de direction, exprimée en radians par ms */
	public static final float REGULAR_TURNING_SPEED = 0.003f;
	/** Vitesse élevée à laquelle le serpent change de direction, exprimée en radians par ms */
	public static final float FAST_TURNING_SPEED = 0.003f; //TODO valeur à estimer
	/** Vitesse lente à laquelle le serpent change de direction, exprimée en radians par ms */
	public static final float SLOW_TURNING_SPEED = 0.003f; //TODO valeur à estimer
	
	/** Rayon de la tête d'un serpent d'épaisseur normale, exprimé en pixels */ 
	public static final int REGULAR_HEAD_RADIUS = 4;
	/** Rayon de la tête d'un serpent plus épais que la normale, exprimé en pixels */ 
	public static final int LARGE_HEAD_RADIUS = 8;

	/** Taux de création des trous, correspond à la probabilité pour un serpent de laisser un trou dans sa traine lors d'une itération donnée */
	public static final float HOLE_RATE = 0.0085f;
	/** Largeur d'un trou pour un serpent d'épaisseur normale, exprimée en pixels */
	public static final int REGULAR_HOLE_WIDTH = 20;
	/** Largeur d'un trou pour un serpent plus épais que la normale, exprimée en pixels */
	public static final int LARGE_HOLE_WIDTH = 40;
	
	/** Taille d'un item, exprimée en pixels */
	public static final int ITEM_SIZE = 40;
	/** Temps de vie d'un item (en ms), avant qu'il ne disparaisse de l'aire de jeu */
	public static final long ITEM_DURATION = 6000;
	
	/** Nombre maximal de joueurs dans une manche donnée */
	public static final int MAX_PLAYER_NBR = 6;
    /** Tableau contenant les couleurs associées à chaque numéro de joueur pendant une manche */
    public static final Color[] PLAYER_COLORS = 
	{	Color.red,		// joueur 0
    	Color.blue,		// joueur 1
    	Color.green,	// joueur 2
    	Color.cyan,		// joueur 3
    	Color.orange,	// joueur 4
    	Color.pink		// joueur 5
	};
    /** Couleur utilisée pendant une manche pour indiquer qu'un joueur n'est plus connecté */
    public static final Color DISCONNECTED_PLAYER_COLOR = Color.GRAY;
    /** Épaisseur des auréoles (en pixels) affichées autour de la tête des serpents pour représenter l'effet des items */
    public static final int AUREOLA_THICKNESS = 6;
    /** Espacement entre les auréoles, en pixels */
    public static final int AUREOLA_SPACE = 2;
    
    /** Épaisseur de la bordure de l'aire de jeu (en pixels) */
    public static final int BORDER_THICKNESS = 5;
    /** Couleur de la bordure de l'aire de jeu */
    public static final Color BORDER_COLOR = Color.WHITE;
    
    /** Points marqués par les joueurs à l'issue d'une manche, en fonction de leur classement */
    public static final Map<Integer,Integer> POINTS_FOR_RANK = new HashMap<Integer,Integer>();
    static
    {	POINTS_FOR_RANK.put(1,5); //TODO points à confirmer sur le jeu original
    	POINTS_FOR_RANK.put(2,4);
    	POINTS_FOR_RANK.put(3,3);
    	POINTS_FOR_RANK.put(4,2);
    	POINTS_FOR_RANK.put(5,1);
    	POINTS_FOR_RANK.put(6,0);
    }
    
    // TODO en fait, l'effet des items est cumulatif: par ex, si on prend plusieurs grossisseurs, on grossit plusieurs fois.
}
