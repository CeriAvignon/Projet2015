package fr.univavignon.courbes.inter.groupe12;
package fr.univavignon.courbes.inter.groupe12;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * @author groupe12
 * Classe abstraite héritant de la classe JFrame qui nous permet de créer des fenetre  
 */
abstract class Fenetre extends JFrame 
{
	protected Container contenu;
	

	/**
	 * @param titre
	 * @param x
	 * @param y
	 */
	
	Fenetre(String titre, int x, int y) {
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dimension = toolkit.getScreenSize();
		int largeur = dimension.width;
		int hauteur = dimension.height;
		setTitle(titre);
		
		setBounds((largeur-x)/2,(hauteur-y)/2,x,y);
		contenu = getContentPane();
	}

}


