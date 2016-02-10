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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;

/**
 * Panel représentant un joueur en cours de configuration.
 * Utilisé par un autre panel chargé de la configuration d'une partie.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientPlayerConfig extends JPanel implements ActionListener
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Texte utilisé pour le nom quand aucun joueur n'est sélectionné */
	private static final String EMPTY_NAME = "Emplacement libre";
	/** Texte utilisé pour le classement ELO quand aucun joueur n'est sélectionné */
	private static final String EMPTY_ELO = "-";
	
	/**
	 * Crée un panel chargé de représenter un joueur et
	 * sa configuration.
	 * 
	 * @param configPanel
	 * 		Panel de configuration contenant ce panel. 
	 */
	public ClientPlayerConfig(ServerGameRemotePlayerSelectionPanel configPanel)
	{	this.configPanel = configPanel;
		
		initPlayer();
		initPanel();
	}

	/** Panel contenant ce panel */
	private ServerGameRemotePlayerSelectionPanel configPanel;
	/** Joueur sélectionné */
	public Player player;
	/** Label affichant le nom du joueur */
	public JLabel nameLabel;
	/** Label affichant le rang ELO du joueur */
	public JLabel eloLabel;
	/** Bouton permettant de refuser le joueur */
	public JButton kickButton;
	
	/**
	 * Initialise les composants contenus dans ce panel.
	 */
	private void initPanel()
	{	BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);
		int height = 30;
		Dimension dim;
		
		nameLabel = new JLabel(EMPTY_NAME);
		dim = new Dimension(configPanel.nameWidth,height);
		nameLabel.setPreferredSize(dim);
		nameLabel.setMaximumSize(dim);
		nameLabel.setMinimumSize(dim);
		nameLabel.setBackground(Constants.PLAYER_COLORS[player.playerId]);
		add(nameLabel);
		
		add(Box.createHorizontalGlue());
		
		eloLabel = new JLabel(EMPTY_ELO);
		dim = new Dimension(configPanel.eloWidth,height);
		eloLabel.setPreferredSize(dim);
		eloLabel.setMaximumSize(dim);
		eloLabel.setMinimumSize(dim);
		eloLabel.setBackground(Constants.PLAYER_COLORS[player.playerId]);
		add(eloLabel);
		
		add(Box.createHorizontalGlue());
		
		kickButton = new JButton("Rejet");
		kickButton.addActionListener(this);
		dim = new Dimension(configPanel.kickWidth,height);
		kickButton.setPreferredSize(dim);
		kickButton.setMaximumSize(dim);
		kickButton.setMinimumSize(dim);
		kickButton.setBackground(Constants.PLAYER_COLORS[player.playerId]);
		add(this.kickButton);
		kickButton.setEnabled(false);
	}

	/**
	 * Initialise le joueur courant.
	 */
	private void initPlayer()
	{	player = new Player();
		player.profile = null;
		
		Round round = configPanel.mainWindow.currentRound;
		int index = round.players.length + configPanel.selectedProfiles.size();
		player.playerId = index;
		
		player.local = false;
		
		player.totalScore = 0;
		player.roundScore = 0;
		
		player.leftKey = -1;
		player.rightKey = -1;
	}
	
	/**
	 * Remplace le profil actuellement sélectionné par
	 * celui passé en paramètre, en indiquant le classement ELO.
	 * 
	 * @param profile
	 * 		Nouveau profil du joueur.
	 */
	public void setPlayer(Profile profile)
	{	player.profile = profile;
		nameLabel.setText(profile.userName);
		eloLabel.setText(Integer.toString(profile.eloRank));
		kickButton.setEnabled(true);
	}
	
	/**
	 * Détermine si cette position est disponible.
	 * Autrement dit : un client peut il utiliser ce slot,
	 * ou bien est-il déjà pris ?
	 * 
	 * @return
	 * 		{@code true} ssi la position est libre.
	 */
	public boolean isAvailable()
	{	boolean result = player.profile==null;
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==kickButton)
		{	if(e.getSource()==kickButton)
			{	player.profile = null;
				nameLabel.setText(EMPTY_NAME);
				eloLabel.setText(EMPTY_ELO);
				kickButton.setEnabled(false);
			}
		}
	}
}
