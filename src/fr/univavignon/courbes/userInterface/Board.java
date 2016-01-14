package fr.univavignon.courbes.userInterface;

import java.io.Serializable;
import java.util.Map;

import javax.swing.text.Position;

/**
 * Cette classe correspond à l'ensemble des informations propres à 
 * l'aire de jeu utilisée pendant une manche.
 * <br/>
 * Il faut bien distinguer la notion de partie et de manche. Les joueurs
 * sont confrontés lors d'une parties se déroulant sur plusieurs manches
 * distinctes. À chaque, chaque joueur marque un certain nombre de points.
 * Un joueur gagne la partie quand son score dépasse une certaine valeur
 * limite. 
 */
public class Board implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/** Largeur de l'aire de jeu, en pixels */
	public int width;
	/** Hauteur de l'aire de jeu, en pixels */
	public int height;
	
	/** Trainées des serpents sur l'aire de jeu: associe la position d'un pixel à un ID de joueur */
	public Map<Position, Integer> snakesMap;
	/** Tableau contenant tous les serpents de la manche, placés dans l'ordre des ID des joueurs correspondants */
	public Snake snakes[];
	
	/** Position des items sur l'aire de jeu: associe la position du <i>centre</i> d'un item à la valeur de cet item */
	public Map<Position, Item> itemsMap;
}