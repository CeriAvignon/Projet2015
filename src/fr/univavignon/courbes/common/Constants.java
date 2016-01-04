package fr.univavignon.courbes.common;

/**
 * Cette classe contient uniquement les constantes du jeu, dont les valeurs
 * sont imposées à toutes les implémentations des composantes.
 * <br/>
 * N'hésitez pas à faire une demande sur le forum si vous estimez que certaines
 * valeurs devraient figurer ici.
 */
public class Constants
{	
	/** Vitesse de déplacement normale (i.e. sans l'effet d'un item) des serpents, exprimée en pixel par ms */
	public static final float REGULAR_MOVING_SPEED = 0.1f;
	/** Vitesse de déplacement rapide des serpents, exprimée en pixel par ms */
	public static final float FAST_MOVING_SPEED = 0.1f;	//TODO valeur à estimer
	/** Vitesse de déplacement lente des serpents, exprimée en pixel par ms */
	public static final float SLOW_MOVING_SPEED = 0.1f; //TODO valeur à estimer
	
	/** Vitesse normale à laquelle le snake change de direction, exprimée en radians par ms */
	public static final float REGULAR_TURNING_SPEED = 0.003f;
	/** Vitesse élevée à laquelle le snake change de direction, exprimée en radians par ms */
	public static final float FAST_TURNING_SPEED = 0.003f; //TODO valeur à estimer
	/** Vitesse lente à laquelle le snake change de direction, exprimée en radians par ms */
	public static final float SLOW_TURNING_SPEED = 0.003f; //TODO valeur à estimer
	
	/** Rayon de la tête normale du snake, exprimé en pixels */ 
	public static final int REGULAR_HEAD_RADIUS = 4;
	/** Rayon de la tête agrandie du snake, exprimé en pixels */ 
	public static final int LARGE_HEAD_RADIUS = 4; //TODO valeur à estimer

	//TODO différents taux de création de trous ? (taux normal, taux élevé, seulement des trous, autres ?)
	
	// TODO certaines de ces constantes pourraient rendre inutiles des champs de Snake (ex. movingSpeed) dont
	// la valeur pourrait se déduire de ces constantes et de la nature des items faisant effet sur le Snake au moment considéré.
}
