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

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
public class ClientGamePlayerSelectionPanel extends AbstractLocalPlayerSelectionPanel implements ItemListener
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
	/** Title du panel */
	private static final String BOX_LABEL = "Connexion directe : ";
	
	/**
	 * Crée et initialise le panel permettant de sélectionner
	 * les participants à une partie locale.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public ClientGamePlayerSelectionPanel(MainWindow mainWindow)
	{	super(mainWindow,TITLE);
	}
	
	/** Check box pour la partie publique/privée */
	private JCheckBox publicBox;

	@Override
	protected void initContent()
	{	super.initContent();
		
		// on désactive le combo
		playerNbrCombo.setEnabled(false);
		comboLabel.setEnabled(false);
		
		// on sort les couleurs
		for(LocalPlayerConfigPanel lpcp: selectedProfiles)
			lpcp.removeColor();
		
		// on rajoute la check box
		Dimension winDim = mainWindow.getPreferredSize();
		Dimension dim;
		int height = 30;
		
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		dim = new Dimension((int)(winDim.width),height);
		panel.setPreferredSize(dim);
		panel.setMaximumSize(dim);
		panel.setMinimumSize(dim);
		
		JLabel publicLabel = new JLabel(BOX_LABEL);
		panel.add(publicLabel);
		
		publicBox = new JCheckBox();
		publicBox.setSelected(true);
		publicBox.addItemListener(this);
		panel.add(publicBox);
		
		add(panel);
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
	{		Player player = selectedProfiles.get(0).player;
			mainWindow.clientPlayer = player;
			// connexion directe
			if(publicBox.isSelected())
				mainWindow.displayPanel(PanelName.CLIENT_GAME_CONNECTION);
			// connexion via le central
			else
			{	System.out.println("Fonctionnalité pas encore implémentée");
				// TODO à compléter avec le traitement relatif au serveur central :
				// il faut afficher un panel qui va se connecter au central et faire le traitement approprié
				//mainWindow.displayPanel(PanelName.XXXXXXXXXX);
			}
		}
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Les données du joueur local ne sont pas correctement remplies. Vérifiez que" +
				"<br/>les deux commandes sont définies et différentes.</html>");
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==publicBox)
		{	// rien de spécial à faire
		}
	}
}
