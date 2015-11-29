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


	/**
	 * @param width Largeur du plateau
	 * @param height Hauteur du plateau
	 * @param nbSnakes Nombre de snakes généré dans le plateau
	 */
	public Board(int width, int height, int nbSnakes) {
		
		snakes = new Snake[nbSnakes];
		Position posSpawn;

		for (int i = 0; i < nbSnakes ; i++) 
		{
			posSpawn = generateSpawnPos(width, height);
			snakes[i] = new Snake(i, posSpawn);
			System.out.println("Snake " + Integer.toString(i) + " spawn a la position " + Integer.toString(posSpawn.x) + " "+Integer.toString(posSpawn.y));
		}
	}




	/**
	 * Génére une position aléatoire sur la plateau, la fonction générera une position qui n'est pas
	 * trop rapproché des bords du plateau ou trop proche et verifiera qu'elle n'est pas trop proche
	 * d'un autre snake.
	 *
	 * @param widthBoard Largeur du plateau
	 * @param heightBoard Hauteur du plateau
	 * @return La position généré aléatoirement
	 */
	private Position generateSpawnPos(int widthBoard, int heightBoard) {

		Boolean flagPos;
		Position posSpawn = new Position();

		do {
			posSpawn.x = 20 + (int)(Math.random() * heightBoard - 20); 
			posSpawn.y = 20 + (int)(Math.random() * widthBoard - 20); 
			flagPos = true;

			for(int i = 0; i < snakes.length ; i++)// Teste de la proximité avec un autre snake
			{
				if(snakes[i] != null)
				{
					if(Math.abs(posSpawn.x - snakes[i].currentX) +  Math.abs(posSpawn.y - snakes[i].currentY) < 40)
					{
						flagPos = false; // Proximité détécté, on cherche alors une nouvelle position
					}
				}
			}
		}while(!flagPos);

		return posSpawn;
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
