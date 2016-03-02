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
{	
	////////////////////////////////////////////////////////////////
	////	DIMENSIONS
	////////////////////////////////////////////////////////////////
	/** Largeur du panel de score */
	public static final int SCORE_WIDTH = 200;//TODO à confirmer
	/** Marge de séparation */
	public static final int WINDOW_MARGIN = 20;
	
	////////////////////////////////////////////////////////////////
	////	DUREES ASSOCIEES AUX ETATS DE LA MANCHE
	////////////////////////////////////////////////////////////////
	/** Durée de la période, au début d'une manche, pendant laquelle les collisions sont désactivées */
	public static final long ENTRANCE_DURATION = 3000;//5000; 	//TODO à confirmer
	/** Durée de la période, au début d'une manche, pendant laquelle les collisions sont désactivées */
	public static final long PRESENTATION_DURATION = 3000; 	//TODO à confirmer
	/** Durée de la période, à la fin d'une manche, pendant laquelle il ne reste qu'un seul joueur en lice */
	public static final long END_DURATION = 3000; 	//TODO à confirmer
	
	////////////////////////////////////////////////////////////////
	////	VITESSE DE DEPLACEMENT
	////////////////////////////////////////////////////////////////
	/** Vitesse de déplacement initiale (i.e. sans l'effet d'un item) des serpents, exprimée en pixels par ms */
	public static final float BASE_MOVING_SPEED = 0.08f;
	/** Coefficient multiplicatif/diviseur appliqué à la vitesse de déplacement pour l'augmenter/la diminuer */
	public static final float MOVING_SPEED_COEFF = 2f;
	
	////////////////////////////////////////////////////////////////
	////	VITESSE DE ROTATION
	////////////////////////////////////////////////////////////////
	/** Vitesse initiale à laquelle le serpent change de direction, exprimée en radians par ms */
	public static final float BASE_TURNING_SPEED = (float)(0.0003*2*Math.PI);
	/** Coefficient multiplicateur/diviseur appliqué à la vitesse de rotation pour l'augmenter/la diminuer */
	public static final float TURNING_COEFF = 1.25f;
	
	////////////////////////////////////////////////////////////////
	////	TAILLE DE LA TÊTE
	////////////////////////////////////////////////////////////////
	/** Rayon initial de la tête d'un serpent, exprimé en pixels */ 
	public static final int BASE_HEAD_RADIUS = 4;
	/** Coefficient multiplicateur appliqué au rayon de la tête d'un serpent pour l'augmenter */
	public static final float HEAD_RADIUS_COEFF = 2;

	////////////////////////////////////////////////////////////////
	////	TROUS DANS LES TRAINES
	////////////////////////////////////////////////////////////////
	/** Taux de création des trous, correspond à la probabilité pour un serpent de laisser un trou dans sa traine à chaque ms */
	public static final float HOLE_RATE = 0.0008f;
	/** Largeur d'un trou pour un serpent d'épaisseur normale, exprimée en pixels */
	public static final int BASE_HOLE_WIDTH = 40;
	/** Coefficient multiplicatif/diviseur appliqué à la largeur d'un trou pour l'augmenter */
	public static final float HOLE_WIDTH_COEFF = 2;
	/** Délai minimal entre deux trous */
	public static final long MIN_HOLE_DELAY = 100; //TODO à confirmer
	
	////////////////////////////////////////////////////////////////
	////	ITEMS
	////////////////////////////////////////////////////////////////
	/** Rayon d'un item, exprimé en pixels */
	public static final int ITEM_RADIUS = 20;
	/** Temps de vie d'un item (en ms), avant qu'il ne disparaisse de l'aire de jeu */
	public static final long ITEM_DURATION = -1; // valeur négative = infini
	/** Probabilité qu'un item apparaisse à chaque ms */
	public static final float BASE_ITEM_POPUP_RATE = 0.0001f; 
	/** Coefficient multiplicateur appliqué à la probabilité qu'un item apparaisse, pour l'augmenter */
	public static final float ITEM_POPUP_COEFF = 1.5f; //TODO faut régler ça et/ou le popup rate
	/** Nombre maximal d'items affichés simultanément à l'écran */
	public static final int MAX_ITEM_NBR = 10;
	
	////////////////////////////////////////////////////////////////
	////	JOUEURS & SERPENTS
	////////////////////////////////////////////////////////////////
	/** Tableau contenant les couleurs associées à chaque numéro de joueur pendant une manche */
	public static final Color[] PLAYER_COLORS = 
	{	new Color(255, 69, 69),	// rouge
		new Color( 68, 68,255),	// bleu
		new Color(255,233, 43),	// jaune
		new Color( 2,209,192),	// turquoise
		new Color(255,136, 52),	// orange
		new Color(255,164,186)	// rose
	};
	/** Nombre maximal de joueurs dans une manche donnée */
	public static final int MAX_PLAYER_NBR = PLAYER_COLORS.length;
	/** Couleur utilisée pendant une manche pour indiquer qu'un joueur n'est plus connecté */
	public static final Color DISCO_PLAYER_COLOR = Color.GRAY;
	/** Couleur utilisée pendant une manche pour tracer la tête d'un joueur éliminé */
	public static final Color ELIM_PLAYER_COLOR = Color.DARK_GRAY;
	/** Épaisseur des auréoles (en pixels) affichées autour de la tête des serpents pour représenter l'effet des items */
	public static final int AUREOLA_THICKNESS = 6;
	/** Espacement entre les auréoles, en pixels */
	public static final int AUREOLA_SPACE = 2;
 
	////////////////////////////////////////////////////////////////
	////	BORDURE DE L'AIRE DE JEU
	////////////////////////////////////////////////////////////////
	/** Épaisseur de la bordure de l'aire de jeu (en pixels) */
	public static final int BORDER_THICKNESS = 5;	
	/** Couleur de la bordure de l'aire de jeu */
	public static final Color BORDER_COLOR = Color.WHITE;
 
	////////////////////////////////////////////////////////////////
	////	POINTS
	////////////////////////////////////////////////////////////////
	/** Points marqués par les joueurs à l'issue d'une manche, en fonction de leur classement */
	public static final Map<Integer,int[]> POINTS_FOR_RANK = new HashMap<Integer,int[]>();
	static
	{	POINTS_FOR_RANK.put(2, new int[] {1,0});
		POINTS_FOR_RANK.put(3, new int[] {2,1,0});
		POINTS_FOR_RANK.put(4, new int[] {3,2,1,0});
		POINTS_FOR_RANK.put(5, new int[] {4,3,2,1,0});
		POINTS_FOR_RANK.put(6, new int[] {5,4,3,2,1,0});
	}
	/** Limite de points à atteindre */
	public static final Map<Integer,Integer> POINT_LIMIT_FOR_PLAYER_NBR = new HashMap<Integer,Integer>();
	static
	{	POINT_LIMIT_FOR_PLAYER_NBR.put(2,10);
		POINT_LIMIT_FOR_PLAYER_NBR.put(3,20);
		POINT_LIMIT_FOR_PLAYER_NBR.put(4,30);
		POINT_LIMIT_FOR_PLAYER_NBR.put(5,40);
		POINT_LIMIT_FOR_PLAYER_NBR.put(6,50);
	}
	
	////////////////////////////////////////////////////////////////
	////	DONNEES RESEAU
	////////////////////////////////////////////////////////////////
	/** Adresse IP par défaut */
	public static final String DEFAULT_IP = "localhost";//60010;//453;
	/** Port TCP par défaut */
	public static final int DEFAULT_PORT = 9999;//60010;//453;
	/** Liste des préfixes d'adresses IP acceptées par le Moteur Réseau */
	public static final String[] IP_PREFIXES = {"192.168.","10.104.","194.57."};
}
