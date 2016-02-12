package fr.univavignon.courbes.inter.simpleimpl.remote.client;

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

import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.local.AbstractLocalPlayerSelectionPanel;
import fr.univavignon.courbes.inter.simpleimpl.local.LocalPlayerConfigPanel;

/**
 * Panel permettant de sélectionner les joueurs participant à une partie réseau côté client.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientGamePlayerSelectionPanel extends AbstractLocalPlayerSelectionPanel
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Sélection du joueur";
	/** Nombre minimal de joueurs recquis pour la partie */
	private static final int MIN_PLYR_NBR = 1;
	/** Nombre maximal de joueurs autorisé pour la partie */
	private static final int MAX_PLYR_NBR = 1;
	/** Texte associé à la combobox */
	private static final String COMBO_TEXT = "Nombre de joueurs : ";
	
	/**
	 * Crée et initialise le panel permettant de sélectionner
	 * les participants à une partie locale.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public ClientGamePlayerSelectionPanel(MainWindow mainWindow)
	{	super(mainWindow,TITLE);
	
		// on désactive le combo
		playerNbrCombo.setEnabled(false);
		comboLabel.setEnabled(false);
		
		// on sort les couleurs
		for(LocalPlayerConfigPanel lpcp: selectedProfiles)
			lpcp.removeColor();
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
	{	mainWindow.clientPlayer = null;
		mainWindow.displayPanel(PanelName.MAIN_MENU);
	}
	
	@Override
	protected void nextStep()
	{	if(checkConfiguration())
		{	Player player = selectedProfiles.get(0).player;
			mainWindow.clientPlayer = player;
			mainWindow.displayPanel(PanelName.CLIENT_GAME_CONNECTION);
		}
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Les données du joueur local ne sont pas correctement remplies. Vérifiez que" +
				"<br/>les deux commandes sont définies et différentes.</html>");
		}
	}
}

//TODO à chaque écran, il faut traiter le cas où le serveur se déconnecte
//TODO pareil pour le serveur: traiter le cas où un client se déconnecte
