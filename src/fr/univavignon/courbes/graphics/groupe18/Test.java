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

public class Test {
	public static void main(String[] args){
		
		JFrame fenetreScore = new JFrame();
		fenetreScore.setTitle("Démonstration Score Panel");
		fenetreScore.setSize(350, 800);
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
		joueur1.currentX = 100;
		joueur1.currentY = 100;
		joueur1.currentScore = 10;
		joueur1.headRadius = 10;
		joueur1.state = false;
		Snake joueur2 = new Snake();
		joueur2.playerId = 1;
		joueur2.currentX = 700;
		joueur2.currentY = 100;
		joueur2.currentScore = 20;
		joueur2.headRadius = 10;
		joueur2.state = false;
		Snake joueur3 = new Snake();
		joueur3.playerId = 2;
		joueur3.currentX = 100;
		joueur3.currentY = 700;
		joueur3.currentScore = 39;
		joueur3.headRadius = 10;
		joueur3.state = false;
		Snake joueur4 = new Snake();
		joueur4.playerId = 3;
		joueur4.currentX = 700;
		joueur4.currentY = 700;
		joueur4.currentScore = 30;
		joueur4.headRadius = 10;
		joueur4.state = false;
		Snake joueur5 = new Snake();
		joueur5.playerId = 4;
		joueur5.currentX = 400;
		joueur5.currentY = 400;
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
		
		for(int i = 0; i<100; i++){
			for(int j = 100; j<110; j++) {
				snakesMap.put(new Position(i,j),0);
			}
		}
		Test.update();
		Test.end();
		
		
		fenetreScore.add(scorePanel);
		fenetreBoard.add(boardPanel);        
		fenetreScore.setVisible(true);
		fenetreBoard.setVisible(true);

	}
}
