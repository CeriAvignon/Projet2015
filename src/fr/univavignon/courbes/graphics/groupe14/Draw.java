package fr.univavignon.courbes.graphics.groupe14;
import java.awt.Dimension;
import java.awt.Color; 
import java.awt.Graphics;
import java.util.Map;
import java.lang.Math;
import javax.swing.JPanel;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Position;

/**
 * Draw dessine les Snakes sur le Board
 * @author1 uapv1400768 Drissi Remi
 * @author2 uapv1402587 Girardon Valentin
 */
public class Draw extends JPanel
{
	/**
	 * Numéro unique (pour {@code Serializable})
	 */
	private static final long serialVersionUID = 1L;
	
    /**
	 * Plateau de jeu
	 */
	private Board board;
	
	/**
     * Constructeur de classe : paintComponent pourra dessiner sur le panel créé
     * @param X
     * 		Largeur de la matrice 
     * @param Y
     * 		Hauteur de la matrice 
     * @param board
     * 		Plateau de jeu
     */
    Draw(int X, int Y, Board board)
    {

        setPreferredSize(new Dimension(X, Y));
        

        this.board = board;
    }

    /**
     * Renvoie la couleur associée d'un joueur en fonction de son playerId
     * @param id
     * 		L'id du joueur
     * @return Un objet Color, qui contient la couleur correspondante au joueur
     */
    public static Color getColor(int id)
    {
    	Color color = Color.black;
        switch (id)
        {
  			case 0:
  				color = Color.red; 
  				break;
  			case 1:
  				color = Color.cyan; 
  				break;
  			case 2:
  				color = Color.green; 
  				break;
  			case 3:
  				color = Color.yellow; 
  				break;
  			case 4:
  				color = Color.magenta; 
  				break;
  			case 5:
  				color = Color.white; 
  				break;
  			case 6:
  				color = Color.orange; 
  				break;
  			case 7:
  				color = Color.pink; 
  				break;
  			case 8:
  				color = Color.lightGray; 
  				break;
  		} 
        return color;
    }
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        for(int i = 0; i < board.snakes.length; i++)
        {
        	g.setColor(getColor(board.snakes[i].playerId));
        	int x = (int)Math.round(board.snakes[i].headRadius);
        	g.fillOval(board.snakes[i].currentX, board.snakes[i].currentY, x, x);
        	g.drawOval(board.snakes[i].currentX, board.snakes[i].currentY, x, x);
        }
        
		for (Map.Entry<Position, Integer> entry : board.snakesMap.entrySet())
		{
			g.setColor(getColor(entry.getValue()));
			g.fillRect(entry.getKey().x, entry.getKey().y, 1, 1);
		}
    }          
}