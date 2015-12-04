package fr.univavignon.courbes.physics.groupe10;

import java.io.Serializable;
import java.util.HashMap;
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
	/** Vitesse de déplacement du snake, exprimée en pixel par ms */
	public double movingSpeed;
	
	/** Vitesse à laquelle le snake change de direction, exprimée en radians par ms */
	public double turningSpeed;
	
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
	
	/** constructeur avec tout les attributs */
	public Snake(int playerId, int profileId, int currentX, int currentY, double currentAngle, double headRadius, double movingSpeed, double turningSpeed, boolean state, boolean collision, boolean inversion, double holeRate, boolean fly, int currentScore )
	{
		this.playerId = playerId;
		this.profileId = profileId;
		this.currentX = currentX;
		this.currentY = currentY;
		this.currentAngle = currentAngle;
		this.headRadius = headRadius;
		this.movingSpeed = movingSpeed;
		this.turningSpeed = turningSpeed;
		this.state = state;
		this.collision = collision;
		this.inversion = inversion;
		this.holeRate = holeRate;
		this.fly = fly;
		this.currentScore = currentScore;
		currentItems = new HashMap<Item, Long>();
	}
	
	/** Un constructeur basique de Snake qui sera  utiliser au début  de la partie*/
	
	public Snake(int playerId, int profileId, int currentX, int currentY, double currentAngle)
	{
		/* VALEUR DEFAUT (fixé arbitrairement pour le moment)
		      headRadius : 10 px
		      currentSpeed : 0.1 px/ms
		*/
		//    playerId, profileId, currentX, currentY, currentAngle, headRadius, movingSpeed, turningSpeed, state, collision, inversion, holeRate, fly, currentScore )
		this(playerId, profileId, currentX, currentY, currentAngle, 10.0        , 0.1         , 0.1, true    , true,   false    , 0.0       , false, 0);

	}
	
	/** Les getters pour les atributs privés */ 
	
	public int getPlayerId()
	{
		return playerId;
	}

	public int getProfileId()
	{
		return profileId;
	}

	public int getCurrentX()
	{
		return currentX;
	}

	public int getCurrentY()
	{
		return currentY;
	}

	public double getCurrentAngle()
	{
		return currentAngle;
	}

	public boolean getState()
	{
		return state;
	}
	
	public boolean getCollision()
	{
		return collision;
	}
	
	public double getHoleRate()
	{
		return holeRate;
	}
	
	public boolean getInversion()
	{
		return inversion;
	}
	
	public boolean getFly()
	{
		return fly;
	}
	
	public int getCurrentScore()
	{
		return currentScore;
	}
	
	
	
}
