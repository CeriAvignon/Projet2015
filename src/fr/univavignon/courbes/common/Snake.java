package fr.univavignon.courbes.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Cette classe contient les informations et caractéristiques d'un serpent.
 * <br/>
 * L'ID du joueur associé au serpent est relatif à la partie courante. Par 
 * exemple, si on a 4 joueurs, alors leurs ID vont de 0 à 3. À ne pas confondre
 * avec l'ID du profile, qui est valable pour le jeu globalement, et indépendant
 * de toute partie.
 */
public class Snake implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
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
	
	/** Mode avion ({@code true}) ou pas ({@code false}) */
	public boolean fly;
	
	/** Items affectant ce serpent, associés à leur temps d'effet restant (en ms) */
	public Map<Item, Long> currentItems;
	
	/** Score courant du joueur associé au serpent dans la partie courante */
	public int currentScore;
}
