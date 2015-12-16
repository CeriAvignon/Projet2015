package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Point;

import javax.swing.JPanel;

import fr.univavignon.courbes.graphics.groupe23.MyGraphic.Repaint;

public class Panel extends JPanel{
	int posX=0;// position X du Snake
	int posY=0;// position Y du Snake
	Color color=Color.black; // couleur du snake
int rayon;// rayon du snake 
	public synchronized void  paint(Graphics g) // fonction que repaint appelle
{
		super.paintComponent(g);
		 g.setColor(color);
		 g.fillOval(posX, posY,rayon,rayon);
}

	
	public synchronized void repaint(int posX,int posY,int rayon,Color color,Point prec) {
	
		this.posX = posX;
		this.color = color;
		this.rayon=rayon;
		this.posY = posY;
		super.repaint((prec.getX()<posX)?(int)(posX+rayon/2):(int)(posX-rayon/2),(prec.getY()<posY)?(int)(posY+rayon/2):(int)(posY-rayon/2),(int) rayon+5, (int) rayon+5);
	
	}
	
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}	
public int getRayon()
{
	return rayon;
}
public void setRayon(int rayon)
{
	this.rayon=rayon;
}

}
