package fr.univavignon.courbes.graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel{
	int posX=0;
	int posY=0;
	Color color=Color.white;
public void  paint(Graphics g)
{
		super.paintComponent(g);
g.setColor(color);
g.fillOval(posX, posY,6 ,6);
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

}
