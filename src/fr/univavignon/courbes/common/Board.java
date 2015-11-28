package fr.univavignon.courbes.common;

import java.io.Serializable;
import java.util.Map;

import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

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
	
	/** Trainées des snakes sur l'aire de jeu: associe la position d'un pixel à un ID de joueur */
	public Map<Position, Integer> snakesMap;
	/** Tableau contentant tous les snakes de la manche, placés dans l'ordre des ID des joueurs correspondants */
	public Snake snakes[];
	
	/** Position des items sur l'aire de jeu: associe la position d'un item à la valeur de cet item */
	public Map<Position, Item> itemsMap;
	
	void buildSnakeList(int nbSnake)
	{
		for (int i = 1; i <= nbSnake; i++)
		{
			//Position posSpawn = new Position(200+(25*i),200);
			//listSnake.add(new Snake(i, posSpawn));
			//updateSnakePosition()
		}
	}
	
	void spawnSnakeOnBoard(Snake listSnake[])
	{
		
	}
	
	
	/**
	 * @param snake
	 * @param currentPos
	 */
	void updateSnakePosition(Snake snake)
	{
		//Utiliser la speed,direction, et pos  pour faire evoluer la Pos
		//Mettre la old Pos du Snake dans la hash map trac�
		
	}
	
	/**
	 * @param posSnake Position du Snake a tester
	 * @return l'item si le snake est sur un item, sinon null
	 */
	public Item snakeOnItem(Position posSnake)
	{
		return itemsMap.get(posSnake);
	}
	
}
