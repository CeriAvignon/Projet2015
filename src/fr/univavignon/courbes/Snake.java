<<<<<<< HEAD
package fr.univavignon.courbes.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Cette classe contient les informations et caractéristiques d'un snake.
 * <br/>
 * L'ID du joueur associé au snake est relatif à la partie courante. Par 
 * exemple, si on a 4 joueurs, alors leurs ID vont de 0 à 3. À ne pas confondre
 * avec l'ID du profile, qui est valable pour le jeu globalement, et indépendant
 * de toute partie.
 */
public class Snake implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	/** Numéro du joueur associé à ce snake, dans la partie en cours */
	public int playerId;
	/** Numéro du profil associé à ce snake, dans la BD du jeu */
	public int profileId;
	
	/** Position courante de la tête du Snake en abscisse */
	public int currentX;
	/** Position courante de la tête du Snake en ordonnée */
	public int currentY;
	
	/** Angle représentant la direction de déplacement courante du snake */
	public double currentAngle;
	/** Rayon de la tête du snake */
	public double headRadius;
	/** Vitesse du snake, exprimée en pixel par ms */
	public double currentSpeed;
	
	/** Etat du snake : {@code false} il est mort, {@code true} il est vivant */
	public boolean state;
	/** Mode de collision : {@code false} il est insensible aux collisions, {@code true} il l’est */
	public boolean collision;
	/**Inversion des commandes : {@code false} le snake est dirigé normalement, {@code true} ses commandes sont inversées */
	public boolean inversion;
	
	/** Taux de création de trous dans le tracé : pour 0 on n'a aucun trou, pour 1 on n'a aucune traînée (i.e. que des trous) */
	public double holeRate;
	
	/** Mode avion ({@code true}) ou pas ({@code false}) */
	public boolean fly;
	
	/** Items affectant ce snake, associés à leur temps d'effet restant (en ms) */
	public Map<Item, Long> currentItems;
	
	/** Score courant du joueur associé au snake dans la partie courante */
	public int currentScore;
}
=======
package fr.univavignon.courbes;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Shift + ctrl + o     to import automaticly 
public class Snake 
{
	private int idPlayer;
	private Position head; 			//Position = (x,y)
	private double direction; 		//angle
	private double ray;				//Size
	private double speed;
	private boolean state;			// 0 = dead 	   ----- 1 = alive
	private boolean crashMode;		// 0 = nocollision ----- 1 = collision   
	private boolean invertMode; 	// 0 = normal keys ----- 1 = invert keys
	private double holeRate;		// % hole apparition
	private boolean planeMode; 		// 0 = not plane   ----- 1 = plane mode
	private HashMap<Item,double> changes;     		// List of Items

	
	public Snake(int id)
	{
		idPlayer 	= id;
		head 		= new Position();
		direction   = -90.0; 		// vers le haut
		ray 		= 3.0;  		// pixel ?
		speed 		= 1.0;			// pixel/frame ?
		state 		= true;
		crashMode 	= true;
		invertMode  = false;
		holeRate 	= 5.0;			// 5% ?
		planeMode  = false;
		changes = new HashMap<Item,double>(50);
	}
	
	/***************************************************************************************************************************************************************\
	/																																							   *\
	/																		GETTERS & SETTERS																	   *\
	/																																							   *\
	/***************************************************************************************************************************************************************/
	public int getID()
	{
		return idPlayer;
	}
	
	public boolean isAlive()
	{
		return state;
	}
	
	public boolean iscrashMode()
	{
		return crashMode;
	}
	
	public boolean isInversed()
	{
		return invertMode;
	}
	
	public boolean isFlying()
	{
		return planeMode;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double rateHoles()
	{
		return holeRate;
	}
	
	public double getSize()
	{
		return ray;
	}
	
	public Position getPosition()
	{
		return head;
	}
	
	/***************************************************************************************************************************************************************\
	/																																							   *\
	/																		ACTIONS																				   *\
	/																																							   *\
	/***************************************************************************************************************************************************************/

	
	public void turnLeft()
	{
		direction--;
	}
	
	public void turnRight()
	{
		direction++;
	}
	
	public void newChange(Changes object)
	{
		// a voir plus tard
	}
	
	public void getChanges()
	{
		// a voir plus tard
	}
}


	
//double axeX1=cos(angle1*PI/180.0);
//double axeY1=sin(angle1*PI/180.0);
>>>>>>> 7c32b7e412d00ee50a38af320ead8d73b18e127f
