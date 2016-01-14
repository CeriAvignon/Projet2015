package fr.univavignon.courbes.physics.groupe05
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainClass {
	
	public static void main(String[] args) {
		
		
		int width = 500;
		int height = 500;
		int profiles[] = {101,102,103};
	  	
		Round r = new Round();
		
		r.board = r.init(width, height, profiles);
		
		
		Map<Integer,Direction> commands = new HashMap<Integer, Direction>();
		
		commands.put(profileId[0], Direction.RIGHT);
		commands.put(profileId[1], Direction.RIGHT);
		commands.put(profileId[2], Direction.NONE);


		r.update(50, commands);

		
		System.out.println(r.board.itemsMap.isEmpty());
		
	

}
}
