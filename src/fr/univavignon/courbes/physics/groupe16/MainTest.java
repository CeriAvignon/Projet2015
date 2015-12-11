package fr.univavignon.courbes.physics.groupe16;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;


public class MainTest {

	public static void main(String[] args) {
		Round round = new Round();
		int[] idPlayers = { 0 };
		round.init(400, 400, idPlayers);
		Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();

		commandesTest.put(0, Direction.LEFT);
		
	    Round round1= new Round();
	    round1.init(400, 400, idPlayers)
;	    
		//commandesTest.put(0, Direction.RIGHT );


	}

}
