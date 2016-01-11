package fr.univavignon.courbes.graphics.groupe18;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.physics.groupe04.MyPhysicsEngine;
import fr.univavignon.courbes.physics.groupe10.Rnd;

/**
 * Sert a effectuer les tests de notre composante
 * @author uapv1504323 Antoine Letourneur
 * @author uapv1402334 Axel Clerici
 */
public  class Test {
	
	public static Map<Integer, Direction> commandMap;
	public static Integer pointThreshold;

	/**
	 * @param args
	 * 		parametre par defaut de la fonction main, non utilisé dans notre programme
	 */
	public static void main(String[] args){
		/*
		JFrame fenetreScore = new JFrame();
		fenetreScore.setTitle("Démonstration Score Panel");
		fenetreScore.setSize(380, 800);
		fenetreScore.setLocationRelativeTo(null);
		fenetreScore.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		fenetreScore.setVisible(true);
		JFrame fenetreBoard = new JFrame();
		fenetreBoard.setTitle("Démonstration Board Panel");
		fenetreBoard.setSize(800, 800);
		fenetreBoard.setLocationRelativeTo(null);
		fenetreBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		fenetreBoard.setVisible(true);
		
		JPanel scorePanel = new JPanel();
		JPanel boardPanel = new JPanel();

		Snake joueur1 = new Snake();
		joueur1.playerId = 0;
		joueur1.currentX = 0;
		joueur1.currentY = 100;
		joueur1.currentScore = 10;
		joueur1.headRadius = 10;
		joueur1.state = false;
		Snake joueur2 = new Snake();
		joueur2.playerId = 1;
		joueur2.currentX = 700;
		joueur2.currentY = 300;
		joueur2.currentScore = 20;
		joueur2.headRadius = 10;
		joueur2.state = false;
		Snake joueur3 = new Snake();
		joueur3.playerId = 2;
		joueur3.currentX = 250;
		joueur3.currentY = 400;
		joueur3.currentScore = 39;
		joueur3.headRadius = 10;
		joueur3.state = false;
		Snake joueur4 = new Snake();
		joueur4.playerId = 3;
		joueur4.currentX = 700;
		joueur4.currentY = 700;
		joueur4.currentScore = 17;
		joueur4.headRadius = 10;
		joueur4.state = false;
		Snake joueur5 = new Snake();
		joueur5.playerId = 4;
		joueur5.currentX = 150;
		joueur5.currentY = 600;
		joueur5.currentScore = 40;
		joueur5.headRadius = 10;
		joueur5.state = true;

		
		Profile joueur1Profile = new Profile();
		joueur1Profile.userName = "Blobfish";
		Profile joueur2Profile = new Profile();
		joueur2Profile.userName = "Giraffe";
		Profile joueur3Profile = new Profile();
		joueur3Profile.userName = "Guest 120 Billion";
		Profile joueur4Profile = new Profile();
		joueur4Profile.userName = "Lama";
		Profile joueur5Profile = new Profile();
		joueur5Profile.userName = "Pingouin";
		List<Profile> players = new ArrayList<Profile>();
		players.add(joueur1Profile);
		players.add(joueur2Profile);
		players.add(joueur3Profile);
		players.add(joueur4Profile);
		players.add(joueur5Profile);
		
		Map<Position,Integer> snakesMap = new HashMap<Position,Integer>();
		
		Board board = new Board();
		board.height = 800;
		board.width = 800;
		board.snakes = new Snake[5];
		board.snakes[0] = joueur1;
		board.snakes[1] = joueur2;
		board.snakes[2] = joueur3;
		board.snakes[3] = joueur4;
		board.snakes[4] = joueur5;
		board.snakesMap = snakesMap;
		int pointThreshold = 40;
		
		
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
		
		GraphicDisplayGroupe18 Test = new GraphicDisplayGroupe18();
		Test.init(board, pointThreshold,players,boardPanel,scorePanel);
		
		
		fenetreScore.add(scorePanel);
		fenetreBoard.add(boardPanel);
		
		int head = (int)joueur1.headRadius;
		int xDepart = joueur1.currentX;	
	
		for(int i = joueur1.currentX; i<xDepart+100; i++){
			for(int j = joueur1.currentY-(head/2); j<=joueur1.currentY+(head/2); j++) {
				snakesMap.put(new Position(i,j),joueur1.playerId);
			}
			joueur1.currentX = i;
			Test.update();       
			//fenetreScore.setVisible(true);
			fenetreBoard.setVisible(true);
			
			try {
	    		Thread.sleep(30);
				} 
			catch(InterruptedException ex) {
	    		Thread.currentThread().interrupt();
				}
		}
		
		int yDepart = joueur2.currentY;
		int head2 = (int)joueur2.headRadius;
		fenetreBoard.setVisible(true);
		for(int i = joueur2.currentY; i<yDepart+200; i++){
			for(int j = joueur2.currentX-(head2/2); j<=joueur2.currentX+(head2/2); j++) {
				snakesMap.put(new Position(j,i),joueur2.playerId);
			}
			joueur2.currentY = i;
			Test.update();   
			//fenetreScore.setVisible(true);
			fenetreBoard.setVisible(true);
			
			try {
	    		Thread.sleep(30);
				} 
			catch(InterruptedException ex) {
	    		Thread.currentThread().interrupt();
				}
		}
		//Test.end();
		//fenetreScore.setVisible(true);
		fenetreBoard.setVisible(true);*/
		mainLoop();
		
	}
	
	
	public static void mainLoop() {
		
		MyPhysicsEngine MP = new MyPhysicsEngine();
		GraphicDisplayGroupe18 MG = new GraphicDisplayGroupe18();
		List<Profile> players = new ArrayList<Profile>();
		int profileIds[] = new int[5];
		JFrame window = new JFrame();

		Profile joueur1Profile = new Profile();
		joueur1Profile.userName = "Blobfish";
		joueur1Profile.profileId = 5555;
		Profile joueur2Profile = new Profile();
		joueur2Profile.userName = "Giraffe";
		joueur2Profile.profileId = 42;
		Profile joueur3Profile = new Profile();
		joueur3Profile.userName = "Guest 120 Billion";
		joueur3Profile.profileId = 666;
		Profile joueur4Profile = new Profile();
		joueur4Profile.userName = "Lama";
		joueur4Profile.profileId = 1234;
		Profile joueur5Profile = new Profile();
		joueur5Profile.userName = "Pingouin";
		joueur5Profile.profileId = 3;
		players.add(joueur1Profile);
		players.add(joueur2Profile);
		players.add(joueur3Profile);
		players.add(joueur4Profile);
		players.add(joueur5Profile);
		
		profileIds[0] = joueur1Profile.profileId;
		profileIds[1] = joueur2Profile.profileId;
		profileIds[2] = joueur3Profile.profileId;
		profileIds[3] = joueur4Profile.profileId;
		profileIds[4] = joueur5Profile.profileId;
		
		Board board = MP.init(800, 600 , profileIds);
		
		int scores[] = new int[board.snakes.length];
		for(int i=0; i<board.snakes.length; i++) {
			scores[i] = 37;
		}
		pointThreshold = 10 * (board.snakes.length-1);
		window.setTitle("Curve Fever");
		window.setSize(board.width + 500, board.height + 100);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		window.setVisible(true);
		
		JPanel boardPanel = new JPanel();
		JPanel scorePanel = new JPanel();
		JPanel content = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		content.setLayout(new GridBagLayout());
		content.setPreferredSize(new Dimension(board.width + 400, board.height));
		boardPanel.setPreferredSize(new Dimension(board.width, board.height));
		scorePanel.setPreferredSize(new Dimension(400, board.height));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		content.add(scorePanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		content.add(boardPanel, gbc);
		boolean gameOver = false;
		commandMap = new HashMap<Integer, Direction>();
		
		boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	        .put(KeyStroke.getKeyStroke("D"), "test1");
		boardPanel.getActionMap().put("test1", new MoveAction(Direction.RIGHT));
		
		boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke("Q"), "test2");
	boardPanel.getActionMap().put("test2", new MoveAction(Direction.LEFT));
		
	
		while( gameOver == false) {
			boolean roundOver = false;
			board = MP.init(board.width, board.height, profileIds);
			for(int i=0;i<board.snakes.length;i++) {
				board.snakes[i].currentScore = scores[i];
			}
			MG.init(board, pointThreshold, players, boardPanel, scorePanel);
			window.repaint();
			
			window.add(content);
			window.setVisible(true);
			List<Integer> order = new ArrayList<Integer>();
			while (roundOver == false) {
				try {
		    		Thread.sleep(70);
					} 
				catch(InterruptedException ex) {
		    		Thread.currentThread().interrupt();
					}
				MP.update(70, commandMap);
				MG.update();
				window.setVisible(true);
				for(int i = 0; i<board.snakes.length; i++) {
					if(board.snakes[i].state == false) {
						boolean alreadyDead = false;
						for(int j = 0; j< order.size(); j++) {
							if(order.get(j)== board.snakes[i].playerId) {
								alreadyDead = true;
							}
						}
						if (alreadyDead == false) {
							order.add(board.snakes[i].playerId);
						}
					}
					
					if(order.size() == board.snakes.length-1) {
						for(int k = 0; k< board.snakes.length; k++) {
							if(board.snakes[k].state == true) {
								order.add(board.snakes[k].playerId);
							}
						}
					}
				}
				roundOver = isRoundOver(board.snakes);
			}
			
			updateScores(order, scores, board.snakes);
			for(int i=0;i<board.snakes.length;i++) {
				board.snakes[i].currentScore = scores[i];
			}
			
			MG.end();
			window.setVisible(true);
			gameOver = isGameOver(board.snakes);
			if(gameOver == true) {
				window.remove(scorePanel);
				window.repaint();
				window.setVisible(true);
			}
			try {
	    		Thread.sleep(2000);
				} 
			catch(InterruptedException ex) {
	    		Thread.currentThread().interrupt();
				}
		}
	}
	
	
	public static void updatePointThreshold(Snake snakes[]) {
		int compt = 0;
		for (int i = 0; i < snakes.length; i ++) {
			if (snakes[i].currentScore >( pointThreshold - (snakes.length -1)))
				compt ++;
		}
		if (compt >= 2)
			pointThreshold += 2;
		System.out.println(pointThreshold);
	}
	
	public static boolean isGameOver(Snake snakes[]) {
		int max = 0;
		for( int i = 0; i<(snakes.length); i++) {
			if(snakes[i].currentScore >= max) {
				max = snakes[i].currentScore;
			}
		}
		if(max >= pointThreshold)
			return true;
		else
			return false;
	}
	
	public static boolean isRoundOver(Snake snakes[]) {
		int compt = 0;
		for(int i = 0 ; i<snakes.length;i++) {
			if(snakes[i].state == true)
				compt ++;
		}
		if (compt >= 2)
			return false;
		else
			return true;
	}
	
	
	static class MoveAction extends AbstractAction {
		Direction direction;
		MoveAction(Direction direction) {
			this.direction = direction;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Test.commandMap.put(1, this.direction);
		}
	}
	
	public static void updateScores(List<Integer> order, int scores[], Snake snakes[]) {
		for(int i = 0; i<order.size(); i++) {
			scores[order.get(i)] += i;
		}
		for(int i =0; i<snakes.length; i++) {
			snakes[i].currentScore = scores[i];
		}
	}
}

