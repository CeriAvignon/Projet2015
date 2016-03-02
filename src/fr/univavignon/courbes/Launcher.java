package fr.univavignon.courbes;

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
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;

/**
 * Classe principale chargée du lancement du jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Launcher extends JFrame
{	/** numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Méthode de lancement du jeu.
	 * 
	 * @param args
	 * 		Pas utilisés.
	 * 
	 * @throws Exception
	 * 		Une Exception queconque... 
	 */
	public static void main(String[] args) throws Exception
	{	// on charge les options du jeu qui avaient été stockées dans un fichier texte
		SettingsManager.loadSettings();
		
		// on change le look'n'feel Java
		setLookAndFeel();
		
		// on crée la fenêtre principale, qui lance le jeu
		new MainWindow();
	}
	
	/**
	 * Change l'aspect général de l'application (composants Swing).
	 * 
	 * @throws Exception
	 * 		Une Exception queconque... 
	 */
	private static void setLookAndFeel() throws Exception
	{	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		
		// change look and feel
		UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//		UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		try
		{	LookAndFeelInfo tab[] = UIManager.getInstalledLookAndFeels();
			int i = 0;
			boolean found = false;
			while(i<tab.length && !found)
			{	LookAndFeelInfo info = tab[i];
				if("Nimbus".equals(info.getName()))
				{	UIManager.setLookAndFeel(info.getClassName());
		            found = true;
		        }
				i++;
		    }
		} catch (Exception e)
		{	// Nimbus not available
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
	}
}

/*
 * TODO tâches à traiter :
 * - on pourrait reproduire l'animation d'apparition des items
 */
