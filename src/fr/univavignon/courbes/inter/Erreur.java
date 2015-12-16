package fr.univavignon.courbes.inter;

import fr.univavignon.courbes.inter.ErrorHandler;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Erreur extends JFrame implements ErrorHandler {
	public void displayError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);
	}
}