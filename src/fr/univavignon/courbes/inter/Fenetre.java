package fr.univavignon.courbes.inter;

import java.awt.*;
import javax.swing.*;


abstract class Fenetre extends JFrame{
	public Container container;
	
	/**
	 * @param largeur
	 * @param hauteur
	 */
	Fenetre(int largeur, int hauteur) {
	    this.setSize(largeur, hauteur);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
	    container = getContentPane();
	}

}
