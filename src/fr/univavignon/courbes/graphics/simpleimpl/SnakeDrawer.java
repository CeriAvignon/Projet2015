package fr.univavignon.courbes.graphics.simpleimpl;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.BasicStroke;
import java.awt.Color; 
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Board.State;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.Snake;

/**
 * Contient les méthodes permettant de dessiner les serpents.
 * <br/>
 * Pour aller plus vite, on ne redessine pas tous les pixels des serpents
 * un par un à chaque itération. À la place, on maintient une image contenant
 * tous les pixels précédents, et on y rajoute les nouveaux pixels à chaque
 * itération. Puis, on dessine cette image sur l'aire de jeu pour obtenir le 
 * serpent. La recopie d'une telle image est beaucoup plus rapide que le tracé
 * de chaque pixel pris séparément.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class SnakeDrawer
{	
	/**
	 * Initialise les données nécessaires au tracé des snakes.
	 * 
	 * @param playerNbr
	 * 		Nombre de joueurs à afficher.
	 * @param width
	 * 		Largeur de l'aire de jeu en pixels.
	 * @param height
	 * 		Hauteur de l'aire de jeu en pixels.
	 */
	public SnakeDrawer(int playerNbr, int width, int height)
	{	boardWidth = width;
		boardHeight = height;
		images = new Image[playerNbr];
		resetImages();
		
		// TODO on pourrait aussi, par exemple, rajouter un tirage au sort des couleurs des joueurs.
	}
	
	/** images utilisées pour accélerer le tracer des serpents */
	private transient Image images[];
	/** Largeur de l'aire de jeu en pixels */
	private int boardWidth;
	/** Hauteur de l'aire de jeu en pixels */
	private int boardHeight;
	
	/**
	 * Trace tous les serpents présents dans l'aire de jeu.
	 * 
	 * @param round
	 * 		La manche en cours.
	 * @param g
	 * 		Objet graphique sur lequel on trace les serpents.
	 */
	public void drawSnakes(Round round, Graphics2D g)
	{	Board board = round.board;
		Snake snakes[] = board.snakes;
		
		// on boucle sur chaque joueur
		for(int playerId=0;playerId<snakes.length;playerId++)
		{	// on récupère la couleur du joueur dans la partie
			Snake snake = snakes[playerId];
			Color playerColor = Constants.PLAYER_COLORS[playerId];
			
			// si le client est déconnecté, on utilise la couleur spéciale pour tracer la trainée
			Color color;
			if(snake.connected)
				color = playerColor;
			else
				color = Constants.DISCO_PLAYER_COLOR;
			
//			// ancienne version : on dessine chaque pixel de la traine (lent)
//			g.setColor(playerColor);
//			for(Position position: ((PhysSnake)snake).oldTrail)
//				g.drawLine(position.x, position.y, position.x, position.y);
//			for(Position position: snake.newTrail)
//				g.drawLine(position.x, position.y, position.x, position.y);
			
			if(images==null)
			{	images = new Image[snakes.length];
				resetImages();
			}
			
			// on efface éventuellement l'image
			if(snakes[playerId].clearedTrail)
			{	int boardWidth = board.width;
				int boardHeight = board.height;
				images[playerId] = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
				snakes[playerId].clearedTrail = false;
			}
			// on complète le corps dans l'image
			Graphics2D gImg = (Graphics2D)images[playerId].getGraphics();
			gImg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gImg.setColor(color);
			// on boucle sur chaque position, i.e. chaque pixel
			for(Position position: snake.newTrail)
				gImg.drawLine(position.x, position.y, position.x, position.y);
			gImg.dispose();
			// on dessine l'image dans le panel
			g.drawImage(images[playerId],0,0,null);
			
			// pour debug : affiche en jaune la section de la traine du serpent utilisée pour détecter les collisions
//			g.setColor(Color.YELLOW);
//			MySnake s = (MySnake)snake;
//			for(Set<Position> disk: s.prevDisks)
//			{	for(Position position: disk)
//					g.drawLine(position.x, position.y, position.x, position.y);
//			}
//			g.setColor(playerColor);
			
			// si le client est éliminé, la tête doit être tracée en noir
			if(snake.eliminatedBy==null)
				color = playerColor;
			else
				color = new Color
				(	(int)Math.max(0, playerColor.getRed()-100),
					(int)Math.max(0, playerColor.getGreen()-100),
					(int)Math.max(0, playerColor.getBlue()-100));
			g.setColor(color);
			
			// on trace la tête du serpent
			g.fillOval(snake.currentX-snake.headRadius, snake.currentY-snake.headRadius,
				snake.headRadius*2, snake.headRadius*2);
			
			// on trace éventuellement la flèche de présentation
			if(board.state==State.PRESENTATION)
			{	g.setColor(playerColor);
				// flèche montrant la direction
				{	float dist = snake.headRadius+5;
					int x1 = (int)(snake.currentX + dist*Math.cos(snake.currentAngle));
					int y1 = (int)(snake.currentY + dist*Math.sin(snake.currentAngle));
					float length = 40;
					int x2 = (int)(snake.currentX + length*Math.cos(snake.currentAngle));
					int y2 = (int)(snake.currentY + length*Math.sin(snake.currentAngle));
					drawArrow(g, x1, y1, x2, y2, 10, 4);
				}
				// nom du joueur
				{	Player player = round.players[playerId];
					Profile profile = player.profile;
					String name = profile.userName;
					float dist = snake.headRadius+5;
					int xc = (int)(snake.currentX + dist*Math.cos(snake.currentAngle+Math.PI));
					int yc = (int)(snake.currentY + dist*Math.sin(snake.currentAngle+Math.PI));
					Font font = g.getFont();
				    FontMetrics metrics = g.getFontMetrics(font);
				    int textWidth = metrics.stringWidth(name);
				    int textHeight = metrics.getHeight();
				    int xt = xc - textWidth/2;
				    int yt = yc + textHeight/2;
					AffineTransform orig = g.getTransform();
					g.rotate(snake.currentAngle+Math.PI/2,xc,yc);
					g.drawString(name,xt,yt);
					g.setTransform(orig);
				}
			}
		}
	}
	
	/**
	 * Méthode chargée de tracer les auréoles représentant le temps d'effet restant pour
	 * chaque item affectant actuellement un serpent. Cette méthode doit être appelée après
	 * les autres, car les auréoles se superposent aux serpents et aux items parsement l'aire 
	 * de jeu.
	 *  
	 * @param board
	 * 		Aire de jeu à afficher.
	 * @param g
	 * 		Objet graphique sur lequel on trace les auréoles.
	 */
	public void drawAureolas(Board board, Graphics2D g)
	{	Snake snakes[] = board.snakes;
		
		// on change l'épaisseur du stylo
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(Constants.AUREOLA_THICKNESS));
		
		// on traite chaque serpent individuellement
		for(Snake snake: snakes)
		{	if(snake.eliminatedBy==null)
			{	// on récupère le centre des auréoles (i.e. centre de la tête)
				int x = snake.currentX;
				int y = snake.currentY;
				
				// initialisation de l'espace entre la tête et l'auréole à tracer
				int offset = snake.headRadius + Constants.AUREOLA_SPACE*2;
				
				// on traite séparément chaque item qui affecte actuellement le serpent
				// chacun sera représenté sous la forme d'une auréole
				for(ItemInstance item: snake.currentItems)
				{	// on détermine la couleur en fonction du type d'item
					ItemType type = item.type;
					Color color = type.color;
					g.setColor(color);
					
					// on trace un arc de cercle centré sur la tête
					// sa longueur est proportionnelle au temps d'effet restant à l'item 
					int radius = offset + Constants.AUREOLA_THICKNESS/2;
					double startAngle = Math.toDegrees(-snake.currentAngle); // angle de déplacement du serpent
					double extentAngle = 360*item.remainingTime/(double)type.duration;
					Arc2D arc = new Arc2D.Double(x-radius, y-radius, 2*radius, 2*radius, startAngle, extentAngle, Arc2D.OPEN);
					g.draw(arc);
					
					// on met à jour l'espace avec l'auréole suivante
					offset = offset + Constants.AUREOLA_THICKNESS + Constants.AUREOLA_SPACE;
				}
			}
		}
		
		// on rétablit l'épaisseur normale du stylo
		g.setStroke(oldStroke);
	}
	
	/**
     * Dessine une flèche reliant les deux points spécifiés.
     * Méthode prise sur <a href="http://stackoverflow.com/a/27461352/1254730">
     * StackOverflow</a>, écrite par user2447581.
     * 
     * @param g
     * 		Objet utilisé pour dessiner la flèche.
     * @param x1 
     * 		Abscisse du premier point.
     * @param y1
     * 		Ordonnée du premier point. 
     * @param x2
     * 		Abscisse du second point.
     * @param y2
     * 		Ordonnée du second point.
     * @param width
     * 		Largeur de la tête de la flèche.
     * @param height
     * 		Hauteur de la tête de la flèche.
     */
    private void drawArrow(Graphics2D g, int x1, int y1, int x2, int y2, int width, int height)
    {	int dx = x2 - x1;
    	int dy = y2 - y1;
		double D = Math.sqrt(dx*dx + dy*dy);
		double xm = D - width;
		double xn = xm;
		double ym = height;
		double yn = -height;
		double x;
		double sin = dy/D;
		double cos = dx/D;

		x = xm*cos - ym*sin + x1;
		ym = xm*sin + ym*cos + y1;
		xm = x;

		x = xn*cos - yn*sin + x1;
		yn = xn*sin + yn*cos + y1;
		xn = x;

		int[] xPoints = {x2, (int) xm, (int) xn};
		int[] yPoints = {y2, (int) ym, (int) yn};

		g.drawLine(x1, y1, x2, y2);
		g.fillPolygon(xPoints, yPoints, 3);
    }
    
	/**
	 * Nettoie les images des serpents en prévision de la prochaine manche.
	 */
	public void resetImages()
	{	for(int i=0;i<images.length;i++)
			images[i] = new BufferedImage(boardWidth, boardHeight, BufferedImage.TYPE_INT_ARGB);
	}
}
