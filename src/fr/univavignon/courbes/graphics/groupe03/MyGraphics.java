package fr.univavignon.courbes.graphics.groupe03;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;


/**
 * @author matrouf
 *
 */
public class MyGraphics extends JFrame implements GraphicDisplay {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	protected Dessin dessin = new Dessin();
	/**
	 * 
	 */
	private static JPanel scorePanel = null;
	/**
	 * 
	 */
	private JPanel boardPanel = null;
	/**
	 * 
	 */
	private MyGraphics mongraph;
	/**
	 * 
	 */
	private Board board;
	/**
	 * 
	 */
	private int pointThreshold;
	/**
	 * 
	 */
	private List<Profile> players;
	private JPanel PanelFinal;
    

	/**
	 */
	
	
	
	
	
	
	private JLabel libname;
	private JTextField libscore;
	
	
	
	
	/**
	 * @param height
	 * @param width
	 * @param board 
	 * @return 
	 */
	public MyGraphics nouveau_Frame(int height,int width,Board board,String type) 
		{
		             
           libname=new JLabel("amine");
           libscore=new JTextField(100);
		
		    //Instanciation d'un objet JPanel
			this.setTitle("Ma première fenêtre Java");
			this.setSize(height, width);
			this.setLocationRelativeTo(null);
		    //Définition de sa couleur de fond
		    //On prévient notre JFrame que notre JPanel sera son content pane
			if(type=="create")
			{
			JPanel pan = create_panel_plateau(board,height,width);
					 
			this.setContentPane(pan);   
			
		    this.setVisible(true);
		    
			}
			if(type=="score")
			{
				JPanel pan = create_panel_score(board,height,width);
				 
				this.setContentPane(pan);   
				
			    this.setVisible(true);
			    
				
			}
			if(type=="fin")
			{

				JPanel pan = create_panel_fin(board,height,width);
				 
				this.setContentPane(pan);   
				
			    this.setVisible(true);
				
			}
			return this;
		}
	
	/**
	 * @param width 
	 * @param height 
	 * @param board2 
	 * @return
	 */
	public JPanel create_panel_score(Board board2, int height, int width)
	{
		dessin=new Dessin(height,width,board);
		JPanel pan = new JPanel();		
	    pan.setBackground(Color.ORANGE);

		pan.add(libname);
	    pan.add(libscore);
	    setScorePanel(pan);
	    return pan;

	}
	
	/**
	 * dsc
	 * @param board 
	 * @param height 
	 * @param width 
	 * @return
	 */
	
	public JPanel create_panel_plateau(Board board,int height,int width)
	{
		dessin=new Dessin(height,width,board);
		JPanel pan = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                  super.paintComponent(g);
                  // affichage du modèle du jeu
                  MyGraphics.this.dessin.affichage(g);
            }
      };
      	
	    pan.setBackground(Color.white); 
	    
	   // pan.repaint();
	    boardPanel=pan;
	    return pan;

	}
	
	
	
	
	public JPanel create_panel_fin(Board board,int height,int width)
	{
		dessin=new Dessin(height,width,board);
		JPanel pan = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void paintComponent(Graphics g) {
                  super.paintComponent(g);
                  // affichage du modèle du jeu
                  MyGraphics.this.dessin.affichage_fin(g);
            }
      };
      	
	    pan.setBackground(Color.white); 
	    
	   // pan.repaint();
	    PanelFinal=pan;
	    return pan;

	}
	@Override
	
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel){
		setMongraph(nouveau_Frame(board.height,board.width,board,"create"));
		//setMongraph(nouveau_Frame(board.height,board.width,board,"score"));
		this.setBoard(board);
		this.setPointThreshold(pointThreshold);
		this.setPlayers(players);
	}
	
	

	/**
	 */



	@Override
	public void update() {

		//setBoardPanel(this.board,this.boardPanel);
		this.boardPanel.repaint();
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		nouveau_Frame(300, 300, board,"fin");
		
	}
	
	
	
	
	
	/**
	 * @param args
	 */




	/**
	 * @return
	 */
	public MyGraphics getMongraph() {
		return mongraph;
	}




	/**
	 * @param mongraph
	 */
	public void setMongraph(MyGraphics mongraph) {
		this.mongraph = mongraph;
	}

	/**
	 * @return
	 */
	public static JPanel getScorePanel() {
		return scorePanel;
	}

	/**
	 * @param scorePanel
	 */
	public static void setScorePanel(JPanel scorePanel) {
		MyGraphics.scorePanel = scorePanel;
	}

	/**
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * @return
	 */
	public int getPointThreshold() {
		return pointThreshold;
	}

	/**
	 * @param pointThreshold
	 */
	public void setPointThreshold(int pointThreshold) {
		this.pointThreshold = pointThreshold;
	}

	/**
	 * @return
	 */
	public List<Profile> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 */
	public void setPlayers(List<Profile> players) {
		this.players = players;
	}
} ;


