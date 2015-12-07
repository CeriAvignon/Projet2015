package fr.univavignon.courbes.inter.groupe09;
import fr.univavignon.courbes.inter.ErrorHandler;
import javax.swing.*;
/**
 * @author 
 *
 */
public class Error extends JFrame implements ErrorHandler {

	@Override
	public void displayError(String errorMessage) {
		
		JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);

	}
}	
