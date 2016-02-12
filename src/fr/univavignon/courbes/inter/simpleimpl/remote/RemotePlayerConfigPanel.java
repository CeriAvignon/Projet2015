package fr.univavignon.courbes.inter.simpleimpl.remote;

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

/**
 * Panel représentant un joueur en cours de configuration.
 * Utilisé par un autre panel chargé de la configuration d'une partie.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class RemotePlayerConfigPanel extends JPanel implements ActionListener
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
	 * @param withButton
	 * 		Indique s'il faut inclure un bouton pour rejeter le joueur (serveur) ou pas (client). 
	 */
	public RemotePlayerConfigPanel(RemotePlayerSelectionPanel configPanel, boolean withButton)
	{	this.configPanel = configPanel;
		this.withButton = withButton;
		
		initPlayer();
		initPanel();
	}

	/**
	 * Crée un panel chargé de représenter un joueur et
	 * sa configuration.
	 * 
	 * @param configPanel
	 * 		Panel de configuration contenant ce panel.
	 * @param withButton
	 * 		Indique s'il faut inclure un bouton pour rejeter le joueur (serveur) ou pas (client). 
	 * @param player 
	 * 		Le joueur à recopier dans ce nouvel objet.
	 */
	public RemotePlayerConfigPanel(RemotePlayerSelectionPanel configPanel, boolean withButton, Player player)
	{	this.configPanel = configPanel;
		this.withButton = withButton;
		
		this.player = player;
		initPanel();
		setProfile(player.profile);
	}
	
	/** Panel contenant ce panel */
	private RemotePlayerSelectionPanel configPanel;
	/** Joueur sélectionné */
	public Player player;
	/** Label affichant le nom du joueur */
	public JLabel nameLabel;
	/** Label affichant le rang ELO du joueur */
	public JLabel eloLabel;
	/** Bouton permettant de refuser le joueur */
	public JButton kickButton;
	/** Indique s'il faut mettre un bouton de kick ou pas dans ce panel */
	private boolean withButton;
	
	/**
	 * Initialise les composants contenus dans ce panel.
	 */
	private void initPanel()
	{	BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);
		int height = 30;
		Dimension dim;
		
		nameLabel = new JLabel(EMPTY_NAME);
		if(withButton)
			dim = new Dimension(configPanel.getNameWidth(),height);
		else
			dim = new Dimension(configPanel.getNameWidth()+configPanel.getKickWidth(),height);
		nameLabel.setPreferredSize(dim);
		nameLabel.setMaximumSize(dim);
		nameLabel.setMinimumSize(dim);
		nameLabel.setBackground(Constants.PLAYER_COLORS[player.playerId]);
		nameLabel.setOpaque(true);
		add(nameLabel);
		
		add(Box.createHorizontalGlue());
		
		eloLabel = new JLabel(EMPTY_ELO);
		dim = new Dimension(configPanel.getEloWidth(),height);
		eloLabel.setPreferredSize(dim);
		eloLabel.setMaximumSize(dim);
		eloLabel.setMinimumSize(dim);
		eloLabel.setBackground(Constants.PLAYER_COLORS[player.playerId]);
		eloLabel.setOpaque(true);
		add(eloLabel);
		
		if(withButton)
		{	add(Box.createHorizontalGlue());
			
			kickButton = new JButton("Rejet");
			kickButton.addActionListener(this);
			dim = new Dimension(configPanel.getKickWidth(),height);
			kickButton.setPreferredSize(dim);
			kickButton.setMaximumSize(dim);
			kickButton.setMinimumSize(dim);
			kickButton.setBackground(Constants.PLAYER_COLORS[player.playerId]);
			add(this.kickButton);
			kickButton.setEnabled(false);
		}
	}

	/**
	 * Initialise le joueur courant.
	 */
	private void initPlayer()
	{	player = new Player();
		player.profile = null;
		
//		Round round = configPanel.getMainWindow().currentRound;
//		int index = round.players.length + configPanel.getSelectedProfileCount();
		int index = configPanel.getSelectedProfileCount();
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
	public void setProfile(Profile profile)
	{	player.profile = profile;
		if(profile==null)
		{	nameLabel.setText(EMPTY_NAME);
			eloLabel.setText(EMPTY_ELO);
			if(withButton)
				kickButton.setEnabled(false);
		}
		else
		{	nameLabel.setText(profile.userName);
			eloLabel.setText(Integer.toString(profile.eloRank));
		if(withButton)
			kickButton.setEnabled(true);
		}
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
				configPanel.kickPlayer(player.playerId);
			}
		}
	}
}
