package fr.univavignon.courbes.inter.groupe09;


import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
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
	Map<Integer, Direction> commandesTest = new HashMap<Integer, Direction>();
	/**
	 * point max de la partie, la partie se finie une fois ce max atteind par un joueur
	 */
	int pointThreshold;
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
	Game(String titre, int x, int y) {
		
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
	}
	/**
	 * déroulement d'une manche, implementation de la boucle du jeu
	 */
	public void manche() {
	
		Board board1 = new Board();
		graphe1.init(board1, pointThreshold, players, boardPanel, scorePanel);
		physic1.init(500, 500, null);
		
		while(players.size() > 1) {
			graphe1.update();
			Map<Integer, Direction> commands = null;
			physic1.update(300, commands);	
		}
		graphe1.end();
		
	}
	
	/**
	 * calcule du score, sur 6 joueur de la partie, le 1er à mourir gagne 1 point, le dernier vivant gagne 6 point
	 */
	public int scorePlayer(int playerId, int profileId, int currentScore) {
		
		return currentScore = currentScore + maxPlayer - currentPalyer;
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		codeCmd = e.getKeyCode();
		switch (codeCmd) {
		// 52 correspend à la touche pavé numérique 4, tourné à gauche
		case 52 :
			commandesTest.put(0, Direction.LEFT);
			break;
		// 54 correspend à la touche pavé numérique 6, tourné à droite	
		case 54 :
			commandesTest.put(0, Direction.RIGHT);
			break;
		default :
			
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	

}
