package fr.univavignon.courbes.inter.simpleimpl.server;

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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.simpleimpl.AbstractPlayerSelectionPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;

/**
 * Panel permettant de sélectionner les joueurs distants au serveur participant à une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerGameRemotePlayerSelectionPanel extends AbstractPlayerSelectionPanel<ClientPlayerConfig>
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	/** Nombre minimal de joueurs distants recquis pour la partie */
	private static final int MIN_PLYR_NBR = 1;
	/** Texte associé à la combobox */
	private static final String COMBO_TEXT = "Nombre de joueurs distants : ";
	
	/**
	 * Crée et initialise le panel permettant de sélectionner
	 * les joueurs locaux au serveur participant à une partie locale.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public ServerGameRemotePlayerSelectionPanel(MainWindow mainWindow)
	{	super(mainWindow);
	}
	
	/** Largeur des noms */
	public int nameWidth;
	/** Largeur des IP */
	public int ipWidth;
	/** Largeur des boutons kick */
	public int kickWidth;
	
	/**
	 * Initialisation des composants de l'interface graphique.
	 */
	@Override
	protected void init()
	{	Dimension winDim = mainWindow.getPreferredSize();
		nameWidth = (int)(winDim.width*0.5);
		ipWidth = (int)(winDim.width*0.2);
		kickWidth = (int)(winDim.width*0.2);
		
		super.init();
	}
	
	@Override
	protected void initPlayersPanel()
	{	playersPanel = new JPanel();
		BoxLayout layout = new BoxLayout(playersPanel, BoxLayout.PAGE_AXIS);
		playersPanel.setLayout(layout);
		add(playersPanel);
		
		JPanel titlePanel = new JPanel();
		layout = new BoxLayout(titlePanel, BoxLayout.LINE_AXIS);
		titlePanel.setLayout(layout);
		playersPanel.add(titlePanel);
		int height = 20;
		Dimension dim;
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		JLabel playerLabel = new JLabel("Profil");
		playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(nameWidth,height);
		playerLabel.setPreferredSize(dim);
		playerLabel.setMaximumSize(dim);
		playerLabel.setMinimumSize(dim);
		playerLabel.setBorder(border);
		titlePanel.add(playerLabel);

		titlePanel.add(Box.createHorizontalGlue());

		JLabel rightLabel = new JLabel("Adresse IP");
		rightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(ipWidth,height);
		rightLabel.setPreferredSize(dim);
		rightLabel.setMaximumSize(dim);
		rightLabel.setMinimumSize(dim);
		rightLabel.setBorder(border);
		titlePanel.add(rightLabel);

		titlePanel.add(Box.createHorizontalGlue());

		JLabel leftLabel = new JLabel("Rejeter");
		leftLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(kickWidth,height);
		leftLabel.setPreferredSize(dim);
		leftLabel.setMaximumSize(dim);
		leftLabel.setMinimumSize(dim);
		leftLabel.setBorder(border);
		titlePanel.add(leftLabel);
		
		for(int i=0;i<Math.max(1,getMinPlayerNbr());i++)
			addProfile();
	}
	
	@Override
	public int getMinPlayerNbr()
	{	return MIN_PLYR_NBR;
	}
	
	@Override
	protected int getMaxPlayerNbr()
	{	Round round = mainWindow.currentRound;
		Player players[] = round.players;
		int result = Constants.MAX_PLAYER_NBR - players.length;
		return result;
	}

	@Override
	protected String getComboText()
	{	return COMBO_TEXT;
	}
	
	@Override
	protected void addProfile()
	{	ClientPlayerConfig lps = new ClientPlayerConfig(this);
		selectedProfiles.add(lps);
		playersPanel.add(lps);
		
		validate();
		repaint();
	}
	
	@Override
	protected boolean checkConfiguration()
	{	boolean isReady = true;
		
		// on compare toutes les paires de profils sélectionnés
		int i1 = 0;
		while(i1<selectedProfiles.size() && isReady)
		{	ClientPlayerConfig cpc1 = selectedProfiles.get(i1);
			Player player1 = cpc1.player;
			int left1 = player1.leftKey;
			int right1 = player1.rightKey;
			Profile profile1 = player1.profile;
			
			if(left1==right1 || profile1==null || left1==-1 || right1==-1)
				isReady = false;
			
			else
			{	int i2 = i1 + 1;
				while(i2<selectedProfiles.size() && isReady)
				{	ClientPlayerConfig cpc2 = selectedProfiles.get(i2);
					Player player2 = cpc2.player;
					int left2 = player2.leftKey;
					int right2 = player2.rightKey;
					Profile profile2 = player2.profile;
					
					if(profile2==null || profile1.equals(profile2)
						|| left1==left2 || left1==right2 || right1==left2 || right1==right2)
						isReady = false;
					else
						i2++;
				}
				i1++;
			}
		}
		
		return isReady;
	}
	
	@Override
	protected Round initRound()
	{	Round result = new Round();
		
		// initialisation des joueurs
		result.players = new Player[selectedProfiles.size()];
		for(int i=0;i<selectedProfiles.size();i++)
		{	ClientPlayerConfig cpc = selectedProfiles.get(i);
			Player player = cpc.player;
			player.playerId = i;
			result.players[i] = player;
		}
		
		result.pointLimit = Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(selectedProfiles.size());
		return result;
	}
	
	@Override
	protected void nextStep()
	{	if(checkConfiguration())
		{	Round round = initRound();
			mainWindow.currentRound = round;
			mainWindow.displayPanel(PanelName.SERVER_GAME_PLAY);
		}
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
				"<br>- tous les profils sont définis et différents, et que" +
				"<br>- toutes les commandes sont définies et différentes.</html>");
		}
	}
	
	@Override
	protected void previousStep()
	{	mainWindow.displayPanel(PanelName.SERVER_GAME_LOCAL_PLAYERS);
	}
	
	@Override
	protected void comboboxChanged()
	{	int oldPlayerNbr = selectedProfiles.size();
		int newPlayerNbr = (int) playerNbrCombo.getSelectedItem();
		
		if(oldPlayerNbr<newPlayerNbr)
		{	for(int i=oldPlayerNbr;i<newPlayerNbr;i++)
				addProfile();
		}
		else
		{	for(int i=oldPlayerNbr;i>newPlayerNbr;i--)
			{	selectedProfiles.remove(i-1);
				playersPanel.remove(i);
				playersPanel.repaint();
			}
		}
	}
}
