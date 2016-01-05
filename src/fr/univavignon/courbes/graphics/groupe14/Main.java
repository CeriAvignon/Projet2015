package fr.univavignon.courbes.graphics.groupe14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.*;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.common.Item;

public class Main
{
	public static void main(String[] args)
	{
		JFrame scoreWindow = new JFrame();
		scoreWindow.setTitle("Scores");
		scoreWindow.setSize(350, 400);
		scoreWindow.setResizable(false);
		scoreWindow.setLocationRelativeTo(null);
		scoreWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		scoreWindow.setVisible(true);
		JFrame boardWindow = new JFrame();
		boardWindow.setTitle("Board");
		boardWindow.setSize(800, 800);
		boardWindow.setResizable(false);
		boardWindow.setLocationRelativeTo(null);
		boardWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		boardWindow.setVisible(true);
		
		JPanel panelScore = new JPanel();
		JPanel panelBoard = new JPanel();

		Snake player1 = new Snake();
		player1.state = false;
		player1.playerId = 0;
		player1.currentX = 100;
		player1.currentY = 100;
		player1.currentScore = 15;
		player1.headRadius = 10;
		
		Snake player2 = new Snake();
		player2.state = false;
		player2.playerId = 1;
		player2.currentX = 700;
		player2.currentY = 100;
		player2.currentScore = 11;
		player2.headRadius = 10;
		
		Snake player3 = new Snake();
		player3.state = false;
		player3.playerId = 2;
		player3.currentX = 100;
		player3.currentY = 700;
		player3.currentScore = 25;
		player3.headRadius = 10;
		
		Snake player4 = new Snake();
		player4.state = false;
		player4.playerId = 3;
		player4.currentX = 700;
		player4.currentY = 700;
		player4.currentScore = 40;
		player4.headRadius = 10;
		
		Snake player5 = new Snake();
		player5.state = true;
		player5.playerId = 4;
		player5.currentX = 200;
		player5.currentY = 400;
		player5.currentScore = 37;
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
		int k = 0;
		int large = 7;
		
		board.itemsMap = new HashMap<Position,Item>();
		board.itemsMap.put(new Position(200,200),Item.USER_SPEED);
		board.itemsMap.put(new Position(400,400),Item.USER_SLOW);
		board.itemsMap.put(new Position(300,300),Item.USER_BIG_HOLE);
		board.itemsMap.put(new Position(250,250),Item.OTHERS_SPEED);
		board.itemsMap.put(new Position(50,80),Item.OTHERS_THICK);
		board.itemsMap.put(new Position(700,100),Item.OTHERS_SLOW);
		board.itemsMap.put(new Position(600,100),Item.OTHERS_REVERSE);
		board.itemsMap.put(new Position(400,100),Item.COLLECTIVE_THREE_CIRCLES);
		board.itemsMap.put(new Position(300,100),Item.COLLECTIVE_TRAVERSE_WALL);
		board.itemsMap.put(new Position(200,100),Item.COLLECTIVE_ERASER);
		
		for(int i = 0; i<300; i++)
		{
			for(int j = player1.currentY-large; j <= player1.currentY+large; j++)
				{snakesMap.put(new Position(player1.currentX,j),0);}
			for(int j = player2.currentY-large; j <= player2.currentY+large; j++)
				{snakesMap.put(new Position(player2.currentX,j),1);}
			for(int j = player3.currentY-large; j <= player3.currentY+large; j++)
				{snakesMap.put(new Position(player3.currentX,j),2);}
			for(int j = player4.currentY-large; j <= player4.currentY+large; j++)
				{snakesMap.put(new Position(player4.currentX,j),3);}
			
			player1.currentX++;
			player1.currentY++;
			player2.currentX--;
			player2.currentY++;
			player3.currentX++;
			player3.currentY--;
			player4.currentX--;
			player4.currentY--;
			player5.currentX++;
			Main.update();
			scoreWindow.add(panelScore);
			boardWindow.add(panelBoard);     
			scoreWindow.setVisible(true);
			boardWindow.setVisible(true);
			
			try {Thread.sleep(20);}
			catch(InterruptedException ex) {Thread.currentThread().interrupt();}
		
		}
		
		
		// end() en commentaire : Pas appelé
		// enlever le commentaire pour afficher la fin du round / partie
		
		Main.end();
		scoreWindow.setLayout(new BorderLayout());
		scoreWindow.add(panelScore, BorderLayout.CENTER);
		//boardWindow.add(panelBoard);     
		scoreWindow.setVisible(true);
		boardWindow.setVisible(true);
	}
}
