package fr.univavignon.courbes.physics.groupe10;
import fr.univavignon.courbes.common.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainClass {
	
	public static void main(String[] args) {
		
		//CREATION DES OBJETS PHYSIQUES
		
 		int tab[]={1};//,2,3};		// profileIDs
		Rnd a = new Rnd();//  EASIER FOR COLLISIONS
		a.board = a.init(800,600,tab);
		
		Map<Integer,Direction> com = new HashMap<Integer,Direction>();
		com.put(1, Direction.RIGHT);
		//com.put(2, Direction.RIGHT);
		//com.put(3, Direction.RIGHT);		
		
		// CREATION DES OBJETS GRAPHIQUES
		
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
		
		// BOUCLE D'UPDATE
		
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
