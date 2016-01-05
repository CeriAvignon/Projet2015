package fr.univavignon.courbes.graphics.groupe03;
import fr.univavignon.courbes.graphics.*;
import fr.univavignon.courbes.physics.groupe16.Round;
import fr.univavignon.courbes.common.*;

import java.util.*;
import java.awt.*;

import javax.swing.*;

public class Test 
{
	public static void main(String[] args)
	{
		int tab[]={0,1};		// profileIDs
		Round a = new Round();//  EASIER FOR COLLISIONS
		a.board = a.init(600,600,tab);
		Map<Integer,Direction> com = new HashMap<Integer,Direction>();
		//com.put(1, Direction.RIGHT);
		
		a.board.snakes[0].currentX =  200;
		a.board.snakes[0].currentY =  100;
		a.board.snakes[0].currentAngle =  180;
		a.board.snakes[1].currentX =  250;
		a.board.snakes[1].currentY =  200;
		a.board.snakes[1].currentAngle =  0;
		Item item = Item.COLLECTIVE_THREE_CIRCLES;
		item.duration = 3000;
		a.board.itemsMap.put(new Position(100,100), item );
		
		JPanel c = new JPanel();
		JPanel b = new JPanel();
		MyGraphics z = new MyGraphics();
		z.init(a.board, 20,null,null,null);


		while(z.isVisible())
		{
		
			a.update(10, com);
			z.update();

			try {
    		Thread.sleep(25);                 //25 milliseconds is one second.
			} catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
			}

		}
	}
}