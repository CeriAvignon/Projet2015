package fr.univavignon.courbes.inter.groupe01;
import javax.swing.JFrame;

  
 

import fr.univavignon.courbes.inter.ErrorHandler;

public class Traitement_erreur extends JFrame implements ErrorHandler  {

	@Override
	public void displayError(String errorMessage) {
		// TODO Auto-generated method stub 
		
		System.err.println(errorMessage);
		JFrame fenetre = new JFrame();
		fenetre.setSize(100,80);
		fenetre.setTitle("error");
		//fenetre.setLocation(600, 400);
		fenetre.setLocationRelativeTo(null);
	    fenetre.setVisible(true);
		
	}
	public static void main(String[] args) {
		Traitement_erreur t = new Traitement_erreur();
		t.displayError("une erreur detecté");
	}

}
