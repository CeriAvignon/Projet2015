package fr.univavignon.courbes.graphics.groupe18;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color; 
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;

/**
 * @author uapv1504323
 *
 */
public class GraphicDisplayGroupe18 implements GraphicDisplay {

	public Board board;
	public Integer pointThreshold;
	public List<Profile> players;
	public JPanel boardPanel;
	public JPanel scorePanel;

	@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel) {
		// TODO Auto-generated method stub
		this.board = board;
		this.pointThreshold = new Integer(pointThreshold);
		this.players = players;
		this.boardPanel = boardPanel;
		this.scorePanel = scorePanel;
		setScorePanel(this.board, this.pointThreshold, this.players, this.scorePanel);
		setBoardPanel(this.board, this.boardPanel);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		this.scorePanel.removeAll();
		this.boardPanel.removeAll();
		setScorePanel(this.board, this.pointThreshold, this.players, this.scorePanel);
		setBoardPanel(this.board, this.boardPanel);
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		this.boardPanel.removeAll();
		JLabel label = new JLabel();
		this.boardPanel.add(label, BorderLayout.CENTER);
	}
	
	public static void setScorePanel(Board board, int pointThreshold, List<Profile> players, JPanel scorePanel) {
	    int size = players.size();
	    int goal = pointThreshold;
	    GridLayout jours = new GridLayout(size+1, size+1);
	    FlowLayout left = new FlowLayout(FlowLayout.LEFT);
	    FlowLayout center = new FlowLayout(FlowLayout.CENTER);
	    scorePanel.setLayout(jours);
	    
    	JPanel panel = new JPanel();
    	panel.setBackground(Color.black);
    	panel.setLayout(left);
    	String test = "Score à atteindre : ";
	    JLabel text = new JLabel(test);
	    panel.setFont(new Font("Verdana",1,18));
	    text.setForeground(Color.white);
	    panel.add(text)	;
	    
    	JPanel panel2 = new JPanel();
    	panel2.setBackground(Color.black);
    	panel2.setLayout(center);
    	String Goal = String.valueOf(goal);
	    JLabel text2 = new JLabel(Goal);
	    panel2.setFont(new Font("Verdana",1,18));
	    panel2.add(text2);
	    
	    scorePanel.add(panel);
	    scorePanel.add(panel2);
	    
	    for(int i=0; i<size;i++) {
	    	JPanel panel3 = new JPanel();
	    	panel3.setBackground(Color.black);
	    	panel3.setLayout(left);
	    	String pseudo = (players.get(i)).userName;
		    JLabel text3 = new JLabel(pseudo);
		    panel.setFont(new Font("Verdana",1,18));
		    setColor(text3,board.snakes[i].playerId);
		    text.setForeground(Color.white);
		    panel.add(text3)	;
		    
	    	JPanel panel4 = new JPanel();
	    	panel4.setBackground(Color.black);
	    	panel4.setLayout(center);
	    	String Score = String.valueOf(board.snakes[i].currentScore);
		    JLabel text4 = new JLabel(Score);
		    panel2.setFont(new Font("Verdana",1,18));
		    setColor(text4,i);
		    panel2.add(text4);
		    
		    
	    	scorePanel.add(panel3);
	    	scorePanel.add(panel4);
	    }
	}
	
	public static void setBoardPanel(Board board, JPanel boardPanel) {
		JPanel draw = new Draw(board.width, board.height, board);
		draw.setBackground(Color.black);
		boardPanel.add(draw);
	}
	
	public static void setColor(JLabel label, int id) {
		switch (id)
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
				label.setForeground(Color.white);
		    break;
			case 4:
				label.setForeground(Color.cyan);
		    break;
			case 5:
				label.setForeground(Color.magenta);
		    break;
			case 6:
				label.setForeground(Color.pink);
		    break;
			case 7:
				label.setForeground(Color.orange);
		    break;
		}
	}
	
		public static void main(String[] args){

	}

		
}
