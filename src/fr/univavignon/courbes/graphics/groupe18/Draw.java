package fr.univavignon.courbes.graphics.groupe18;
import java.awt.Dimension;
import java.awt.Color; 
import java.awt.Font;
import java.awt.Graphics;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.Math;

import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Item;

 
/**
 * La classe Draw sert a dessiner les snakes sur l'aire de jeu.
 * 
 * @author Antoine Letourneur
 * @author Axel Clerici
 * @author Vincent Labatut
 */
public class Draw extends JPanel
{	/** Numéro de série (pour {@code Serializable})	 */
	private static final long serialVersionUID = 1L;
	
    /** Tableau contenant les couleurs associées à chaque numéro de joueur */
    private static final Color[] PLAYER_COLORS = {Color.red,Color.blue,Color.green,Color.cyan,Color.orange,Color.magenta,Color.pink,Color.white,Color.black};

	/** Aire de jeu */
	private Board board;
	
    /**
     * Constructeur de la classe Draw pour construire le panel où la fonction paintComponent dessinera.
     * 
     * @param X
     * 		Taille en X de la matrice 
     * @param Y
     * 		Taille en Y de la matrice 
     * @param board
     * 		L'aire de jeu
     */
    Draw(int X, int Y, Board board) {
        setPreferredSize(new Dimension(X, Y));
        this.board = board;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for(int i = 0; i < board.snakes.length; i++) {
        	g.setColor(PLAYER_COLORS[board.snakes[i].playerId]);
        	int x = (int)Math.round(board.snakes[i].headRadius);
        	g.drawOval(board.snakes[i].currentX-(x/2), board.snakes[i].currentY-(x/2), x, x);
        	g.fillOval(board.snakes[i].currentX-(x/2), board.snakes[i].currentY-(x/2), x, x);
        	
        }
        
        Map<Position, Integer> mapSnake = new ConcurrentHashMap<Position,Integer>();
		mapSnake = board.snakesMap;
		
		for (ConcurrentHashMap.Entry<Position, Integer> entry : mapSnake.entrySet())
		{
			g.setColor(PLAYER_COLORS[entry.getValue()]);
			g.fillRect(entry.getKey().x, entry.getKey().y, 1, 1);
		}
		
        Map<Position, Item> mapItems = new ConcurrentHashMap<Position,Item>();
		mapItems = board.itemsMap;
		
		for (ConcurrentHashMap.Entry<Position, Item> entry : mapItems.entrySet())
		{
//			switch(entry.getValue())
//			{
//				case USER_SPEED:
//					g.setColor(Color.green);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 45)); 
//		        	g.drawString("⬆",entry.getKey().x+1,entry.getKey().y+36);
//					break;
//					
//				case USER_SLOW:
//					g.setColor(Color.green);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 45)); 
//		        	g.drawString("⬇",entry.getKey().x+2,entry.getKey().y+36);
//					break;
//					
//				case USER_BIG_HOLE:
//					g.setColor(Color.green);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.BOLD, 35)); 
//		        	g.drawString("H",entry.getKey().x+6,entry.getKey().y+34);
//					break;
//					
//				case OTHERS_SPEED:
//					g.setColor(Color.red);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 45)); 
//		        	g.drawString("⬆",entry.getKey().x+1,entry.getKey().y+36);
//					break;
//					
//				case OTHERS_THICK:
//					g.setColor(Color.red);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Times New Roman", Font.PLAIN, 45)); 
//		        	g.drawString("⬌",entry.getKey().x+1,entry.getKey().y+36);
//					break;
//					
//				case OTHERS_SLOW:
//					g.setColor(Color.red);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 45)); 
//		        	g.drawString("⬇",entry.getKey().x+2,entry.getKey().y+36);
//					break;
//					
//				case OTHERS_REVERSE:
//					g.setColor(Color.red);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 42)); 
//		        	g.drawString("⟲",entry.getKey().x,entry.getKey().y+36);
//					break;
//				case COLLECTIVE_THREE_CIRCLES:
//					g.setColor(Color.blue);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 42)); 
//		        	g.drawString("+",entry.getKey().x+4,entry.getKey().y+34);
//					break;
//				case COLLECTIVE_TRAVERSE_WALL:
//					g.setColor(Color.blue);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 40)); 
//		        	g.drawString("\u2605",entry.getKey().x+3,entry.getKey().y+34);
//					break;
//				case COLLECTIVE_ERASER:
//					g.setColor(Color.blue);
//					g.drawOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.fillOval(entry.getKey().x,entry.getKey().y,40,40);
//		        	g.setColor(Color.white);
//		        	g.setFont(new Font("Arial", Font.PLAIN, 40)); 
//		        	g.drawString("x",entry.getKey().x+9,entry.getKey().y+32);
//					break;	
//	
//				default:				
//			}      	
		}       
    }          
}