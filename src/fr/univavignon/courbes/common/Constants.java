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
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Constants
{	/** Largeur de l'aire de jeu standard */
	public static final int BOARD_WIDTH = 800;//TODO à confirmer
	/** Hauteur de l'aire de jeu standard */
	public static final int BOARD_HEIGHT = 800;//TODO à confirmer
	
	/** Durée de la période, au début d'une manche, pendant laquelle les collisions sont désactivées */
	public static final long ENTRANCE_DURATION = 0;//5000; //TODO à confirmer
	
	/** Vitesse de déplacement initiale (i.e. sans l'effet d'un item) des serpents, exprimée en pixels par ms */
	public static final float BASE_MOVING_SPEED = 0.1f;
	/** Coefficient multiplicatif/diviseur appliqué à la vitesse de déplacement pour l'augmenter/la diminuer */
	public static final float MOVING_SPEED_COEFF = 3f;
	
	/** Vitesse initiale à laquelle le serpent change de direction, exprimée en radians par ms */
	public static final float BASE_TURNING_SPEED = (float)(0.0005*2*Math.PI);
	/** Coefficient multiplicateur/diviseur appliqué à la vitesse de rotation pour l'augmenter/la diminuer */
	public static final float TURNING_COEFF = 3f;
	
	/** Rayon initial de la tête d'un serpent, exprimé en pixels */ 
	public static final int BASE_HEAD_RADIUS = 4;
	/** Coefficient multiplicateur appliqué au rayon de la tête d'un serpent pour l'augmenter */
	public static final float HEAD_RADIUS_COEFF = 2;

	/** Taux de création des trous, correspond à la probabilité pour un serpent de laisser un trou dans sa traine à chaque ms */
	public static final float HOLE_RATE = 0.0008f;
	/** Largeur d'un trou pour un serpent d'épaisseur normale, exprimée en pixels */
	public static final int BASE_HOLE_WIDTH = 20;
	/** Coefficient multiplicatif/diviseur appliqué à la largeur d'un trou pour l'augmenter */
	public static final float HOLE_WIDTH_COEFF = 2;
	/** Délai minimal entre deux trous */
	public static final long MIN_HOLE_DELAY = 100; //TODO à confirmer
	
	/** Rayon d'un item, exprimé en pixels */
	public static final int ITEM_RADIUS = 20;
	/** Temps de vie d'un item (en ms), avant qu'il ne disparaisse de l'aire de jeu */
	public static final long ITEM_DURATION = 6000;
	/** Probabilité qu'un item apparaisse à chaque ms */
	public static final float BASE_ITEM_POPUP_RATE = 0.00005f;
	/** Coefficient multiplicateur appliqué à la probabilité qu'un item apparaisse, pour l'augmenter */
	public static final float ITEM_POPUP_COEFF = 5f;
	
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
    public static final Color DISC_PLAYER_COLOR = Color.GRAY;
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
