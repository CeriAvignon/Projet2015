package fr.univavignon.courbes.inter.simpleimpl;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.univavignon.courbes.inter.ErrorHandler;

/**
 * Classe qui implemente l'interface ErrorHandler.
 * 
 * @author	L3 Info UAPV 2015-16
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