package fr.univavignon.courbes.graphics.groupe18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;

/**
 * Sert a effectuer les tests de notre composante
 * @author uapv1504323 Antoine Letourneur
 * @author uapv1402334 Axel Clerici
 */
public class Test {
	/**
	 * @param args
	 * 		parametre par defaut de la fonction main, non utilisé dans notre programme
	 */
	public static void main(String[] args){
		
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
		joueur5.currentScore = 35;
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
			fenetreScore.setVisible(true);
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
			fenetreScore.setVisible(true);
			fenetreBoard.setVisible(true);
			
			try {
	    		Thread.sleep(30);
				} 
			catch(InterruptedException ex) {
	    		Thread.currentThread().interrupt();
				}
		}
		Test.end();
		fenetreScore.setVisible(true);
		fenetreBoard.setVisible(true);
		


	}
}
