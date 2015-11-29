package fr.univavignon.courbes.physics;

import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;

public class Round implements PhysicsEngine {
	Board board;
	
	

	@Override
	public Board init(int width, int height, int playerNbr) {
		board = new Board(width, height, playerNbr);
		return board;
	}

	@Override
	public void update(long elapsedTime, Map<Integer, Direction> commands) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void forceUpdate(Board board) {
		// TODO Auto-generated method stub
		
	}
	
}
