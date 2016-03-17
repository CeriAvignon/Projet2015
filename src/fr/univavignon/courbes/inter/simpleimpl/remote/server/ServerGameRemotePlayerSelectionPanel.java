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

import java.awt.Dimension;

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

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.ServerProfileHandler;
import fr.univavignon.courbes.inter.simpleimpl.AbstractPlayerSelectionPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager.NetEngineImpl;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerConfigPanel;
import fr.univavignon.courbes.inter.simpleimpl.remote.RemotePlayerSelectionPanel;
import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.kryonet.ServerCommunicationKryonetImpl;
import fr.univavignon.courbes.network.simpleimpl.server.ServerCommunicationImpl;

/**
 * Panel permettant de sélectionner les joueurs distants au serveur participant à une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerGameRemotePlayerSelectionPanel extends AbstractPlayerSelectionPanel<RemotePlayerConfigPanel> implements ServerProfileHandler, RemotePlayerSelectionPanel
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Sélection des joueurs distants";
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
	{	super(mainWindow,TITLE);
		
		initServer();
	}
	
	/** Largeur des noms */
	private int nameWidth;
	/** Largeur du rang ELO */
	private int eloWidth;
	/** Largeur des boutons kick */
	private int kickWidth;
	/** Moteur réseau */
	private ServerCommunication serverCom;
	
	@Override
	public MainWindow getMainWindow()
	{	return mainWindow;
	}
	
	@Override
	protected void initDimensions()
	{	Dimension winDim = mainWindow.getPreferredSize();
		nameWidth = (int)(winDim.width*0.5);
		eloWidth = (int)(winDim.width*0.2);
		kickWidth = (int)(winDim.width*0.2);
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
	{	return kickWidth;
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

		JLabel rightLabel = new JLabel("Classement ELO");
		rightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(eloWidth,height);
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
		
		// ajout des joueurs locaux (pour info)
		Player[] localPlayers = mainWindow.currentRound.players;
		for(Player localPlayer: localPlayers)
		{	RemotePlayerConfigPanel lps = new RemotePlayerConfigPanel(this,true,localPlayer);
			lps.kickButton.setEnabled(false);
			lps.kickButton.setText("Local");
			selectedProfiles.add(lps);
			playersPanel.add(lps);
		}
		
		// ajout des slots pour les joueurs distants
		int remoteNbr = Math.max(1,getMinPlayerNbr());
		for(int i=0;i<remoteNbr;i++)
			addProfile();
	}

	/**
	 * Initialise la partie serveur du moteur réseau
	 */
	private void initServer()
	{	NetEngineImpl netEngineImpl = SettingsManager.getNetEngineImpl();
		switch(netEngineImpl)
		{	case KRYONET:
				serverCom = new ServerCommunicationKryonetImpl();
				break;
			case SOCKET:
				serverCom = new ServerCommunicationImpl();
				break;
		}
		
		serverCom.setErrorHandler(mainWindow);
		serverCom.setProfileHandler(this);
		serverCom.launchServer();
		int localNbr = selectedProfiles.size() - mainWindow.currentRound.players.length;
		serverCom.setClientNumber(localNbr);
		mainWindow.serverCom = serverCom;
	}
	
	@Override
	public int getMinPlayerNbr()
	{	Round round = mainWindow.currentRound;
		Player players[] = round.players;
		int result = MIN_PLYR_NBR;
		if(players.length==0)
			result++;
		return result;
	}
	
	@Override
	protected int getMaxPlayerNbr()
	{	Round round = mainWindow.currentRound;
		Player players[] = round.players;
		int result = Constants.MAX_PLAYER_NBR - players.length;
		return result;
	}

	@Override
	public int getSelectedProfileCount()
	{	return selectedProfiles.size();
	}

	@Override
	protected String getComboText()
	{	return COMBO_TEXT;
	}
	
	@Override
	protected void addProfile()
	{	RemotePlayerConfigPanel lps = new RemotePlayerConfigPanel(this,true);
		selectedProfiles.add(lps);
		playersPanel.add(lps);
		
		validate();
		repaint();
	}
	
	@Override
	public void kickPlayer(int playerId)
	{	// on vire le profil sélectionné pour ce numéro
		int index = playerId - mainWindow.currentRound.players.length;
		serverCom.kickClient(index);
		
		// on prévient les clients restants
		Profile profiles[] = getAllPlayers();
		serverCom.sendProfiles(profiles);
	}
	
	@Override
	protected boolean checkConfiguration()
	{	boolean isReady = true;
		
		// on compare toutes les paires de profils sélectionnés
		int i1 = 0;
		while(i1<selectedProfiles.size() && isReady)
		{	RemotePlayerConfigPanel cpc1 = selectedProfiles.get(i1);
			Player player1 = cpc1.player;
			Profile profile1 = player1.profile;
			
			if(profile1==null)
				isReady = false;
			
			else
			{	int i2 = i1 + 1;
				while(i2<selectedProfiles.size() && isReady)
				{	RemotePlayerConfigPanel cpc2 = selectedProfiles.get(i2);
					Player player2 = cpc2.player;
					Profile profile2 = player2.profile;
					
					if(profile2==null || profile1.equals(profile2))
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
	{	Round result = new Round(SettingsManager.getBoardWidth(),SettingsManager.getBoardHeight());
		
		// initialisation des joueurs
		result.players = new Player[selectedProfiles.size()];
		for(int i=0;i<selectedProfiles.size();i++)
		{	RemotePlayerConfigPanel cpc = selectedProfiles.get(i);
			Player player = cpc.player;
			player.playerId = i;
			result.players[i] = player;
		}
		
		// initialisation de la limite de points
//		result.pointLimit = Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(selectedProfiles.size());
		
		return result;
	}
	
	@Override
	protected synchronized void nextStep()
	{	if(checkConfiguration())
		{	mainWindow.serverCom.setProfileHandler(null);
			Round round = initRound();
			mainWindow.currentRound = round;
			mainWindow.serverCom.sendRound(round);
			// on laisse un peu de temps aux clients
			// (pas très propre, vaudrait mieux faire une vraie synchro) 
			try
			{	Thread.sleep(500);
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
			mainWindow.displayPanel(PanelName.SERVER_GAME_PLAY);
//			mainWindow.serverCom.sendRound(round);
		}
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Tous les joueurs distants n'ont pas été sélectionnés.<br/>"
				+ "Ajoutez de nouveaux joueurs ou diminuez le nombre de joueurs distants.</html>");
		}
	}
	
	@Override
	protected synchronized void previousStep()
	{	serverCom.closeServer();
		mainWindow.serverCom = null;
		mainWindow.displayPanel(PanelName.SERVER_GAME_LOCAL_PLAYER_SELECTION);
	}
	
	/**
	 * Construit un tableau contenant tous les profils actuellement
	 * sélectionnés, que ce soient les locaux ou les distants.
	 * Parmi ces derniers, les positions pas encore remplies sont
	 * représentées par des valeurs {@code null}.
	 * 
	 * @return
	 * 		Un tableau contenant tous les joueurs sélectionnés jusqu'à présent.
	 */
	private Profile[] getAllPlayers()
	{	Profile[] result = new Profile[selectedProfiles.size()];
		
		for(int i=0;i<selectedProfiles.size();i++)
		{	RemotePlayerConfigPanel cpc = selectedProfiles.get(i);
			Profile profile = cpc.player.profile;
			result[i] = profile;
		}
		
		return result;
	}
	
	@Override
	protected synchronized void comboboxChanged()
	{	int localPlayerNbr = mainWindow.currentRound.players.length;
		int oldPlayerNbr = selectedProfiles.size();
		int newPlayerNbr = localPlayerNbr + (int) playerNbrCombo.getSelectedItem();
		
		// on met à jour les composants graphiques
		if(oldPlayerNbr<newPlayerNbr)
		{	for(int i=oldPlayerNbr;i<newPlayerNbr;i++)
				addProfile();
		}
		else
		{	for(int i=oldPlayerNbr;i>newPlayerNbr;i--)
			{	selectedProfiles.remove(i-1);
				playersPanel.remove(i);
				playersPanel.repaint();
				mainWindow.serverCom.kickClient(i-localPlayerNbr-1);
			}
		}
		
		// on met à jour le Moteur Réseau
		serverCom.setClientNumber(selectedProfiles.size()-localPlayerNbr);
		
		// on prévient les clients
		Profile profiles[] = getAllPlayers();
		serverCom.sendProfiles(profiles);
	}

	@Override
	public synchronized void fetchProfile(final Profile profile, final int index)
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	int localPlayers = mainWindow.currentRound.players.length;
				int playerIndex = index+localPlayers;
				// on vérifie si le profil n'est pas déjà sélectionné
				boolean found = false;
				int i = 0;
				while(i<selectedProfiles.size() && !found)
				{	if(i!=playerIndex)
					{	RemotePlayerConfigPanel rpcp = selectedProfiles.get(i);
						Player player0 = rpcp.player;
						Profile profile0 = player0.profile;
						found = profile0!=null && profile.equals(profile0);
					}
					i++;
				}

				// auquel cas on le refuse
				if(found)
					mainWindow.serverCom.kickClient(index);
				
				// si pas déjà présent, on tente de l'ajouter
				else
				{	RemotePlayerConfigPanel cpc = selectedProfiles.get(playerIndex);
					if(cpc.isAvailable())
					{	// on rajoute le joueur à l'emplacement associé au client
						cpc.setProfile(profile);
						
						// on prévient les clients du changement
						Profile profiles[] = getAllPlayers();
						serverCom.sendProfiles(profiles);
						
						// on rafraichit
						validate();
						repaint();
					}
				}
			}
	    });
	}

	@Override
	public void connectionLost(final int index)
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	// on vire le profil sélectionné pour ce numéro
				int playerId =  index + mainWindow.currentRound.players.length;
				RemotePlayerConfigPanel rpcp = selectedProfiles.get(playerId);
				rpcp.setProfile(null);
				
				// on prévient les clients restants
				Profile profiles[] = getAllPlayers();
				serverCom.sendProfiles(profiles);
			}
		   });
	}
}
