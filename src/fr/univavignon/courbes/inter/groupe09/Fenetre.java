package fr.univavignon.courbes.inter.groupe09;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


abstract class Fenetre extends JFrame 
{
	protected Container contenu;
	
	Fenetre(String titre, int x, int y) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dimEcran = tk.getScreenSize();
		int larg = dimEcran.width;
		int haut = dimEcran.height;
		setTitle(titre);
		// afficher la fenetre au millieu de l'ecran
		setBounds((larg-x)/2,(haut-y)/2,x,y);
		contenu = getContentPane();
	}

}
