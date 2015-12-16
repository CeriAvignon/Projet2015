package fr.univavignon.courbes.physics.groupe16;

import java.util.HashMap;
import java.util.Map;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;


public class MainTest {

	public static void main(String[] args) {
		Round round = new Round();

		if(false) { // TEST MOUVEMENT NORMAL SNAKE + SPAWN ITEM
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			
			round.board.snakes[0].currentX =  50;
			round.board.snakes[0].currentY =  350;
			round.board.snakes[0].currentAngle =  45;
			round.board.snakes[0].movingSpeed /= 3; // diminuer vitesse pour mieux montrer le spawn random des items
			for(int i = 0; i< 750; i++) {
				round.update(20, commandesTest);
			}
			
		}
		
		if(false) { // TEST CHANGEMENT DIRECTION SNAKE
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			commandesTest.put(0, Direction.LEFT);
			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  100;
			round.board.snakes[0].currentAngle =  90;
			
			for(int i = 0; i< 50; i++) {
				round.update(20, commandesTest);
			}
			
		}
		
		if(false) { // TEST SNAKE COLLISION
			int[] idPlayers = {0,1};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  110;
			round.board.snakes[0].currentAngle =  90;
			round.board.snakes[0].headRadius =  5;
			round.board.snakes[1].currentX =  95;
			round.board.snakes[1].currentY =  90;
			round.board.snakes[1].currentAngle =  0;
			
			for(int i = 0; i< 10; i++) {
				round.update(20, commandesTest);
			}
		}
		
		if(false) { // TEST ITEM COLLISION
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			round.board.snakes[0].currentX =  200;
			round.board.snakes[0].currentY =  100;
			round.board.snakes[0].currentAngle =  180;
			
			Item item = Item.USER_SPEED;
			item.duration = 3000;
			round.board.itemsMap.put(new Position(100,100), item );
			
			for(int i = 0; i< 50; i++) {
				round.update(20, commandesTest);
			}
		}
		
		if(true) { // TEST BORDURE COLLISION
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			
			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  150;
			round.board.snakes[0].currentAngle =  180;
			round.board.snakes[0].collision =  false;
			
			for(int i = 0; i< 50; i++) {
				round.update(20, commandesTest);
			}
		}

		
		
		
	}

}
