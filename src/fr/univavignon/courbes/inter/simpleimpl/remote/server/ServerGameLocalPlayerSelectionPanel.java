package fr.univavignon.courbes.inter.simpleimpl.remote.server;

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
import fr.univavignon.courbes.inter.simpleimpl.local.AbstractLocalPlayerSelectionPanel;

/**
 * Panel permettant de sélectionner les joueurs locaux au serveur participant à une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerGameLocalPlayerSelectionPanel extends AbstractLocalPlayerSelectionPanel
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Sélection des joueurs locaux";
	/** Nombre minimal de joueurs locaux recquis pour la partie */
	private static final int MIN_PLYR_NBR = 0;
	/** Nombre maximal de joueurs autorisé pour la partie */
	private static final int MAX_PLYR_NBR = Constants.MAX_PLAYER_NBR - 1;
	/** Texte associé à la combobox */
	private static final String COMBO_TEXT = "Nombre de joueurs locaux : ";
	
	/**
	 * Crée et initialise le panel permettant de sélectionner
	 * les joueurs locaux au serveur participant à une partie locale.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public ServerGameLocalPlayerSelectionPanel(MainWindow mainWindow)
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
	{	mainWindow.displayPanel(PanelName.SERVER_GAME_PORT_SELECTION);
	}
	
	@Override
	protected void nextStep()
	{	if(checkConfiguration())
		{	Round round = initRound();
			mainWindow.currentRound = round;
			mainWindow.displayPanel(PanelName.SERVER_GAME_REMOTE_PLAYER_SELECTION);
		}
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
				"<br/>- tous les profils sont définis et différents, et que" +
				"<br/>- toutes les commandes sont définies et différentes.</html>");
		}
	}
}
