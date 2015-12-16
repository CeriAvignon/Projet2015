package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;

public class MyGraphic implements GraphicDisplay {
Board b;
Panel panel= new Panel();
DefaultListModel dm =new DefaultListModel();
JList list=new JList();
List<Profile> players;
public void main()
{
	panel=new Panel();
}
@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel){
		// TODO Auto-generated method stub
		this.b=board;
		panel.setBackground(Color.BLACK);
		panel.setBounds(0, 0,b.width , b.height);
boardPanel.add(panel);
this.players=players;

	}

	@Override
	public void update() {
		String text="";
		int i =0;
		for(Profile j:players)
		{
		text+=j.score+ " "+j.userName;
		dm.set(i, text);
panel.setPosX(b.snakes[i].currentX);
panel.setPosY(b.snakes[i].currentY);
panel.setRayon((int)b.snakes[i].headRadius);
panel.setColor(Color.red);
panel.repaint((int)b.snakes[i].currentX,(int) b.snakes[i].currentY,(int) b.snakes[i].headRadius, (int) b.snakes[i].headRadius);
		}
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

	

}
