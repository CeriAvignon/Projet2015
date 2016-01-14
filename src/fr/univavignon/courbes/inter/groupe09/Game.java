package fr.univavignon.courbes.inter.groupe09;


import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.inter.groupe09.moteur.Graphics;
import fr.univavignon.courbes.inter.groupe09.moteur.physics;

import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;



/**
 * @author groupe09
 * Classe contenant l'ensembles des méthodes lié au dérouleet du jeu et sa configuration,
 * configuration d'une partie, boucle du jeu, calcule du score, deroulement d'une manche, d'une partie. 
 */
public class Game extends Fenetre implements KeyListener{
	
	int codeCmd;
	int maxPlayer = 6;
	int currentPalyer = 0;
	Board board1 = new Board();
	int [] profileIds;
	Snake[] tabSnake;
	Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
	boolean end = false;
	/**
	 * point max de la partie, la partie se finie une fois ce max atteind par un joueur
	 */
	int pointThreshold = 40;
	
	/**
	 * liste des profiles des joueur participants à la partie
	 */
	ArrayList<Profile> players = new ArrayList<Profile>();
	/**
	 * objet permettant d'utiliser les méthode du moteur graphique
	 */
	Graphics graphe1 = new Graphics();
	/**
	 * objet permettant d'utiliser les méthode du moteur physique
	 */
	physics physic1 = new physics();
	/**
	 * panel contenant la board de la partie
	 */
	private JPanel boardPanel;
	/**
	 * panel contenant le score de la partie 
	 */
	private JPanel scorePanel;

	/**
	 * @param titre
	 * @param x
	 * @param y
	 */
	public Game(String titre, int x, int y, ArrayList<Profile> joueurs) {
		
		super(titre, x, y);
		boardPanel = new JPanel();
		scorePanel = new JPanel();
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(scorePanel);
		contenu.add(boardPanel);
		setVisible(true);
		setResizable(false);
		// ajout d'un écouteur sur le clavier
		this.addKeyListener(this);
		for(int i = 0; i< joueurs.size();i++) {
			players.add(joueurs.get(i));
			Snake serpent = new Snake();
			serpent.playerId = i;
			profileIds[i] = i;
			serpent.profileId = joueurs.get(i).profileId;
			serpent.currentScore = 0;
			tabSnake[i] = serpent;
			
		}
		
	}
	/**
	 * déroulement d'une manche, implementation de la boucle du jeu
	 */
	public void manche() {
	
		graphe1.init(board1, pointThreshold, players, boardPanel, scorePanel);
		physic1.init(600, 600, profileIds);
		
		// thread qui controle si il y a un gagant ou pas 
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!end) {
					for(int i = 0; i< tabSnake.length; i++)
					if ( tabSnake[1].currentScore == pointThreshold) {
						end = true;
					}		
				}
			Thread.interrupted();	
			}
				
		}).start();
		// thread qui enleve les joueur mort de la liste players qui elle e contient que les joueur vivant
		// calcule aussi le score avec la methode scoreplayer
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (players.size() > 1) {
					for (int i = 0; i< players.size(); i++) {
						// supression du joueur de la liste de sjoueur courrants
						if(!tabSnake[i].state) {
							players.remove(i);
							tabSnake[i].currentScore = scorePlayer(tabSnake[i].currentScore);
						}
					}
				}
			}
		}).start();
		while (!end) {
			while(players.size() > 1) {
				graphe1.update();
				physic1.update(300, commandesTest);	
			}
			graphe1.end();
		
		}
		graphe1.end();
	}
	
	/**
	 * calcule du score, sur 6 joueur de la partie, le 1er à mourir gagne 0 point, le dernier vivant gagne 5 points
	 */
	public int scorePlayer(int currentScore) {
		int sc = 0;
		// premier joueur out
		if (players.size() == 5) {
			sc = 0;
		}
		if (players.size() < 5 && players.size() > 1) {
			sc = tabSnake.length - (players.size() +1);
		}
		return currentScore = currentScore + sc;
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		codeCmd = e.getKeyCode();
		switch (codeCmd) {
		// Correspend à la touche directionnel gauche, tourné à gauche pour le joueur 1
		case KeyEvent.VK_LEFT :
			commandesTest.put(0, Direction.LEFT);
			break;
		// Correspend à la touche directionnel droite, tourné à droite pour le joueur 1	
		case KeyEvent.VK_RIGHT :
			commandesTest.put(0, Direction.RIGHT);
			break;
		// Correspend à la touche a, tourné à gauche pour le joueur 2
		case 65: 
			commandesTest.put(1, Direction.LEFT);
		// Correspend à la touche z, tourné à droite pour le joueur 2	
		case 90:
			commandesTest.put(1, Direction.RIGHT);
		// Correspend à la touche 4 du pavé numérique, tourné à gauche pour le joueur 3
		case 52: 
			commandesTest.put(2, Direction.LEFT);
		// Correspend à la touche 6 du pavé numérique, tourné à droite pour le joueur 3	
		case 54:
			commandesTest.put(2, Direction.LEFT);
			
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	

}
