package fr.univavignon.courbes.physics.groupe07;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.groupe07.Round;

public class Main
{
	
	public static void main(String[] args)
	
	{
		/*int idPlayers[] = {0,1,2,3};
		Round newRound = new Round(500,500,idPlayers);
		Map<Integer, Direction> command = new HashMap<Integer, Direction>();
		command.put(0, Direction.NONE);
		command.put(1, Direction.NONE);
		command.put(2, Direction.NONE);
		command.put(3, Direction.NONE);
		long timeRound = 1;
		
		for(int i = 0 ; i < 1000 ; i++ )
		{
			newRound.update(timeRound,command);
		}*/
		int idPlayers[] = {0,1,2,3};
		Round newRound = new Round(500,500,idPlayers);
		Map<Integer, Direction> command = new HashMap<Integer, Direction>();
		command.put(0, Direction.NONE);
		command.put(1, Direction.LEFT);
		command.put(2, Direction.RIGHT);
		command.put(3, Direction.NONE);
		long timeRound = 1;
		
		JPanel c = new JPanel();
		JPanel b = new JPanel();
		MyGraphics z = new MyGraphics();
		z.init(newRound.ourBoard, 20, c, b);

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

		}
	}
}
