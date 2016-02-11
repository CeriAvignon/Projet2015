package fr.univavignon.courbes.inter.simpleimpl.client;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.inter.simpleimpl.AbstractConfigurationPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.network.ClientCommunication;
import fr.univavignon.courbes.network.simpleimpl.client.ClientCommunicationImpl;

/**
 * Classe permettant à l'utilisateur de spécifier les information de connexion
 * au serveur, pour configurer une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientGameServerConnectionPanel extends AbstractConfigurationPanel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Connexion au serveur";
	/** adresse IP par défaut */
	private static final String DEFAULT_IP = "192.168.0.1";
	/** Port par défaut */
	private static final String DEFAULT_PORT = "453";
	/** Couleur rouge pour l'état déconnecté */
	private static final Color COLOR_DISC = new Color(255, 69, 69);
	/** Couleur verte pour l'état connecté */
	private static final Color COLOR_CONN = new Color(10, 255, 128);
	
	/**
	 * Construit un nouveau panel chargé de connecter le client à son serveur.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public ClientGameServerConnectionPanel(MainWindow mainWindow)
	{	super(mainWindow,TITLE);
	}
	
	/** Saisie de l'adresse IP */
	private JTextField ipTextField;
	/** Saisie du port */
	private JTextField portTextField;
	/** Bouton de connexion */
	private JButton connectButton;
	/** Label indiquand le statut de la connexion */
	private JLabel statusLabel;
	/** Indique si on est actuellement connecté ou pas */
	private boolean connected;
	
	@Override
	protected void initContent()
	{	connected = mainWindow.clientCom!=null;
		
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
		
		JLabel ipLabel = new JLabel("Adresse IP : ");
		panel.add(ipLabel);
		
		if(connected)
		{	String ipStr = mainWindow.clientCom.getIp();
			ipTextField = new JTextField(ipStr);
		
		}
		else
			ipTextField = new JTextField(DEFAULT_IP);
		dim = new Dimension((int)(winDim.width*0.4),height);
		ipTextField.setPreferredSize(dim);
		ipTextField.setMaximumSize(dim);
		ipTextField.setMinimumSize(dim);
		panel.add(ipTextField);
		
		panel.add(Box.createGlue());
		
		JLabel portLabel = new JLabel("Port : ");
		panel.add(portLabel);
		
		if(connected)
		{	String portStr = Integer.toString(mainWindow.clientCom.getPort());
			portTextField = new JTextField(portStr);
		}
		else
			portTextField = new JTextField(DEFAULT_PORT);
		dim = new Dimension((int)(winDim.width*0.2),height);
		portTextField.setPreferredSize(dim);
		portTextField.setMaximumSize(dim);
		portTextField.setMinimumSize(dim);
		panel.add(portTextField);
		
		panel.add(Box.createGlue());

		connectButton = new JButton("Connexion");
		dim = new Dimension((int)(winDim.width*0.1),height);
		connectButton.setPreferredSize(dim);
		connectButton.setMaximumSize(dim);
		connectButton.setMinimumSize(dim);
		connectButton.addActionListener(this);
		panel.add(connectButton);

		panel.add(Box.createGlue());
		
		statusLabel = new JLabel("N/A");
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension((int)(winDim.width*0.1),height);
		statusLabel.setPreferredSize(dim);
		statusLabel.setMaximumSize(dim);
		statusLabel.setMinimumSize(dim);
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		statusLabel.setBorder(border);
		statusLabel.setOpaque(true);
		panel.add(statusLabel);
		
		updatePanel();
		
		add(panel);
	}
	
	/**
	 * Met à jour les composants du panel en fonction de l'état
	 * de la connexion.
	 */
	private void updatePanel()
	{	if(connected)
		{	ipTextField.setEnabled(false);
			portTextField.setEnabled(false);
			connectButton.setText("Déconnexion");
			statusLabel.setBackground(COLOR_CONN);
			statusLabel.setText("Connecté");
		}
		else
		{	ipTextField.setEnabled(true);
			portTextField.setEnabled(true);
			connectButton.setText("Connexion");
			statusLabel.setBackground(COLOR_DISC);
			statusLabel.setText("Déconnecté");
		}
	}
	
	/**
	 * Tente de se connecter au serveur.
	 */
	private void connect()
	{	ClientCommunication clientCom = new ClientCommunicationImpl();
		mainWindow.clientCom = clientCom;
		clientCom.setErrorHandler(mainWindow);
		
		String ipStr = ipTextField.getText();
		clientCom.setIp(ipStr);
		
		String portStr = portTextField.getText();
		int port = Integer.parseInt(portStr);
		clientCom.setPort(port);
		
		// d'abord on se connecte
		connected = clientCom.launchClient();
		// en cas de succès, on envoie le joueur sélectionné
		if(connected)
		{	// on vérifie si le serveur accepte bien le joueur du client
//			Player player = mainWindow.clientPlayer;
//			boolean accepted = mainWindow.clientCom.addProfile(player.profile);	TODO pour le débug de l'IU
			
//			if(!accepted)
//			{	JOptionPane.showMessageDialog(mainWindow, 
//					"<html>Le serveur a rejeté votre candidature, ce qui signifie généralement qu'il ne reste" +
//					"<br/>pas de place libre dans cette partie. Il est aussi possible que le profil sélectionné"
//					+ "<br/>soit déjà pris. Vous pouvez soit réessayer, soit chercher une autre partie.</html>");
//			}
		}		
		updatePanel();
	}
	
	/**
	 * Déconnexion du serveur courant.
	 */
	private void disconnect()
	{	mainWindow.clientCom.closeClient();
		connected = false;
		
		updatePanel();
		
		mainWindow.clientCom = null;	//TODO penser à faire la même chose à la fin de la partie. c'est aussi vrai pour le serveur.
	}
	
	@Override
	protected void nextStep()
	{	if(connected)
			mainWindow.displayPanel(PanelName.CLIENT_GAME_WAIT);
		else
		{	JOptionPane.showMessageDialog(mainWindow, 
				"<html>Vous devez d'abord vous connecter à un serveur,<br/>"
				+ "avant de commencer la partie.</html>");
		}
	}


	@Override
	protected void previousStep()
	{	if(connected)
			disconnect();
		mainWindow.displayPanel(PanelName.CLIENT_GAME_PLAYER_SELECTION);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{	super.actionPerformed(e);
		
		if(e.getSource()==connectButton)
		{	if(connected)
				disconnect();
			else
				connect();
		}
	}
}
