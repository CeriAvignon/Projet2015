package fr.univavignon.courbes.physics.groupe04;
import fr.univavignon.courbes.graphics.*;
import fr.univavignon.courbes.common.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Test 
{
	public static void main(String[] args)
	{
		int tab[]={1,2,3};		// profileIDs
		MyPhysicsEngine a = new MyPhysicsEngine();//  EASIER FOR COLLISIONS
		a.ourBoard = a.init(800,600,tab);
		Map<Integer,Direction> com = new HashMap<Integer,Direction>();
		a.ourBoard.snakes[0].currentAngle = 180;
		a.ourBoard.snakes[1].currentAngle = 0;
		a.ourBoard.snakes[2].currentAngle = 90;
		com.put(1, Direction.RIGHT);
		com.put(2, Direction.NONE);
		com.put(3, Direction.LEFT); 

		JPanel c = new JPanel();
		JPanel b = new JPanel();

		MyGraphics z = new MyGraphics();

		z.init(a.ourBoard, 20, c, b);

		JFrame fen = new JFrame();

		fen.setTitle("Test");
		fen.setSize(800,600);
		fen.setLocationRelativeTo(null);
		//z.end();
		fen.setContentPane(z.getPan());
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setVisible(true);

		while(true)
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