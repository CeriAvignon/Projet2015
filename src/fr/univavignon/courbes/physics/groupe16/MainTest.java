package fr.univavignon.courbes.physics.groupe16;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.groupe23.MyGraphic;
import fr.univavignon.courbes.graphics.groupe23.Panel;


public class MainTest {

	public static void main(String[] args) {
		Round round = new Round();

		if(true) {  // TEST MOTEUR GRAPHIQUE
			int[] idPlayers = {0};
			round.init(250, 250, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			//commandesTest.put(0, Direction.LEFT);
			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  100;
			round.board.snakes[0].currentAngle =  45;

			
			
			JFrame fenetre= new JFrame();
			fenetre.setSize(600, 600);
			fenetre.setLayout(null);
			JPanel boardPanel = new JPanel();
			boardPanel.setBounds(126, 11, 314, 267);
			JPanel scorePanel = new JPanel();
			scorePanel.setBounds(10, 11, 100, 278);
			List<Profile> pro = new ArrayList();
			Profile prof1 =new Profile();
			prof1.userName="omar";
			pro.add(prof1);
			MyGraphic graph =new MyGraphic();
			graph.init(round.board,0,pro,boardPanel,scorePanel);
			fenetre.add(boardPanel);
			fenetre.add(scorePanel);
			fenetre.setVisible(true);
			
			for(int i=0;i<200;i++)
			{
				round.update(20, commandesTest);
				graph.update();
			}
		}


		if(false) { // TEST MOUVEMENT NORMAL SNAKE + SPAWN ITEM
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();

			round.board.snakes[0].currentX =  50;
			round.board.snakes[0].currentY =  350;
			round.board.snakes[0].currentAngle =  45;
			round.board.snakes[0].movingSpeed /= 3; // diminuer vitesse pour mieux montrer le spawn random des items
			for(int i = 0; i< 750; i++) {
				round.update(20, commandesTest);

			}

		}

		if(false) { // TEST CHANGEMENT DIRECTION SNAKE
			int[] idPlayers = {0,4};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			commandesTest.put(0, Direction.LEFT);
			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  100;
			round.board.snakes[0].currentAngle =  90;

			for(int i = 0; i< 50; i++) {
				round.update(20, commandesTest);
			}

		}

		if(false) { // TEST SNAKE COLLISION
			int[] idPlayers = {0,100};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  350;
			round.board.snakes[0].currentAngle =  90;
			round.board.snakes[0].headRadius =  5;
			round.board.snakes[1].currentX =  90;
			round.board.snakes[1].currentY =  20;
			round.board.snakes[1].currentAngle =  0;

			for(int i = 0; i< 100 ; i++) {
				round.update(50, commandesTest);
			}
		}

		if(false) { // TEST ITEM COLLISION
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
			round.board.snakes[0].currentX =  200;
			round.board.snakes[0].currentY =  100;
			round.board.snakes[0].currentAngle =  180;

			Item item = Item.USER_SLOW;
			item.duration = 3000;
			round.board.itemsMap.put(new Position(100,100), item );

			for(int i = 0; i< 50; i++) {
				round.update(20, commandesTest);
			}
		}

		if(false) { // TEST BORDURE COLLISION
			int[] idPlayers = {0};
			round.init(400, 400, idPlayers);
			Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();

			round.board.snakes[0].currentX =  100;
			round.board.snakes[0].currentY =  150;
			round.board.snakes[0].currentAngle =  180;
			round.board.snakes[0].collision =  false;

			for(int i = 0; i< 50; i++) {
				round.update(20, commandesTest);
			}
		}


	}

}
