package fr.univavignon.courbes.physics.groupe10;

import java.util.Map;

import fr.univavignon.courbes.common.*;
import fr.univavignon.courbes.physics.PhysicsEngine;

public class Rnd implements PhysicsEngine {
	
	Board br;
	
	@Override
	public Board  init(int width, int height, int[] profileIds)
	{
		/** J'instancie un Board en utilisant le constructeur */
		br = new Board(width, height);
		
		/** Le tableau de Snake avec le nombre de nombre de joueur passé en paramétre*/
		br.snakes = new Snake[profileIds.length];
		
		int player_id = 0;
		for (int i = 0; i < profileIds.length; i++)
		{
			br.snakes[i] = new Snake(player_id, profileIds[i], width/(profileIds.length+1)*(i+1), height/2, Math.random() * (2*Math.PI));
			player_id++;
		}
		
		
		return br;
	}
	
	@Override
	public void forceUpdate(fr.univavignon.courbes.common.Board board) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void update(long elapsedTime, Map<Integer,Direction> commands)
	{
		// Deplacer snakes "mettre à jour les snakes"
		//moveSnake(long, commands);

		// effect d'item sur les snakes
		//itemEffect();
		
	}
	
