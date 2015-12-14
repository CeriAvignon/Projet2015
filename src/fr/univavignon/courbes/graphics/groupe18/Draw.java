package fr.univavignon.courbes.graphics.groupe18;
import java.awt.Dimension;
import java.awt.Color; 
import java.awt.Graphics;
import java.util.Map;
import java.lang.Math;
import javax.swing.JPanel;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;

 
public class Draw extends JPanel { 
	private static final long serialVersionUID = 1L;
	private Board board;
	
    Draw(int X, int Y, Board board) {
        setPreferredSize(new Dimension(X, Y));
        this.board = board;
    }

    public static Color getColor(int id) {
    	Color color = Color.black;
        switch (id)
        {
  			case 0:
  				color = Color.red; 
  		    break;
  			case 1:
  				color = Color.blue; 
  		    break;
  			case 2:
  				color = Color.green; 
  		    break;
  			case 3:
  				color = Color.white; 
  		    break;
  			case 4:
  				color = Color.cyan; 
  		    break;
  			case 5:
  				color = Color.magenta; 
  		    break;
  			case 6:
  				color = Color.pink; 
  		    break;
  			case 7:
  				color = Color.orange; 
  		    break;
  			case 8:
  				color = Color.lightGray; 
  		    break;
  		} 
        return color;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
		for (Map.Entry<Position, Integer> entry : board.snakesMap.entrySet())
		{
			g.setColor(getColor(entry.getValue()));
			int x = (int)Math.round(board.snakes[entry.getValue()].headRadius);
			g.fillOval(entry.getKey().x, entry.getKey().y, x, x);
		}
        
    }          
}