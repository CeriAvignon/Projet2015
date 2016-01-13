package fr.univavignon.courbes.inter.groupe13;

import java.awt.*;
import javax.swing.*;

/*
 * Classe abstraite permettant d'initialiser les fenètres.
 */
abstract class Window extends JFrame 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Container container;
	/*
	 * Initialise la largeur, la hauteur et le titre d'une fenêtre.
	 * @param width  Largeur de la fenêtre.
	 * @param height Hauteur de la fenêtre.
	 */
	Window(int width, int height) {
		setTitle("Atchung Die Kurv");
	    this.setSize(width, height);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	    container = getContentPane();
	}

}

	
