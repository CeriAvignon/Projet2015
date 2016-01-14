package fr.univavignon.courbes.common;

import java.io.Serializable;
import java.util.Map;

/**
<<<<<<< HEAD
 * Cette classe contient les informations et caractéristiques d'un serpent.
 * <br/>
 * L'ID du joueur associé au serpent est relatif à la partie courante. Par 
=======
 * Cette classe contient les informations et caractéristiques d'un snake.
 * <br/>
 * L'ID du joueur associé au snake est relatif à la partie courante. Par 
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
 * exemple, si on a 4 joueurs, alors leurs ID vont de 0 à 3. À ne pas confondre
 * avec l'ID du profile, qui est valable pour le jeu globalement, et indépendant
 * de toute partie.
 */
public class Snake implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
<<<<<<< HEAD
	/** Numéro du joueur associé à ce serpent, dans la partie en cours */
	public int playerId;
	/** Numéro du profil associé à ce serpent, dans la BD du jeu */
	public int profileId;
	
	/** Position courante de la tête du serpent en abscisse */
	public int currentX;
	/** Position courante de la tête du serpent en ordonnée */
	public int currentY;
	
	/** Angle représentant la direction de déplacement courante du serpent, par rapport à l'horizontale */
	public double currentAngle;
	/** Rayon de la tête du serpent */
	public double headRadius;
	/** Vitesse de déplacement du serpent, exprimée en pixel par ms */
	public double movingSpeed;
	/** Vitesse à laquelle le serpent change de direction, exprimée en radians par ms */
	public double turningSpeed;
	
	/** Etat du serpent : {@code false} il est mort, {@code true} il est vivant */
	public boolean state;
	/** Mode de collision : {@code false} il est insensible aux collisions, {@code true} il l’est */
	public boolean collision;
	/**Inversion des commandes : {@code false} le serpent est dirigé normalement, {@code true} ses commandes sont inversées */
	public boolean inversion;
	
	/** Taux de création de trous dans le tracé : pour 0 on n'a aucun trou, pour 1 on n'a aucune traînée (i.e. que des trous). Ce taux est la probabilité qu'un serpent laisse un trou lors d'une itération donnée. */
	public double holeRate;
	/** Taille du trou restant à "effectuer", ou zéro si aucun trou n'est en cours de réalisation, exprimée en pixels */
	public int remainingHoleWidth;
=======
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
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
	
	/** Mode avion ({@code true}) ou pas ({@code false}) */
	public boolean fly;
	
<<<<<<< HEAD
	/** Items affectant ce serpent, associés à leur temps d'effet restant (en ms) */
	public Map<Item, Long> currentItems;
	
	/** Score courant du joueur associé au serpent dans la partie courante */
=======
	/** Items affectant ce snake, associés à leur temps d'effet restant (en ms) */
	public Map<Item, Long> currentItems;
	
	/** Score courant du joueur associé au snake dans la partie courante */
>>>>>>> 005e8598e797176e5596a6d6e9b00f6a7466cf81
	public int currentScore;
}
