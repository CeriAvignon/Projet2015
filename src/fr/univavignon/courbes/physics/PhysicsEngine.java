package fr.univavignon.courbes.physics;

import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Snake;

/**
 * Ensemble de méthodes permettant à l'Interface Utilisateur de 
 * demander au Moteur Physique d'effectuer différentes sortes de mises 
 * à jour.
 * <br/>
 * Chaque binôme de la composante Moteur Physique doit définir une classe
 * implémentant cette interface, qui sera instanciée par l'Interface Utilisateur. 
 */
public interface PhysicsEngine
{	
	/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * au début de chaque manche.
	 * <br/>
	 * Le Moteur Physique doit s'initialiser, et instancier un objet
	 * de type {@link Board} représentant l'aire de jeu de la manche
	 * qui va se dérouler ensuite. Il doit pour cela utiliser les
	 * valeurs passées en paramètres, puis renvoyer cet objet pour 
	 * que l'Interface Utilisateur puisse l'utiliser à son tour.
	 * 
	 * @param width
	 * 		Largeur de l'aire de jeu, exprimée en pixel.
	 * @param height
	 * 		Hauteur de l'aire de jeu, exprimée en pixel.
	 * @param profileIds
	 * 		Tableau contenant les numéros de profils des joueurs impliqués dans 
	 * 		la manche (à utiliser pour initialiser les objets {@link Snake}).
	 * @return
	 * 		Un objet représentant l'aire de jeu de la manche.
	 */
	public Board init(int width, int height, int[] profileIds);
	
	/**
	 * Méthode permettant d'initialiser un plateau de jeu destiné à tester
	 * l'intégration entre Moteur Graphique et Moteur Physique. Ce plateau
	 * doit respecter les contraintes suivantes :
	 * <ul>
	 * 	<li>Afficher tous les items existants</li>
	 * 	<li>Seulement deux joueurs : le premier est contrôlé normalement, le second est immobile</li>
	 * </ul>
	 * 
	 * @param width
	 * 		Largeur de l'aire de jeu, exprimée en pixel.
	 * @param height
	 * 		Hauteur de l'aire de jeu, exprimée en pixel.
	 * @param profileIds
	 * 		Tableau contenant les numéros de profils des joueurs impliqués dans 
	 * 		la manche (à utiliser pour initialiser les objets {@link Snake}).
	 * @return
	 * 		Un objet représentant l'aire de jeu de la manche.
	 */
	public Board initDemo(int width, int height, int[] profileIds);
	
	/**
	 * Cette méthode doit être appelée par l'Interface Utilisateur
	 * à chaque itération d'une manche.
	 * <br/>
	 * Elle réalise la mise à jour des données physiques, en prenant en compte 
	 * le temps écoulé depuis la dernière mise à jour. Le Moteur Physique
	 * doit pour cela modifier l'objet de type {@link Board} représentant
	 * l'aire de jeu, qui avait été initialisé par {@link #init}
	 * (ou par {@link #forceUpdate(Board)}.
	 * <br/>
	 * Le paramètre {@code commands} représente les dernières commandes générées
	 * par les joueurs. La map associe un ID de joueur à sa commande.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour, exprimé en ms.
	 * @param commands
	 * 		Map associant un joueur ID à la dernière commande générée par le joueur correspondant.
	 */
	public void update(long elapsedTime, Map<Integer,Direction> commands);
	
	/**
	 * Cette méthode est appelée par l'Interface Utilisateur côté client, lors 
	 * d'un jeu en réseau.
	 * <br/> 
	 * Elle remplace la manche courante par celle passée en paramètre. Elle 
	 * permet à l'Interface Utilisateur située côté client de remplacer l'objet 
	 * {@link Board} représentant la manche courante, par un nouvel objet envoyé 
	 * par le serveur, et représentant une version plus récente de l'aire de jeu.
	 * Autrement dit, dans le cas du jeu en réseau côté client, le Moteur
	 * Physique ne fait pas de calcul : il se contente de mettre à jour
	 * l'aire de jeu avec les informations qu'il reçoit. 
	 * <br/>
	 * Le remplacement doit se faire de manière à ne pas induire de modification 
	 * dans le traitement effectué par les autres composantes.
	 * 
	 * @param board
	 * 		Nouvelle aire de jeu, devant remplacer l'aire de jeu courante.
	 */
	public void forceUpdate(Board board);
}