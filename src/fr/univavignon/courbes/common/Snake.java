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
import java.util.Set;

/**
 * Cette classe contient les informations et caractéristiques d'un serpent.
 * <br/>
 * L'ID du joueur associé au serpent est relatif à la partie courante. Par 
 * exemple, si on a 4 joueurs, alors leurs ID vont de 0 à 3. À ne pas confondre
 * avec l'ID du profil, qui est valable pour le jeu globalement, et indépendant
 * de toute partie.
 * <br/>
 * Afin de résoudre les problèmes de ralentissement rencontrés sur les PC du CERI,
 * la traine d'un serpent est maintenant représentée en deux sections : celle apparue 
 * lors de la dernière itération, et celle qui est plus ancienne. Seule la dernière
 * est dessinée pixel-par-pixel à chaque itération (le reste prenant la forme d'une image). 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Snake implements Serializable
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////
	////	IDENTIFICATION
	////////////////////////////////////////////////////////////////
	/** Numéro du joueur associé à ce serpent, dans la partie en cours */
	public int playerId;
	
	////////////////////////////////////////////////////////////////
	////	POSITION
	////////////////////////////////////////////////////////////////
	/** Position courante de la tête du serpent en abscisse */
	public int currentX;
	/** Position courante de la tête du serpent en ordonnée */
	public int currentY;
	
	////////////////////////////////////////////////////////////////
	////	TRAINE
	////////////////////////////////////////////////////////////////
	/** Ancienne section de la trainée du serpent sur l'aire de jeu */
	public Set<Position> oldTrail;
	/** Nouvelle section de la trainée du serpent sur l'aire de jeu */
	public Set<Position> newTrail;
	/** Indique si la trainée du serpent a été réinitialisée lors de la dernière itération */
	public boolean clearedTrail;

	////////////////////////////////////////////////////////////////
	////	DONNEES PHYSIQUES
	////////////////////////////////////////////////////////////////
	/** Angle représentant la direction de déplacement courante du serpent, par rapport à l'horizontale */
	public float currentAngle;
	/** Rayon de la tête du serpent, en pixels */
	public int headRadius;
	/** Vitesse de déplacement du serpent, exprimée en pixel par ms */
	public float movingSpeed;
	/** Vitesse à laquelle le serpent change de direction, exprimée en radians par ms */
	public float turningSpeed;
	
	////////////////////////////////////////////////////////////////
	////	ETAT
	////////////////////////////////////////////////////////////////
	/** Etat de vie du serpent : {@code null} si encore en jeu, négatif si collision avec bordure, valeur entière {@code playerId} si collision avec le joueur numéro {@code playerId} (qui peut être lui-même) */
	public Integer eliminatedBy;
	/** Etat de connexion du serpent : {@code false} il est distant et déconnecté, {@code true} il est distant connecté ou local */
	public boolean connected;
	
	////////////////////////////////////////////////////////////////
	////	ITEMS
	////////////////////////////////////////////////////////////////
	/** File contenant les items affectant actuellement ce serpent */
	public Queue<ItemInstance> currentItems;
	/**Inversion des commandes : {@code false} le serpent est dirigé normalement, {@code true} ses commandes sont inversées */
	public boolean inversion;
	/** Mode avion ({@code true}) ou pas ({@code false}) (effet de l'item {@link ItemType#USER_FLY}) */
	public boolean fly;
}
