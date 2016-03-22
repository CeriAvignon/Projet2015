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

import java.util.List;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.SmallUpdate;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 * Immplémentation de l'interface {@link PhysicsEngine}, i.e.
 * classe principale du Moteur Physique. L'essentiel du traitement
 * est délégué à des classes reproduisant la structures des classes
 * de données communes.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class PhysicsEngineImpl implements PhysicsEngine
{	/** Aire de jeu courante */
	private PhysBoard board;
	
	@Override
	public void init(int playerNbr, int width, int height)
	{	board = new PhysBoard(width,height);
		board.init(playerNbr);
	}
	
	@Override
	public void initDemo(int width, int height)
	{	board = new PhysBoard(width,height);
		board.initDemo();
	}

	@Override
	public Board getBoard()
	{	return board;
	}

	@Override
	public Board getBoardCopy()
	{	Board result = new PhysBoard(board);
		return result;
	}
	
	@Override
	public void setBoard(Board board)
	{	this.board = (PhysBoard)board;
	}
	
	@Override
	public void update(long elapsedTime, Direction commands[])
	{	board.update(elapsedTime,commands);
	}
	
	@Override
	public List<Integer> getEliminatedPlayers()
	{	return board.getEliminatedPlayers();
	}
		
	@Override
	public void forceUpdate(UpdateInterface updateData)
	{	if(updateData instanceof PhysBoard)
			this.board = (PhysBoard)updateData;
		else
		{	SmallUpdate smallUpdate = (SmallUpdate)updateData;
			board.forceUpdate(smallUpdate);
		}
	}
	
	@Override
	public SmallUpdate getSmallUpdate()
	{	return(board.smallUpdate);
	}
}
