package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.graphics.GraphicDisplay;


/**
 * @author Omar
 *Cette classe va nous permettre de pouvoir definir les fonctions de l'interface graphique
 */
public class MyGraphic implements GraphicDisplay {
Board b;
/**
 * Liste des Joeurs et leur points
 */
DefaultListModel<String> dm =new DefaultListModel();
JList list=new JList(dm);
List<Profile> players;
Panel panel;

@Override
	public void init(Board board, int pointThreshold, List<Profile> players, JPanel boardPanel, JPanel scorePanel){
		// TODO Auto-generated method stub
	panel=new Panel(board);	
	this.b=board;
		boardPanel.setLayout(null);
		panel.setBackground(Color.BLACK);
		panel.setBounds(10, 11,b.width , b.height);
boardPanel.add(panel);
scorePanel.setLayout(null);
this.players=players;
list.setBounds(10, 11, 90, 209);
scorePanel.add(list);
String text="";
int i=0;
for(Profile j:players)
{
	text=pointThreshold+ " "+j.userName;
	dm.add(i,text);	
i++;
}
	}

	@Override
	public void update() {
		String text;
		int i =0;
		for(Profile j:players)
		{
		text=j.score+ " "+j.userName;
		dm.set(i, text);
		i++;
		
		}
		panel.repaint();
		
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		String text;
		int i=0;
		for(Profile j:players)
		{
		text="+"+j.score+ " "+j.userName;
		dm.set(i++, text);

		}
		
	}

}
