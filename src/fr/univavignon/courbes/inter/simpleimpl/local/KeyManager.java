package fr.univavignon.courbes.inter.simpleimpl.local;

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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Player;

/**
 * Classe chargée de gérer l'appui et le relâchement des touches,
 * qu'il s'agisse de touches systèmes ou des touches contrôlées
 * par chaque joueur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class KeyManager implements KeyListener
{	/** Touche utilisée pour basculer en/sortir de pause */
	private int PAUSE_KEY = KeyEvent.VK_P;
	/** Touche utilisée pour exécuter une seule itération, quand on est en pause */
	private int STEP_KEY = KeyEvent.VK_O;

	/**
	 * Crée un nouveau gestionnaire de touche.
	 * 
	 * @param players
	 * 		Tableau des joueurs concernés par la manche.
	 */
	public KeyManager(Player players[])
	{	initMappings(players);
		initDirections(players);
		initPause();
	}
	
	/** Associe à une touche le numéro du joueur qui l'a sélectionnée */
	private Map<Integer,Integer> keyPlayerMapping;
	/** Associe à une touche la direction qu'elle représente */
	private Map<Integer,Direction> keyDirectionMapping;
	/** Direction courante des joueurs */
	private Direction[] currentDirections;
	/** Indique si le jeu est en pause */
	private boolean pause = false;
	/** Indique si le joueur a demandé d'exécuter une seule itération (pendant la pause) */
	private boolean passIteration = false;
	/** Enregistrement de l'état des touches (nécessaire pour l'exécution d'une itération) */
	private Map<Integer,Boolean> keyState;
	
	/**
	 * Initialise les hashmap nécessaires à la gestion
	 * des touches.
	 * 
	 * @param players
	 * 		Joueurs avec les touches qu'ils ont sélectionnées.
	 */
	private void initMappings(Player players[])
	{	keyPlayerMapping = new HashMap<Integer,Integer>();
		keyDirectionMapping = new HashMap<Integer,Direction>();
		for(Player player: players)
		{	keyPlayerMapping.put(player.leftKey, player.playerId);
			keyDirectionMapping.put(player.leftKey, Direction.LEFT);
			
			keyPlayerMapping.put(player.rightKey, player.playerId);
			keyDirectionMapping.put(player.rightKey, Direction.RIGHT);
		}
	}
	
	/**
	 * Initialise le tableau des directions courantes de chaque joueur.
	 * 
	 * @param players
	 * 		Joueurs concernés.
	 */
	private void initDirections(Player players[])
	{	currentDirections = new Direction[players.length];
		for(int i=0;i<currentDirections.length;i++)
			currentDirections[i] = Direction.NONE;
	}
	
	/**
	 * Initialise les structures relatives à la gestion de la pause.
	 */
	private void initPause()
	{	pause = false;
		passIteration = false;
		keyState = new HashMap<Integer,Boolean>();
		keyState.put(PAUSE_KEY,false);
		keyState.put(STEP_KEY,false);
	}
	
	/**
	 * Change la direction courante du joueur spécifié.
	 * 
	 * @param playerId
	 * 		Numéro du joueur concerné.
	 * @param direction
	 * 		Nouvelle direction du joueur.
	 */
	private synchronized void setDirection(int playerId, Direction direction)
	{	currentDirections[playerId] = direction;
	}
	
	/**
	 * Retire la direction du joueur spécifié.
	 * 
	 * @param playerId
	 * 		Numéro du joueur concerné.
	 * @param direction
	 * 		Direction à annuler.
	 */
	private synchronized void unsetDirection(int playerId, Direction direction)
	{	if(currentDirections[playerId] == direction)
			currentDirections[playerId] = Direction.NONE;
	}
	
	/**
	 * Utilisé lors d'une partie réseau pour traiter les joueurs distants :
	 * cette méthode se substitue à l'appui physique sur une touche.
	 * 
	 * @param playerId
	 * 		Numéro du joueur concerné.
	 * @param direction
	 * 		Nouvelle direction de ce joueur distant.
	 */
	public synchronized void forceDirection(int playerId, Direction direction)
	{	currentDirections[playerId] = direction;
	}
	
	/**
	 * Renvoie les directions courantes de tous les joueurs.
	 * À noter qu'il s'agit d'une copie du tableau stocké dans ce
	 * gestionnaire de touches, afin d'éviter tout problème d'accès
	 * concurrent. 
	 * 
	 * @return
	 * 		Directions courantes des joueurs.
	 */
	public synchronized Direction[] retrieveDirections()
	{	Direction[] result = Arrays.copyOf(currentDirections, currentDirections.length);
		return result;
	}
	
	/**
	 * Réinitialise les dernières directions stockées, en prévision
	 * du début d'une nouvelle manche.
	 */
	public synchronized void reset()
	{	Arrays.fill(currentDirections,Direction.NONE);
		pause = false;
		passIteration = false;
		keyState.put(PAUSE_KEY,false);
		keyState.put(STEP_KEY,false);
	}
	
	/**
	 * Bascule en/sort de pause.
	 */
	public synchronized void switchPause()
	{	pause = !pause;
	}
	
	/**
	 * Indique si le jeu est en pause ou pas.
	 * 
	 * @return
	 * 		{@code true} ssi le jeu est en pause.
	 */
	public synchronized boolean isPaused()
	{	boolean result = pause;
		return result;
	}
	
	/**
	 * Modifie le flag indiquant si le joueur veut exécuter
	 * une seule itération quand il est en mode pause.
	 * 
	 * @param passIteration
	 * 		Nouvelle valeur du flag.
	 */
	public synchronized void setPassIteration(boolean passIteration)
	{	this.passIteration = passIteration;
	}
	
	/**
	 * Indique si le joueur a demandé de passer une itération
	 * lorsqu'il est en mode pause.
	 * 
	 * @return
	 * 		{@code true} ssi le joueur veut passer une itération.
	 */
	public synchronized boolean getPassIteration()
	{	boolean result = passIteration;
		return result;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{	// pas utilisé
	}

	@Override
	public void keyPressed(KeyEvent e)
	{	int keyCode = e.getKeyCode();
	
		// touches des joueurs
		Integer playerId = keyPlayerMapping.get(keyCode);
		if(playerId!=null)
		{	Direction direction = keyDirectionMapping.get(keyCode);
			setDirection(playerId,direction);
		}
		
		// touches système
		else
		{	Boolean state = keyState.get(keyCode);
			if(state!=null)
			{	if(keyCode==PAUSE_KEY)
				{	if(!state)
					{	keyState.put(PAUSE_KEY,true);
						switchPause();
					}
				}
				else if(keyCode==STEP_KEY)
				{	if(!state)
					{	keyState.put(STEP_KEY,true);
						setPassIteration(true);
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{	int keyCode = e.getKeyCode();
	
		// touches des joueurs
		Integer playerId = keyPlayerMapping.get(keyCode);
		if(playerId!=null)
		{	Direction direction = keyDirectionMapping.get(keyCode);
			unsetDirection(playerId,direction);
		}
		
		// touches système
		else if(keyCode==PAUSE_KEY)
			keyState.put(PAUSE_KEY,false);
		else if(keyCode==STEP_KEY)
			keyState.put(STEP_KEY,false);
	}	
}
