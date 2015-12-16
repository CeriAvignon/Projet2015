package fr.univavignon.courbes.graphics.groupe14;

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

/** GraphicDisplayGroupe14 hérite de GraphicDisplay
 * @author1 uapv1400768 Drissi Remi
 * @author2 uapv1402587 Girardon Valentin
 */
public class GraphicDisplayGroupe14 implements GraphicDisplay
{
	public Board board;
	public JPanel panelBoard;
	public JPanel panelScore;
	public List<Profile> players;
	public int pointThreshold;

	@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel panelBoard, JPanel panelScore)
	{
		// TODO Auto-generated method stub
		this.board = board;
		this.players = players;
		this.pointThreshold = pointThreshold;
		this.panelBoard = panelBoard;
		this.panelScore = panelScore;
		setpanelScore(this.board, pointThreshold, players, this.panelScore);
		setpanelBoard(this.board, this.panelBoard);
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		this.panelBoard.removeAll();
		setpanelBoard(this.board, this.panelBoard);
	}

	@Override
	public void end()
	{
		this.panelBoard.removeAll();
		int max = 0 ;
		int profileId = 0;
		String winner = "none";
		
		for( int i=0; i<(this.board.snakes.length); i++)
		{
			if(this.board.snakes[i].currentScore > max)
			{
				max = this.board.snakes[i].currentScore;
				winner = this.players.get(i).userName;
				profileId = this.board.snakes[i].playerId;
			}
		}
		
		if(max < this.pointThreshold)
		{
			for(int i=0; i<this.board.snakes.length; i++)
			{
				if(this.board.snakes[i].state == true)
				{
					 winner = this.players.get(i).userName;
					 profileId = this.board.snakes[i].playerId;
				}
			}
			JLabel text = new JLabel(winner + " remporte la manche !");
		    text.setFont(new Font("Verdana",1,23));
		    setColor(text,profileId);
		    this.panelBoard.setBackground(Color.BLACK);
		    this.panelBoard.add(text,BorderLayout.CENTER);
		}
		else
		{
		    GridLayout display = new GridLayout(board.snakes.length+1,board.snakes.length+1);
		    FlowLayout LColumn = new FlowLayout(FlowLayout.CENTER);
		    FlowLayout RColumn = new FlowLayout(FlowLayout.CENTER);
		    this.panelBoard.setLayout(display);
		    
	    	JPanel panel = new JPanel();
	    	panel.setBackground(Color.black);
	    	panel.setLayout(LColumn);
		    JLabel text = new JLabel("Le gagnant de la partie est :  ");
		    text.setFont(new Font("Verdana",1,23));
		    setColor(text,profileId);
		    panel.add(text)	;
		    
	    	JPanel panel2 = new JPanel();
	    	panel2.setBackground(Color.black);
	    	panel2.setLayout(RColumn);
		    JLabel text2 = new JLabel(winner);
		    text2.setFont(new Font("Verdana",1,27));
		    setColor(text2,profileId);
		    panel2.add(text2);
		    
		    this.panelBoard.add(panel);
		    this.panelBoard.add(panel2);
		    displayScores(this.pointThreshold, this.board.snakes.length, board, players, LColumn, RColumn, this.panelBoard);
		}
	}
	
	public static void setpanelScore(Board board, int pointThreshold, List<Profile> players, JPanel panelScore)
	{
	    int size = players.size();
	    int goal = pointThreshold;
	    GridLayout display = new GridLayout(size+1, size+1);
	    FlowLayout LColumn = new FlowLayout(FlowLayout.LEFT);
	    FlowLayout RColumn = new FlowLayout(FlowLayout.CENTER);
	    panelScore.setLayout(display);
	    
    	JPanel panel = new JPanel();
    	panel.setBackground(Color.black);
    	panel.setLayout(LColumn);
    	String maxScoreLabel = "FIRST TO REACH ";
	    JLabel text = new JLabel(maxScoreLabel);
	    text.setFont(new Font("Arial",Font.BOLD,18));
	    text.setForeground(Color.white);
	    panel.add(text)	;
	    
    	JPanel panel2 = new JPanel();
    	panel2.setBackground(Color.black);
    	panel2.setLayout(RColumn);
    	String Goal = String.valueOf(goal);
	    JLabel text2 = new JLabel(Goal);
	    text2.setFont(new Font("Arial",Font.BOLD,26));
	    text2.setForeground(Color.white);
	    panel2.add(text2);
	    
	    panelScore.add(panel);
	    panelScore.add(panel2);
	    displayScores(goal, size, board, players, LColumn, RColumn, panelScore);

	}
	
	public static void setpanelBoard(Board board, JPanel panelBoard)
	{
		JPanel draw = new Draw(board.width, board.height, board);
		draw.setBackground(Color.black);
		panelBoard.add(draw);
		
	}
	
	public static void displayScores(int goal, int size, Board board, List<Profile> players, FlowLayout LColumn, FlowLayout RColumn, JPanel gridPanel)
	{
		for(int n=goal; n>0; n--)
		{
			for(int i=0; i<size; i++)
			{
		    	if(board.snakes[i].currentScore == n)
		    	{
			    	JPanel panelName = new JPanel();
			    	panelName.setBackground(Color.black);
			    	panelName.setLayout(LColumn);
			    	String playerName = (players.get(i)).userName;
			    	
			    	JLabel textName = new JLabel("  ➤  "+playerName);
			    	textName.setFont(new Font("Arial",Font.BOLD,17));
			    	setColor(textName,board.snakes[i].playerId);
			    	panelName.add(textName);
				   
			    	JPanel panelScore = new JPanel();
			    	panelScore.setBackground(Color.black);
			    	panelScore.setLayout(RColumn);
			    	String Score = String.valueOf(board.snakes[i].currentScore);
			    	
			    	JLabel textScore = new JLabel(Score);
			    	textScore.setFont(new Font("Arial",Font.BOLD,22));
			    	setColor(textScore,i);
			    	panelScore.add(textScore);
				   
				   
			    	gridPanel.add(panelName);
			    	gridPanel.add(panelScore);
		    	}
		    }
		}
	}
	
	public static void setColor(JLabel label, int playerId)
	{
		switch (playerId)
		{
			case 0:
				label.setForeground(Color.red);
				break;
			case 1:
				label.setForeground(Color.cyan);
				break;
			case 2:
				label.setForeground(Color.green);
				break;
			case 3:
				label.setForeground(Color.yellow);
				break;
			case 4:
				label.setForeground(Color.magenta);
				break;
			case 5:
				label.setForeground(Color.white);
				break;
			case 6:
				label.setForeground(Color.orange);
				break;
			case 7:
				label.setForeground(Color.pink);
				break;
		}
	}
			
}