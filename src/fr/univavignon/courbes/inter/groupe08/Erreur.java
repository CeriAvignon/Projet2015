package fr.univavignon.courbes.inter.groupe08;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.univavignon.courbes.inter.groupe08.ErrorHandler;



public class Erreur extends JFrame implements ErrorHandler {
	public void displayError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);
	}
}