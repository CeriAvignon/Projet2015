package fr.univavignon.courbes.physics.simpleimpl;

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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 * Immplémentation de l'interface {@link PhysicsEngine}, i.e.
 * classe principale du Moteur Physique. 
 */
public class PhysicsEngineImpl implements PhysicsEngine
{	
	@Override
	public Board init(int width, int height, Profile[] profiles)
	{	board = new Board();
		board.width = width;
		board.height = height;
		board.snakesMap = new HashMap<Position, Integer>();
		
		board.snakes = new Snake[profiles.length];
		for(int i=0;i<profiles.length;i++)
		{	Snake snake = new Snake();
			initSnake(i,snake,profiles[i]);
			board.snakes[i] = snake;
		}
		
		return board;
	}
	
	private Board board;
	
	/**
	 * Initialise le serpent passé en paramètre.
	 * 
	 * @param playerId
	 * 		Numéro du joueur dans la manche en cours.
	 * @param snake
	 * 		Serpent à initialiser.
	 * @param profile
	 * 		Profil du joueur contrôlant le serpent.
	 */
	private void initSnake(int playerId, Snake snake, Profile profile)
	{	snake.playerId = playerId;
		snake.profileId = profile.profileId;
		
		snake.currentX = (int)Math.floor(Math.random()*board.width);	// on tire une valeur entre 0 et width-1
		snake.currentY = (int)Math.floor(Math.random()*board.width);	// pareil entre 0 et height-1
		
		snake.currentAngle = Math.random()*Math.PI*2;	// on tire une valeur réelle entre 0 et 2pi
		snake.headRadius = Constants.REGULAR_HEAD_RADIUS;
		snake.movingSpeed = Constants.REGULAR_MOVING_SPEED;
		snake.turningSpeed = Constants.REGULAR_TURNING_SPEED;
		
		snake.alive = true;
		snake.connected = false;

		snake.collision = false;
		snake.inversion = false;
		snake.fly = false;
	
		snake.holeRate = Constants.HOLE_RATE;
		snake.remainingHoleWidth = 0;
	
		snake.currentItems = new LinkedList<ItemInstance>();
	
		snake.currentScore = 0;
	}
	
	@Override
	public Board initDemo(int width, int height, Profile[] profiles)
	{	// initialisation à peu près normale
		{	board = new Board();
			board.width = width;
			board.height = height;
			board.snakesMap = new HashMap<Position, Integer>();
			
			board.snakes = new Snake[profiles.length];
			for(int i=0;i<profiles.length;i++)
			{	Snake snake = new Snake();
				initSnake(i,snake,profiles[i]);
				snake.movingSpeed = 0;
				board.snakes[i] = snake;
			}
			board.snakes[0].movingSpeed = Constants.REGULAR_MOVING_SPEED;
		}
		
		// on rajoute les items
		// TODO
		
		return board;
	}
	
	@Override
	public void update(long elapsedTime, Map<Integer,Direction> commands)
	{	
		// TODO
	}
	
	@Override
	public void forceUpdate(Board board)
	{	
		// TODO
	}
}
