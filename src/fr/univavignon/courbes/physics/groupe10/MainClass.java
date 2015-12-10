package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;

public class MainClass {
	
	public static void main(String[] args) {
		
		
		int width = 500;
		int height = 500;
		int profileIds[] = {101,102,103};
	  	
		Rnd r = new Rnd();
		
		Board b = r.init(width, height, profileIds);
		
		Position p = new Position(130,253);
		
		r.board.itemsMap.put(p, Item.USER_SPEED);
		
	//	System.out.println(r.board.itemsMap.containsKey(p));
		
	//	r.board.itemsMap.remove(p);
		
	//	System.out.println(r.board.itemsMap.containsKey(p));
		
		r.snakeMove(0, 100, Direction.NONE);
		/*
		Position pos = new Position(130,253);

		Item it1 = Item.values()[1];
		

		br.itemsMap.put(pos, it1);

		br.moveSnake(0, (long) 100, Direction.NONE);
		*/
		
		
		
	
	}

}
