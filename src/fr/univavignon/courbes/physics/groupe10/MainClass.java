package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;

public class MainClass {
	
	public static void main(String[] args) {
		
		int width = 500;
		int height = 500;
		int profileIds[] = {101};
	  	
		Rnd r = new Rnd();
		
		r.board = r.init(width, height, profileIds);
		r.board.snakes[0].currentX = 20;
		r.board.snakes[0].currentY = 20;
		
		
		
		Map<Integer,Direction> commands = new HashMap<Integer, Direction>();
		
		// PROFILE ID OU PLAYER ID ??!
		
		commands.put(profileIds[0], Direction.LEFT);

		//while (true)
			r.update(20, commands);

		
		
		
		//System.out.println(r.board.itemsMap.isEmpty());
		
	

}
}
