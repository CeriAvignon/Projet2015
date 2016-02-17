package fr.univavignon.courbes.inter.simpleimpl.local;

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

import javax.swing.JOptionPane;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;

/**
 * Panel permettant de sélectionner les joueurs participant à une partie locale.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class LocalGamePlayerSelectionPanel extends AbstractLocalPlayerSelectionPanel
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Sélection des joueurs";
	/** Nombre minimal de joueurs recquis pour la partie */
	private static final int MIN_PLYR_NBR = 2;
	/** Nombre maximal de joueurs autorisé pour la partie */
	private static final int MAX_PLYR_NBR = Constants.MAX_PLAYER_NBR;
	/** Texte associé à la combobox */
	private static final String COMBO_TEXT = "Nombre de joueurs : ";
	
	/**
	 * Crée et initialise le panel permettant de sélectionner
	 * les participants à une partie locale.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public LocalGamePlayerSelectionPanel(MainWindow mainWindow)
	{	super(mainWindow,TITLE);
	}
	
	@Override
	public int getMinPlayerNbr()
	{	return MIN_PLYR_NBR;
	}
	
	@Override
	protected int getMaxPlayerNbr()
	{	return MAX_PLYR_NBR;
	}

	@Override
	protected String getComboText()
	{	return COMBO_TEXT;
	}
	
	@Override
	protected void previousStep()
	{	mainWindow.displayPanel(PanelName.MAIN_MENU);
	}
	
	@Override
	protected void nextStep()
	{	if(checkConfiguration())
		{	Round round = initRound();
			mainWindow.currentRound = round;
			mainWindow.displayPanel(PanelName.LOCAL_GAME_PLAY);
		}
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
				"<br/>- tous les profils sont définis et différents, et que" +
				"<br/>- toutes les commandes sont définies et différentes.</html>");
		}
	}
}
