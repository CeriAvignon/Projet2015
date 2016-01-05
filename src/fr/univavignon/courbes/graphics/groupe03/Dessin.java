package fr.univavignon.courbes.graphics.groupe03;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Item;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;

/**
 * @author matrouf
 *
 */
public  class Dessin extends JFrame
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -3766523850539626461L;
	/**
	 * 
	 */
	private Board board;

	/**
	 * @param X
	 * @param Y
	 * @param board
	 */
	Dessin(int X, int Y, Board board) {
	        setPreferredSize(new Dimension(X, Y));
	        this.board = board;
	    }
	
	
	 public Dessin() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * @param id
	 * @return
	 */
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
	
	
	/**
	 * @param g
	 */
	
	
	


	/**
	 * @param g
	 */
	
	
	
	public static Color getColorItem(Item a) {
    	Color color = Color.black;
        switch (a)
        {
  			case USER_SPEED:
  				color = Color.red; 
  		    break;
  			case USER_SLOW:
  				color = Color.blue; 
  		    break;
  			case USER_BIG_HOLE:
  				color = Color.green; 
  		    break;
  			case OTHERS_SPEED:
  				color = Color.white; 
  		    break;
  			case OTHERS_THICK:
  				color = Color.darkGray; 
  		    break;
  			case OTHERS_SLOW:
  				color = Color.magenta; 
  		    break;
  			case OTHERS_REVERSE:
  				color = Color.pink; 
  		    break;
  			case COLLECTIVE_THREE_CIRCLES:
  				color = Color.orange; 
  		    break;
  			case COLLECTIVE_TRAVERSE_WALL:
  				color = Color.lightGray; 
  		    break;
  			case COLLECTIVE_ERASER:
  				color = Color.lightGray; 
  		    break;
  		} 
        return color;
    }
	/**
	 * @param g
	 */
	public void affichage(Graphics g) {
		// TODO Auto-generated method stub
		// g.drawString(String.valueOf(6), 5, 15);
		//super.paintComponents(g);
			for(int i = 0; i < board.snakes.length; i++)
	        {
	        	g.setColor(getColor(board.snakes[i].playerId));
	        	//int x = (int)Math.round(board.snakes[i].headRadius);
	        	//g.drawString(String.valueOf(x), 0, 0);
	        	g.fillOval(board.snakes[i].currentX, board.snakes[i].currentY, 1, 1);
	        	
	        }
			
			for (Map.Entry<Position, Integer> entry : board.snakesMap.entrySet())
			{
				g.setColor(getColor(entry.getValue()));	
				//int x = (int)Math.round(board.snakes[entry.getValue()].headRadius);
				g.fillRect(entry.getKey().x, entry.getKey().y, 1, 1);
			}
			
			//g.drawString(String.valueOf(6), 5, 15);
			
			
			for (Map.Entry<Position, Item> entre : board.itemsMap.entrySet())
			{
				g.setColor(getColorItem(entre.getValue()));	
				
				//int x = (int)Math.round(board.items[entry.getValue()].headRadius);;
				g.fillOval(entre.getKey().x, entre.getKey().y, 40, 40);
				g.setColor(Color.white);
				g.setFont(new Font("TimesRoman",Font.PLAIN,25));
				g.drawString("SU", entre.getKey().x+2, entre.getKey().y+30);
			}
	}


	public void affichage_fin(Graphics g) {
		// TODO Auto-generated method stub
		// affichage du serpent
        this.affichage(g);
     
        
            String str = "game over";
            g.setColor(Color.RED);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
            FontMetrics fm = g.getFontMetrics();
            int x = (g.getClipBounds().width - fm.stringWidth(str)) / 2;
            int y = (g.getClipBounds().height / 2) + fm.getMaxDescent();
            g.drawString(str, x, y);
      
     // affichage du niveau
        g.setColor(Color.BLUE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        
       
		
	}


	  

}