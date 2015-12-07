package fr.univavignon.courbes.graphics.groupe23;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel{
	int posX=0;// position X du Snake
	int posY=0;// position Y du Snake
	Color color=Color.white; // couleur du snake
int rayon=6;// rayon du snake 
	public void  paint(Graphics g) // fonction que repaint appelle
{
		super.paintComponent(g);
g.setColor(color);
g.fillOval(posX, posY,rayon,rayon); 
}

	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
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
