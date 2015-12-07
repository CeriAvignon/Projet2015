package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.util.List;

import javax.swing.JList;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;

public class MyGraphic implements GraphicDisplay {
Board b;
Panel panel= new Panel();
JList list=new JList();
	@Override
	public void init(Board board, int pointThreshold, List<Profile> players) {
		// TODO Auto-generated method stub
		this.b=board;
		panel.setBackground(Color.BLACK);
		panel.setBounds(100, 49,b.width , b.height);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

}
