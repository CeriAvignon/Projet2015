package fr.univavignon.courbes.physics.groupe04;
import java.awt.*;
import javax.swing.*;
import fr.univavignon.courbes.graphics.*;
import java.util.*;

import java.util.List;
import fr.univavignon.courbes.common.*;


public class MyGraphics implements GraphicDisplay
{
	public Board graphicBoard;
	public int graphicPointThreshold;
	
	public JPanel jboard;
	public JPanel jscore;

	public MyPannel mjboard = new MyPannel();
	//MyPannel mjscore = new MyPannel();
	public JPanel mjscore = new JPanel();

	public MyGraphics()
	{
		this.graphicBoard = null;
		this.graphicPointThreshold = 0;
		//this.graphicPlayers = null;
	}

	public void	setMyGraphics(Board board, int pointThreshold,  JPanel boardPanel, JPanel scorePanel)
	{
		this.graphicBoard = board;
		this.graphicPointThreshold = pointThreshold;
		this.jboard = boardPanel;
		this.jscore = scorePanel;
	}

	public void init(Board board, int pointThreshold,  JPanel boardPanel, JPanel scorePanel)
	{
		this.setMyGraphics(board, pointThreshold, boardPanel, scorePanel);		
	}

	public void update()
	{
		mjboard.mapos.clear();
		mjboard.addMapHead(graphicBoard.snakesMap, graphicBoard.snakes);
		mjboard.repaint();
	}

	public void end()
	{
		int j=0;

		GridLayout gl = new GridLayout(graphicBoard.snakes.length,2);
		mjscore.setLayout(gl);
		JLabel cases[] = new JLabel[(graphicBoard.snakes.length)*2];

		for (int i = 0; i < graphicBoard.snakes.length; i++) 
		{
			
			System.out.println("1");
			cases[j] = new JLabel(String.valueOf(graphicBoard.snakes[i].playerId)); 
			mjscore.add(cases[j]);
			cases[j].setHorizontalAlignment(JLabel.CENTER);
			cases[j+1] = new JLabel(String.valueOf(graphicBoard.snakes[i].currentScore));
			mjscore.add(cases[j+1]);
			cases[j+1].setHorizontalAlignment(JLabel.CENTER);
			j=j+2;
		}

		//JLabel test = new JLabel("abcd");
		
		//mjscore.add(test);

	}

	public JPanel getPan()
	{
		return mjboard;
		//return mjscore;
	}

	@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel) {
		// TODO Auto-generated method stub
		
	}
}