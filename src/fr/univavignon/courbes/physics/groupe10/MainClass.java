package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainClass {
	
	public static void main(String[] args) {
		
 		int tab[]={1};//,2,3};		// profileIDs
		Rnd a = new Rnd();//  EASIER FOR COLLISIONS
		a.board = a.init(800,600,tab);
		
		Map<Integer,Direction> com = new HashMap<Integer,Direction>();
		com.put(1, Direction.RIGHT);
		//com.put(2, Direction.NONE);
		//com.put(3, Direction.LEFT);
		
		
		//a.board.snakes[0].currentX = 10;
		//a.board.snakes[0].currentY = 10;
		

		//a.snakeDrawHead(0, a.board.snakes[0].currentX, a.board.snakes[0].currentY, (int) a.board.snakes[0].headRadius);
		//a.snakeHeadCollision(0);
		
		//a.board.snakes[1].currentAngle = 0;
		//a.board.snakes[2].currentAngle = 90;
		
		
		JPanel c = new JPanel();
		JPanel b = new JPanel();

		MyGraphics z = new MyGraphics();

		z.init(a.board, 20, c, b);

		JFrame fen = new JFrame();

		fen.setTitle("Test");
		fen.setSize(800,600);
		fen.setLocationRelativeTo(null);
		//z.end();
		fen.setContentPane(z.getPan());
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setVisible(true);
		
		/*
		for (int i = 0; i < 1000; i++)
		{
			a.update(1, com);
			z.update();
		}
		*/
		
		while(true)
		{
		
			a.update(30, com);
			z.update();
			
			
			try {
    		Thread.sleep(20);                 //25 milliseconds is one second.
			} catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
			}

		}
	}
}
