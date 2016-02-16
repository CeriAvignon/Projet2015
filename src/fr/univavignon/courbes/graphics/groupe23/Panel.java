package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.common.Snake;
/**
 * @author Omar
 *Cette classe va nous permettre de redefinir la classe Jpanel
 *ainsi nous permettre de le redessiner a notre maniere
 */
public class Panel extends JPanel{
	
	Color couleur[]={Color.blue,Color.red,Color.orange,Color.green,Color.yellow};
	Board board;
public Panel(Board board)
{
	this.board=board;

}
@Override
	public synchronized void  paint(Graphics g)
	/* fonction que repaint appelle */
{
		super.paintComponent(g);
		
		 for(Map.Entry<Position, Integer> mapentry : board.snakesMap.entrySet())
		 {
			g.setColor(couleur[mapentry.getValue()]);
			g.fillOval(mapentry.getKey().x, mapentry.getKey().y,(int)board.snakes[mapentry.getValue()].headRadius,(int)board.snakes[mapentry.getValue()].headRadius);
		// System.out.println(mapentry.getValue());
		 }
		
		 }


}
