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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.ClientProfileHandler;
import fr.univavignon.courbes.inter.simpleimpl.AbstractConfigurationPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerConfigPanel;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerSelectionPanel;
import fr.univavignon.courbes.network.ClientCommunication;

/**
 * Panel permettant d'afficher côté client les joueurs actuellement sélectionnés
 * pour participer à une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientGameWaitPanel extends AbstractConfigurationPanel implements RemotePlayerSelectionPanel, ClientProfileHandler
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Joueurs sélectionnés";
	
	/**
	 * Crée et initialise le panel permettant d'afficher
	 * les joueurs sélectionnés.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public ClientGameWaitPanel(MainWindow mainWindow)
	{	super(mainWindow, TITLE);
	}

	@Override
	public void init(String title)
	{	ClientCommunication clientCom = mainWindow.clientCom;
		clientCom.setProfileHandler(this);
		Player player = mainWindow.clientPlayer;
		mainWindow.clientCom.sendProfile(player.profile);
//		clientCom.requestProfiles();
		
		super.init(title);
		nextButton.setEnabled(false);
	}
	
	/** Liste des profils sélectionnés */
	public List<RemotePlayerConfigPanel> selectedProfiles;
	/** Panel affichant les profils sélectionnés */
	public JPanel playersPanel;
	/** Largeur des noms */
	private int nameWidth;
	/** Largeur du rang ELO */
	private int eloWidth;
	
	@Override
	public MainWindow getMainWindow()
	{	return mainWindow;
	}

	@Override
	protected void initContent()
	{	selectedProfiles = new ArrayList<RemotePlayerConfigPanel>();
		
		initDimensions();
		initPlayersPanel();
	}
	
	/**
	 * Initialise différentes valeurs utilisées pour la mise en forme.
	 */
	protected void initDimensions()
	{	Dimension winDim = mainWindow.getPreferredSize();
		nameWidth = (int)(winDim.width*0.7);
		eloWidth = (int)(winDim.width*0.2);
	}
	
	@Override
	public int getNameWidth()
	{	return nameWidth;
	}

	@Override
	public int getEloWidth()
	{	return eloWidth;
	}

	@Override
	public int getKickWidth()
	{	return 0;
	}

	/**
	 * Initialise le panel contenant les composants
	 * relatifs aux profils sélectionnés.
	 */
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

		JLabel rightLabel = new JLabel("Classement ELO");
		rightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(eloWidth,height);
		rightLabel.setPreferredSize(dim);
		rightLabel.setMaximumSize(dim);
		rightLabel.setMinimumSize(dim);
		rightLabel.setBorder(border);
		titlePanel.add(rightLabel);
	}
	
	@Override
	public int getSelectedProfileCount()
	{	return selectedProfiles.size();
	}

	@Override
	protected void previousStep()
	{	mainWindow.clientCom.closeClient();
		mainWindow.clientCom.setProfileHandler(null);
		mainWindow.displayPanel(PanelName.CLIENT_GAME_CONNECTION);
	}
	
	@Override
	protected void nextStep()
	{	// rien à faire ici : l'enchaînement est controlé par le serveur
	}

	@Override
	public void updateProfiles(final Profile[] profiles)
	{	final ClientGameWaitPanel panel = this;
		SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	// on met à jour le panel de manière à refléter les derniers changements
				for(int i=0;i<profiles.length;i++)
				{	RemotePlayerConfigPanel rpcp;
					if(selectedProfiles.size()>i)
						rpcp = selectedProfiles.get(i);
					else
					{	rpcp = new RemotePlayerConfigPanel(panel,false);
						selectedProfiles.add(rpcp);
						playersPanel.add(rpcp);
					}
					rpcp.setProfile(profiles[i]);
				}
				
				validate();
				repaint();
			}
	    });
	}

	@Override
	public void startGame(final Round round)
	{	//ce thread est exécuté plus tard par Swing, ce qui rend cette méthode non-bloquante pour le moteur réseau
		SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	// on adapte les joueurs à la configuration du client
				Profile clientProfile = mainWindow.clientPlayer.profile;
				Player players[] = round.players;
				for(Player player: players)
				{	if(player.profile.equals(clientProfile))
					{	player.leftKey = mainWindow.clientPlayer.leftKey;
						player.rightKey = mainWindow.clientPlayer.rightKey;
						player.local = true;
					}
					else
					{	player.leftKey = -1;
						player.rightKey = -1;
						player.local = false;
					}
				}
				
				// on bascule sur le panel de jeu
				mainWindow.clientCom.setProfileHandler(null);
				mainWindow.clientPlayer = null;
				mainWindow.currentRound = round;
				mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAY);
			}
	    });
	}

	@Override
	public void gotKicked()
	{	//ce thread est exécuté plus tard par Swing, ce qui rend cette méthode non-bloquante pour le moteur réseau
		SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	JOptionPane.showMessageDialog(mainWindow, 
					"<html>Le serveur a rejeté le joueur sélectionné, soit personnellement,"
					+ "<br/>soit parce que son profil était déjà utilisé dans cette partie.</html>");
				previousStep();
			}
	    });
	}

	@Override
	public void kickPlayer(int playerId)
	{	// pas utilisé ici
	}

	@Override
	public void connectionLost()
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	JOptionPane.showMessageDialog(mainWindow, 
					"<html>La connexion avec le serveur a été perdue.</html>");
				previousStep();
			}
	    });
	}
}
