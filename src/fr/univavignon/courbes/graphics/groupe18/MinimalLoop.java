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
import fr.univavignon.courbes.physics.groupe07.Round;
import fr.univavignon.courbes.physics.groupe10.Rnd;

/**
 * Implémente la boucle minimale pour les tests
 * @author uapv1504323 Antoine Letourneur
 * @author uapv1402334 Axel Clerici
 */
public  class MinimalLoop {
	
	/**
	 * Map contenant les commandes appuyées par les joueurs
	 */
	public static Map<Integer, Direction> commandMap;
	/**
	 *  Score à atteindre pour gagner la partie
	 */
	public static Integer pointThreshold;

	/**
	 * @param nbPlayers
	 * 		Le nombre de snakes contrôlés par un joueur ( 1 ou 2)
	 *      pour les tests.
	 */

	public static void mainLoop(int nbPlayers) {
		
		GraphicDisplayGroupe18 MG = new GraphicDisplayGroupe18();
		commandMap = new HashMap<Integer, Direction>();
		List<Profile> players = initPlayers(nbPlayers);
		int profileIds[] = initProfile(players);
		// Pour chacun des 3 moteurs testés, il existait un bug dut à 
		// une inversion entre playerId et profileId dans init.
		Rnd MP = new Rnd();
		Board board = MP.init(800, 600 , profileIds);
		if (nbPlayers == 1)
			board = MP.initDemo(800, 600, profileIds);
		
		int scores[] = new int[board.snakes.length];
		for(int i=0; i<board.snakes.length; i++) {
			scores[i] = 0;
		}
		pointThreshold = 10 * (board.snakes.length-1);
		if(nbPlayers == 1)
			pointThreshold = 1;
		
		JFrame window = initWindow(board.width, board.height);
		JPanel boardPanel = new JPanel();
		JPanel scorePanel = new JPanel();
		JPanel content = new JPanel();
		initPanels(boardPanel, scorePanel, content, board.width, board.height);
		
		setBindings(boardPanel, nbPlayers);
		boolean gameOver = false;
		
		while( gameOver == false) {
			boolean roundOver = false;
			board = MP.init(board.width, board.height, profileIds);
			for(int i=0;i<board.snakes.length;i++) {
				board.snakes[i].currentScore = scores[i];
			}
			updatePointThreshold(board.snakes);
			MG.init(board, pointThreshold, players, boardPanel, scorePanel);
			window.add(content);
			window.setVisible(true);
			List<Integer> order = new ArrayList<Integer>();
			while (roundOver == false) {
				sleep(30);
				MP.update(30, commandMap);
				MG.update();
				window.setVisible(true);
				getOrder(board.snakes, order);
				roundOver = isRoundOver(board.snakes, nbPlayers);
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
			sleep(2000);
		}
	}
	
	
	/**
	 * Fonction initialisant les panels à la bonne dimension et les place
	 * dans la fenêtre.
	 * @param boardPanel
	 *        Le panel représentant l'aire de jeu
	 * @param scorePanel
	 *        Le panel contenant les scores des joueurs
	 * @param content
	 *        Le panel dans lequel seront placés le scorePanel et le boardPanel,
	 *        l'un à gauche l'autre à droite.
	 * @param width
	 * 		  Largeur de l'aire de jeu
	 * @param height
	 *        Hauteur de l'aire de jeu
	 */
	private static void initPanels(JPanel boardPanel, JPanel scorePanel,
			JPanel content, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		content.setLayout(new GridBagLayout());
		content.setPreferredSize(new Dimension(width + 400, height));
		boardPanel.setPreferredSize(new Dimension(width, height));
		scorePanel.setPreferredSize(new Dimension(400, height));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		content.add(scorePanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		content.add(boardPanel, gbc);
	}


	/**
	 * @param players
	 *        Nombre de snakes contrôlés par des joueurs
	 * @return
	 *        Le tableau contenant les profils des snakes joueurs
	 */
	private static int[] initProfile(List<Profile> players) {
		int profileIds[] = new int[players.size()];
		profileIds[0] = players.get(0).profileId;
		if (players.size() == 2)
			profileIds[1] = players.get(1).profileId;
		return profileIds;
	}


	/**
	 * @param nbplayers
	 *        Nombre de snakes contrôlés par des joueurs
	 * @return
	 *        La liste contenant les profiles joueurs
	 */
	private static List<Profile> initPlayers(int nbplayers) {
		List<Profile> players = new ArrayList<Profile>();
		Profile joueur1Profile = new Profile();
		joueur1Profile.userName = "Blobfish";
		joueur1Profile.profileId = 5555;
		players.add(joueur1Profile);
		if(nbplayers == 2) {
			Profile joueur2Profile = new Profile();
			joueur2Profile.userName = "Giraffe";
			joueur2Profile.profileId = 42;
			players.add(joueur2Profile);
		}		
		return players;
	}


	/**
	 * Fonction qui met à jour le score à atteindre pour gagner, en situation
	 * de Tie Break
	 * @param snakes
	 *              Tableau des snakes de la board
	 */
	public static void updatePointThreshold(Snake snakes[]) {
		int compt = 0;
		for (int i = 0; i < snakes.length; i ++) {
			if (snakes[i].currentScore >( pointThreshold - (snakes.length -1)))
				compt ++;
		}
		if (compt >= 2)
			pointThreshold += 2;
	}
	
	/**
	 * @param snakes
	 *        Tableau des snakes de la board
	 * @return
	 * 		  True si la partie est finie, false sinon
	 */
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
	
	/**
	 * @param snakes
	 *        Tableau des snakes de la board
	 * @return
	 *         true si la partie est finie, false sinon
	 */		  
	public static boolean isRoundOver(Snake snakes[], int nbPlayers) {
		int compt = 0;
		for(int i = 0 ; i<snakes.length;i++) {
			if(snakes[i].state == true)
				compt ++;
		}
		if (compt >= 2 && nbPlayers > 1)
			return false;
		else if (compt <2 && nbPlayers > 1)
			return true;
		else if (compt == 1 && nbPlayers == 1)
			return false;
		else return true;
	}
	
	
	/**
	 * @author uapv1402334
	 * Classe permettant de recevoir les actions des touches
	 */
	static class MoveAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Direction correspondante à la touche pressée
		 */
		Direction direction;
		/**
		 * Snake qui doit être affecté par la touche
		 */
		int player;
		/**
		 * @param direction
		 *        Oui
		 * @param player
		 * 		  Oui
		 */
		MoveAction(Direction direction, int player) {
			this.direction = direction;
			this.player = player;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			MinimalLoop.commandMap.put(player, this.direction);
		}
	}
	
	/**
	 * Fonction qui ajoute les scores dans un tableau en fonction de
	 * la position des joueurs en fin de manche
	 * @param order
	 *        Liste contenant l'ordre d'arrivée pour cette manche
	 * @param scores
	 *        Tableau contenant les scores actuels des joueurs
	 * @param snakes
	 *        Tableau contenant les snakes de la board
	 */
	public static void updateScores(List<Integer> order, int scores[], Snake snakes[]) {
		for(int i = 0; i<order.size(); i++) {
			scores[order.get(i)] += i;
		}
	}
	
	/**
	 * Fonction qui initialise la fenêtre de jeu
	 * @param width
	 *        Largeur de la fenêtre
	 * @param height
	 *        Hauteur de la fenêtre
	 * @return
	 *        Retourne la fenêtre correctement initialisée
	 */
	public static JFrame initWindow(int width, int height) {
		JFrame window  = new JFrame();
		window.setTitle("Curve Fever");
		window.setSize(width + 500, height + 100);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
		window.setVisible(true);
		return window;
	}
	
	/**
	 * Fonction permettant d'associer l'intéraction avec les touches
	 * Par défaut, le joueur 1 joue avec Q et D et le joueur 2 avec
	 * K et M
	 * @param boardPanel
	 *        Panel représentant l'aire de jeu
	 * @param nbPlayers
	 *        Nombre de snakes joueurs
	 */
	public static void setBindings(JPanel boardPanel, int nbPlayers) {
		boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke("Q"), "test1");
		boardPanel.getActionMap().put("test1", new MoveAction(Direction.RIGHT, 0));
		boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke("D"), "test2");
	
		boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke("released Q"), "release");
		boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke("released D"), "release");
		boardPanel.getActionMap().put("release", new MoveAction(Direction.NONE, 0));
		boardPanel.getActionMap().put("test2", new MoveAction(Direction.LEFT, 0));
		
		if(nbPlayers == 2) {
			boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	        .put(KeyStroke.getKeyStroke("K"), "test3");
			boardPanel.getActionMap().put("test3", new MoveAction(Direction.RIGHT, 1));
			boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke("M"), "test4");
		
			boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke("released K"), "release2");
			boardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke("released M"), "release2");
			boardPanel.getActionMap().put("release2", new MoveAction(Direction.NONE, 1));
			boardPanel.getActionMap().put("test4", new MoveAction(Direction.LEFT, 1));
		}
	}
	
	/**
	 * Permet de mettre un temps de pause
	 * @param time
	 *        Le temps de pause en miliseconds
	 */
	public static void sleep(int time) {
		try {
    		Thread.sleep(time);
			} 
		catch(InterruptedException ex) {
    		Thread.currentThread().interrupt();
			}
	}
	

	/**
	 * Fonction permettant de mettre à jour si nécessaire order, et donc
	 * de connaître l'ordre dans lequel les snakes meurrent
	 * @param snakes
	 *        Tableau des snakes de la board
	 * @param order
	 *        Tableau dans lequel on stock le playerId des snakes dans l'ordre
	 *        de leur mort.
	 */
	public static void getOrder(Snake snakes[], List<Integer> order) {
		for(int i = 0; i<snakes.length; i++) {
			if(snakes[i].state == false) {
				boolean alreadyDead = false;
				for(int j = 0; j< order.size(); j++) {
					if(order.get(j)== snakes[i].playerId) {
						alreadyDead = true;
					}
				}
				if (alreadyDead == false) {
					order.add(snakes[i].playerId);
				}
			}
			if(order.size() == snakes.length-1) {
				for(int k = 0; k< snakes.length; k++) {
					if(snakes[k].state == true) {
						order.add(snakes[k].playerId);
					}// TODO Auto-generated method stub
				}
			}
		}
	}
}

