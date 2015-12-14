package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainClass {
	
	public static void main(String[] args) {
		
		
		int width = 500;
		int height = 500;
		int profileIds[] = {101,102,103};
	  	
		Rnd r = new Rnd();
		
		r.board = r.init(width, height, profileIds);
		
		
		Map<Integer,Direction> commands = new HashMap<Integer, Direction>();
		
		// PROFILE ID OU PLAYER ID ??!
		
		commands.put(profileIds[0], Direction.LEFT);
		commands.put(profileIds[1], Direction.RIGHT);
		commands.put(profileIds[2], Direction.NONE);
		
		r.update(3000, commands);
		
		System.out.println(r.board.itemsMap.isEmpty());
		
		
		
	
		
		
	
	}

}
