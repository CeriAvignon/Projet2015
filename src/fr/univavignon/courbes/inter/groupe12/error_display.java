package fr.univavignon.courbes.inter.groupe12;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * @author uapv1601921
 *
 */

		public class error_display extends JFrame implements ErrorHandler {

			@Override
		    public void displayError(String errorMessage) {
				
			JOptionPane.showMessageDialog(this, errorMessage, "erreur!", JOptionPane.WARNING_MESSAGE);

			}

	}

