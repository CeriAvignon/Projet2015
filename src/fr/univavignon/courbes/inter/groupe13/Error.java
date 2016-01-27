package fr.univavignon.courbes.inter.groupe13;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.univavignon.courbes.inter.ErrorHandler;
/*
 * Classe qui implemente l'interface ErrorHandler.
 */
public class Error extends JFrame implements ErrorHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Affiche une fenêtre d'erreur.
	 * @param errorMessage Message d'erreur à afficher.
	 */
	@Override
	public void displayError(String errorMessage) {
		
		JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);

	}
}