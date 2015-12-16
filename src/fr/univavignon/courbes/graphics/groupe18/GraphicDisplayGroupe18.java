package fr.univavignon.courbes.graphics.groupe18;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Color; 
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.graphics.GraphicDisplay;

/**
 * @author uapv1504323
 * @author uapv1402334
 */
public class GraphicDisplayGroupe18 implements GraphicDisplay {

	public Board board;
	public JPanel boardPanel;
	public JPanel scorePanel;
	public List<Profile> players;
	public int pointThreshold;

	@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		this.boardPanel.removeAll();
		setBoardPanel(this.board, this.boardPanel);
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
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
			//boardPanel = new JPanel(new BorderLayout());
			//JLabel text = new JLabel (winner + " remport la manche !");
		   // text.setFont(new Font("Verdana",1,25));
		   // setColor(text,id);
			//text.setHorizontalAlignment(JLabel.CENTER);
			//text.setVerticalAlignment(JLabel.CENTER);
			//this.boardPanel.add(text);
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
	
	public static void setBoardPanel(Board board, JPanel boardPanel) {
		JPanel draw = new Draw(board.width, board.height, board);
		draw.setBackground(Color.black);
		boardPanel.add(draw);
		
	}
	
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
