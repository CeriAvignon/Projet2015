package fr.univavignon.courbes.inter.groupe09;
import fr.univavignon.courbes.inter.ErrorHandler;
import javax.swing.*;
/**
 * @author groupe09
 * Classe implementant l'interface ErrorHanlder
 * contenu la méthode permettant d'afficher un message d'erreur à l'utilisateur 
 */
public class Error extends JFrame implements ErrorHandler {

	@Override
	public void displayError(String errorMessage) {
		
		JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);

	}

}	
