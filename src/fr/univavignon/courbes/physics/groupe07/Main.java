package fr.univavignon.courbes.physics.groupe07;


import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;


import fr.univavignon.courbes.common.*;
import fr.univavignon.courbes.physics.groupe07.Round;
import fr.univavignon.courbes.physics.groupe07.groupe18.*;
import fr.univavignon.courbes.physics.groupe07.groupe23.*;



public class Main {
	public static void main(String[] args) {
		// Deux Joueurs, Q et D pour diriger le premier,
		// K et M pour diriger le second
		new MinimalLoop().mainLoop(2);
		/*int idPlayers[] = {0,1};
		Round newRound = new Round(800,600,idPlayers);
		
		 while(true)
         {
         
			 newRound.update(10, MinimalLoop.commandMap);
			 MinimalLoop.moteur.update();

			 try {
				 Thread.sleep(25);                 //25 milliseconds is one second.
			 } catch(InterruptedException ex) {
				 Thread.currentThread().interrupt();
			 }

         }*/
		/*
		int idPlayers[] = {0,1};
		Round newRound = new Round(500,500,idPlayers);
		Map<Integer, Direction> command = new HashMap<Integer, Direction>();
		command.put(0, Direction.NONE);
		command.put(1, Direction.LEFT);
		
		JPanel c = new JPanel();
		JPanel b = new JPanel();
		MyGraphic z = new MyGraphic();
		z.init(newRound.board, 20, c, b);

		JFrame fen = new JFrame();

		fen.setTitle("Curve Fever 3");
		fen.setSize(500,500);
		fen.setLocationRelativeTo(null);
		//z.end();
		fen.setContentPane(z.getPan());
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fen.setVisible(true);

		while(true)
		{
		
			newRound.update(10, command);
			z.update();

			try {
    		Thread.sleep(25);                 //25 milliseconds is one second.
			} catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
			}

		}*/
	}
}
