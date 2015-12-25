package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import fr.univavignon.courbes.common.*;


/**
 * @author groupe09
 * Classe contenant la boucle du jeu ou le moteur phisique et graphique sont mis a jour
 * on sort de la boucle une fois la manche fini 
 */
public class Game extends Fenetre {
	
	/**
	 * 
	 */
	int limitePoint;
	/**
	 * 
	 */
	int x;
	/**
	 * 
	 */
	int y;
	/**
	 * 
	 */
	List players = new List();
	/**
	 * 
	 */
	Graphics graphe1 = new Graphics();
	/**
	 * 
	 */
	physics physic1 = new physics();
	/**
	 * 
	 */
	private JPanel boardPanel;
	/**
	 * 
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
		
	}
	/**
	 * 
	 */
	public void manche() {
	
	
		Board board1 = new Board();
		graphe1.init(board1, limitePoint, (java.util.List<Profile>) players, boardPanel, scorePanel);
		physic1.init(limitePoint, limitePoint, null);
		while(true) {
			System.out.println("le jeux");
			
		}
	}

}
