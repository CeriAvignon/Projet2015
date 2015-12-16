package fr.univavignon.courbes.graphics.groupe14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;

public class Main
{
	public static void main(String[] args)
	{
		JFrame scoreWindow = new JFrame();
		scoreWindow.setTitle("Scores");
		scoreWindow.setSize(350, 400);
		scoreWindow.setLocationRelativeTo(null);
		scoreWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		scoreWindow.setVisible(true);
		JFrame boardWindow = new JFrame();
		boardWindow.setTitle("Board");
		boardWindow.setSize(800, 800);
		boardWindow.setLocationRelativeTo(null);
		boardWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		boardWindow.setVisible(true);
		
		JPanel panelScore = new JPanel();
		JPanel panelBoard = new JPanel();

		Snake player1 = new Snake();
		player1.state = true;
		player1.playerId = 0;
		player1.currentX = 100;
		player1.currentY = 100;
		player1.currentScore = 10;
		player1.headRadius = 10;
		
		Snake player2 = new Snake();
		player2.state = false;
		player2.playerId = 1;
		player2.currentX = 700;
		player2.currentY = 100;
		player2.currentScore = 20;
		player2.headRadius = 10;
		
		Snake player3 = new Snake();
		player3.state = false;
		player3.playerId = 2;
		player3.currentX = 100;
		player3.currentY = 700;
		player3.currentScore = 39;
		player3.headRadius = 10;
		
		Snake player4 = new Snake();
		player4.state = true;
		player4.playerId = 3;
		player4.currentX = 700;
		player4.currentY = 700;
		player4.currentScore = 30;
		player4.headRadius = 10;
		
		Snake player5 = new Snake();
		player5.state = false;
		player5.playerId = 4;
		player5.currentX = 400;
		player5.currentY = 400;
		player5.currentScore = 35;
		player5.headRadius = 10;

		
		Profile player1Profile = new Profile();
		player1Profile.userName = "Player 1";
		
		Profile player2Profile = new Profile();
		player2Profile.userName = "Player 2";
		
		Profile player3Profile = new Profile();
		player3Profile.userName = "Player 3";
		
		Profile player4Profile = new Profile();
		player4Profile.userName = "Player 4";
		
		Profile player5Profile = new Profile();
		player5Profile.userName = "Player 5";
		
		List<Profile> players = new ArrayList<Profile>();
		
		players.add(player1Profile);
		players.add(player2Profile);
		players.add(player3Profile);
		players.add(player4Profile);
		players.add(player5Profile);
		
		Map<Position,Integer> snakesMap = new HashMap<Position,Integer>();
		
		Board board = new Board();
		board.height = 800;
		board.width = 800;
		board.snakes = new Snake[5];
		board.snakes[0] = player1;
		board.snakes[1] = player2;
		board.snakes[2] = player3;
		board.snakes[3] = player4;
		board.snakes[4] = player5;
		board.snakesMap = snakesMap;
		int pointThreshold = 40;
		
		GraphicDisplayGroupe14 Main = new GraphicDisplayGroupe14();
		Main.init(board, pointThreshold,players,panelBoard,panelScore);
		
		for(int i = 100; i<=110; i++)
		{
			for(int j = 705; j<900; j++)
			{
				snakesMap.put(new Position(i,j),2);
			}
		}
		Main.update();
		// end() en commentaire : Pas appelé
		// enlever le commentaire pour afficher la fin du round / partie
		//Main.end();
		
		scoreWindow.add(panelScore);
		boardWindow.add(panelBoard);        
		scoreWindow.setVisible(true);
		boardWindow.setVisible(true);

	}
}
