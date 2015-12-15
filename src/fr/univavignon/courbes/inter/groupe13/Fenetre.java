package fr.univavignon.courbes.inter.groupe13;

import java.awt.*;
import javax.swing.*;


abstract class Fenetre extends JFrame 
{
	public Container container;
	
	Fenetre(int largeur, int hauteur) {
		setTitle("Atchung Die Kurv");
	    this.setSize(largeur, hauteur);
	    this.setLocationRelativeTo(null);
	    container = getContentPane();
	}

}

	
