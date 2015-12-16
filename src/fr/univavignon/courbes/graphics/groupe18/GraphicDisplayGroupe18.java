package fr.univavignon.courbes.graphics.groupe18;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color; 
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;

/**
 * GraphicDisplayGroupe18 est la classe implémentant l'interface GraphicDisplay 
 * et qui permet l'affichage du Board et des scores
 * @author uapv1504323 Antoine Letourneur
 * @author uapv1402334 Axel Clerici
 */
public class GraphicDisplayGroupe18 implements GraphicDisplay {

	/**
	 * Aire de jeu
	 */
	public Board board;
	
	/**
	 * Panel graphique utilisé comme support pour dessiner l'aire de jeu.
	 */
	public JPanel boardPanel;

	/**
	 * Panel graphique utilisé comme support pour dessiner les scores 
	 * 	de la partie en cours.
	 */
	public JPanel scorePanel;
	/**
	 * Liste des joueurs impliqués dans la manche.
	 */
	public List<Profile> players;
	/**
	 * Limite de points initiale pour cette manche.
	 */
	public int pointThreshold;

	@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel) {
		this.board = board;
		this.players = players;
		this.pointThreshold = pointThreshold;
		this.boardPanel = boardPanel;
		this.scorePanel = scorePanel;
		setScorePanel(this.board, pointThreshold, players, this.scorePanel);
		setBoardPanel(this.board, this.boardPanel);
	}

	@Override
	public void update() {
		this.boardPanel.removeAll();
		setBoardPanel(this.board, this.boardPanel);
	}

	@Override
	public void end() {
		this.boardPanel.removeAll();
		int max = 0 ;
		String winner = "init";
		int profileId = 0;
		for( int i = 0; i<(this.board.snakes.length); i++) {
			if(this.board.snakes[i].currentScore > max) {
				max = this.board.snakes[i].currentScore;
				winner = this.players.get(i).userName;
				profileId = this.board.snakes[i].playerId;
			}
		}
		if(max < this.pointThreshold) {
			for(int i = 0 ; i<this.board.snakes.length;i++) {
				if(this.board.snakes[i].state == true) {
					 winner = this.players.get(i).userName;
					 profileId = this.board.snakes[i].playerId;
				}
			}
			JLabel text = new JLabel(winner + " remporte la manche !");
		    text.setFont(new Font("Verdana",1,23));
		    setColor(text,profileId);
		    this.boardPanel.setBackground(Color.BLACK);
		    this.boardPanel.add(text,BorderLayout.CENTER);

		    
		}
		else {
		    GridLayout display = new GridLayout(board.snakes.length+1,board.snakes.length+1);
		    FlowLayout leftColumn = new FlowLayout(FlowLayout.CENTER);
		    FlowLayout rightColumn = new FlowLayout(FlowLayout.CENTER);
		    this.boardPanel.setLayout(display);
		    
	    	JPanel panel = new JPanel();
	    	panel.setBackground(Color.black);
	    	panel.setLayout(leftColumn);
		    JLabel text = new JLabel("Le gagnant de la partie est :  ");
		    text.setFont(new Font("Verdana",1,23));
		    setColor(text,profileId);
		    panel.add(text)	;
		    
	    	JPanel panel2 = new JPanel();
	    	panel2.setBackground(Color.black);
	    	panel2.setLayout(rightColumn);
		    JLabel text2 = new JLabel(winner);
		    text2.setFont(new Font("Verdana",1,27));
		    setColor(text2,profileId);
		    panel2.add(text2);
		    
		    this.boardPanel.add(panel);
		    this.boardPanel.add(panel2);
		    drawScores(this.pointThreshold, this.board.snakes.length, board, players, leftColumn, rightColumn, this.boardPanel);
		}
		
		
	}
	/**
	 * Dessine le panel scorePanel en entier, affiche le score a atteindre et le score de chaque joueur pendant la partie
	 * @param board 
	 *	 	C'est l'aire de jeu
	 * @param pointThreshold 
	 * 		C'est le score maximal a atteindre
	 * @param players 
	 *  	La liste des profils des joueurs
	 * @param scorePanel 
	 * 		Le JPanel sur lequel on dessine
	 */
	public static void setScorePanel(Board board, int pointThreshold, List<Profile> players, JPanel scorePanel) {
	    int size = players.size();
	    int goal = pointThreshold;
	    GridLayout display = new GridLayout(size+1, size+1);
	    FlowLayout leftColumn = new FlowLayout(FlowLayout.LEFT);
	    FlowLayout rightColumn = new FlowLayout(FlowLayout.CENTER);
	    scorePanel.setLayout(display);
	    
    	JPanel panel = new JPanel();
    	panel.setBackground(Color.black);
    	panel.setLayout(leftColumn);
    	String test = "Score à atteindre : ";
	    JLabel text = new JLabel(test);
	    text.setFont(new Font("Arial",Font.PLAIN,20));
	    text.setForeground(Color.white);
	    panel.add(text)	;
	    
    	JPanel panel2 = new JPanel();
    	panel2.setBackground(Color.black);
    	panel2.setLayout(rightColumn);
    	String Goal = String.valueOf(goal);
	    JLabel text2 = new JLabel(Goal);
	    text2.setFont(new Font("Arial",Font.PLAIN,30));
	    text2.setForeground(Color.white);
	    panel2.add(text2);
	    
	    scorePanel.add(panel);
	    scorePanel.add(panel2);
	    drawScores(goal, size, board, players, leftColumn, rightColumn, scorePanel);

	}
	
	/**
	 * Dessine le panel boardPanel, le plateau de jeu
	 * @param board
	 * 		C'est l'aire de jeu
	 * @param boardPanel
	 *		 Panel graphique utilisé comme support pour dessiner les scores 
	 * 	de la partie en cours.
	 */
	public static void setBoardPanel(Board board, JPanel boardPanel) {
		JPanel draw = new Draw(board.width, board.height, board);
		draw.setBackground(Color.black);
		boardPanel.add(draw);
		
	}
	
	/**
	 * drawScores est la fonction qui affiche la liste des joueurs et leur scores dans l'ordre décroissant
	 * @param goal
	 * 		Le score a atteindre
	 * @param size
	 * 		Le nombre de joueurs
	 * @param board
	 * 		C'est l'aire de jeu
	 * @param players
	 * 		La liste des profils des joueurs
	 * @param leftColumn
	 * 		Le layout des panels de la colonne gauche, pour gérer l'alignement du texte
	 * @param rightColumn
	 * 		Le layout des panels de la colonne de droite, pour gérer l'alignement du texte
	 * @param gridPanel
	 * 		C'est le layout du panel, découpe en 2 colonnes et n+1 lignes (la ligne score a atteindre + n joueurs)
	 */
	public static void drawScores(int goal, int size, Board board, List<Profile> players, FlowLayout leftColumn, FlowLayout rightColumn, JPanel gridPanel) {
		   for(int n = goal; n>0; n--) {
		    	for(int i=0; i<size;i++) {
		    		if(board.snakes[i].currentScore == n) {
			    		JPanel panel3 = new JPanel();
			    		panel3.setBackground(Color.black);
			    		panel3.setLayout(leftColumn);
			    		String pseudo = (players.get(i)).userName;
			    		JLabel text3 = new JLabel(pseudo);
			    		text3.setFont(new Font("Arial",Font.PLAIN,20));
			    		setColor(text3,board.snakes[i].playerId);
			    		panel3.add(text3)	;
				    
			    		JPanel panel4 = new JPanel();
			    		panel4.setBackground(Color.black);
			    		panel4.setLayout(rightColumn);
			    		String Score = String.valueOf(board.snakes[i].currentScore);
			    		JLabel text4 = new JLabel(Score);
			    		text4.setFont(new Font("Arial",Font.PLAIN,30));
			    		setColor(text4,i);
			    		panel4.add(text4);
				    
				    
			    		gridPanel.add(panel3);
			    		gridPanel.add(panel4);
		    		}
		    }
		   }
	}
	
	/**
	 * Assigne une couleur au label qu'on lui passe en fonction de l'id du joueur 
	 * 	par rapport a un code couleur prédéfini
	 * @param label
	 * 		Le texte que l'on veut colorier
	 * @param playerId
	 * 		L'id du joueur
	 */
	public static void setColor(JLabel label, int playerId) {
		switch (playerId)
		{
			case 0:
				label.setForeground(Color.red);
		    break;
			case 1:
				label.setForeground(Color.blue);
		    break;
			case 2:
				label.setForeground(Color.green);
		    break;
			case 3:
				label.setForeground(Color.cyan);
		    break;
			case 4:
				label.setForeground(Color.orange);
		    break;
			case 5:
				label.setForeground(Color.magenta);
		    break;
			case 6:
				label.setForeground(Color.pink);
		    break;
			case 7:
				label.setForeground(Color.white);
		    break;
		}
	}
			
}
