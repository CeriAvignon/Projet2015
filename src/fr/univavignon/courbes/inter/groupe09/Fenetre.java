package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * @author groupe09
 * Classe abstraite héritant de la classe JFrame qui nous permet de créer des fenetre  
 */
abstract class Fenetre extends JFrame 
{
	protected Container contenu;
	
	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	Fenetre(String titre, int x, int y) {
		// ramener la dimension de l'écran
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimEcran = tk.getScreenSize();
		int larg = dimEcran.width;
		int haut = dimEcran.height;
		setTitle(titre);
		// afficher la fenetre au millieu de l'ecran quelque que soit le taille de l'écrans
		setBounds((larg-x)/2,(haut-y)/2,x,y);
		contenu = getContentPane();
	}

}
