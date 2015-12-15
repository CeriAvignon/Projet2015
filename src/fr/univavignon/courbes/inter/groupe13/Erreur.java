package fr.univavignon.courbes.inter.groupe13;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.univavignon.courbes.inter.ErrorHandler;

public class Erreur extends JFrame implements ErrorHandler {

	@Override
	public void displayError(String errorMessage) {
		
		JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);

	}
}