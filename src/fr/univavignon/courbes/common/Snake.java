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

import java.io.Serializable;
import java.util.Queue;

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
	/** Rayon de la tête du serpent, en pixels */
	public int headRadius;
	/** Vitesse de déplacement du serpent, exprimée en pixel par ms */
	public double movingSpeed;
	/** Vitesse à laquelle le serpent change de direction, exprimée en radians par ms */
	public double turningSpeed;
	
	/** Etat de vie du serpent : {@code false} il est mort, {@code true} il est vivant */
	public boolean alive;
	/** Etat de connexion du serpent : {@code false} il est distant et déconnecté, {@code true} il est distant connecté ou local */
	public boolean connected;

	/** Mode de collision : {@code false} il est insensible aux collisions, {@code true} il l’est (pour le début de partie) */
	public boolean collision;
	/**Inversion des commandes : {@code false} le serpent est dirigé normalement, {@code true} ses commandes sont inversées */
	public boolean inversion;
	/** Mode avion ({@code true}) ou pas ({@code false}) (pour l'item {@link ItemType#USER_FLY}) */
	public boolean fly;
	
	/** Taux de création de trous dans le tracé : pour 0 on n'a aucun trou, pour 1 on n'a aucune traînée (i.e. que des trous). Ce taux est la probabilité qu'un serpent laisse un trou lors d'une itération donnée. */
	public double holeRate;
	/** Taille du trou restant à "effectuer", ou zéro si aucun trou n'est en cours de réalisation, exprimée en pixels */
	public int remainingHoleWidth;
	
	/** File contenant les items affectant actuellement ce serpent */
	public Queue<ItemInstance> currentItems;
	
	/** Score courant du joueur associé au serpent dans la partie courante */
	public int currentScore;
}

//TODO dans MP, écrire des méthodes pour initiliaser toutes ces classes (même une classe initializer ou builder)