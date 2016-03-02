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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager.NetEngineImpl;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;
import fr.univavignon.courbes.inter.simpleimpl.remote.AbstractConnectionPanel;
import fr.univavignon.courbes.network.ServerCommunication;
import fr.univavignon.courbes.network.kryonet.ServerCommunicationKryonetImpl;
import fr.univavignon.courbes.network.simpleimpl.server.ServerCommunicationImpl;

/**
 * Classe permettant à l'utilisateur de spécifier les information de connexion
 * au serveur, pour configurer une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerGamePortSelectionPanel extends AbstractConnectionPanel implements ItemListener
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Title du panel */
	private static final String TITLE = "Configuration réseau";
	/** Title du panel */
	private static final String BOX_LABEL = "Partie publique : ";
	
	/**
	 * Construit un nouveau panel chargé de connecter le client à son serveur.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public ServerGamePortSelectionPanel(MainWindow mainWindow)
	{	super(mainWindow,TITLE);
	}
	
	/** Check box pour la partie publique/privée */
	private JCheckBox publicBox;
	
	@Override
	protected void init(String title)
	{	// initialisation de la connexion
		ServerCommunication serverCom = mainWindow.serverCom;
		if(serverCom==null)
		{	NetEngineImpl netEngineImpl = SettingsManager.getNetEngineImpl();
			switch(netEngineImpl)
			{	case KRYONET:
					serverCom = new ServerCommunicationKryonetImpl();
					break;
				case SOCKET:
					serverCom = new ServerCommunicationImpl();
					break;
			}
			mainWindow.serverCom = serverCom;
			serverCom.setErrorHandler(mainWindow);
		}
		
		super.init(title);
		
	}
	
	@Override
	protected void initContent()
	{	super.initContent();
	
		// on désactive le text field
//		ipTextField.setEditable(false);
		ipTextField.setEnabled(false);
		
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
		publicBox.addItemListener(this);
		panel.add(publicBox);
		
		add(panel);
	}
	
	@Override
	protected void previousStep()
	{	mainWindow.serverCom = null;
		mainWindow.displayPanel(PanelName.MAIN_MENU);
	}

	@Override
	protected void nextStep()
	{	String portStr = portTextField.getText();
		int port = Integer.parseInt(portStr);
		SettingsManager.setLastPort(port);
		mainWindow.serverCom.setPort(port);
	
		mainWindow.displayPanel(PanelName.SERVER_GAME_LOCAL_PLAYER_SELECTION);
	}

	@Override
	public String getDefaultIp()
	{	String result = mainWindow.serverCom.getIp();
		return result;
	}

	@Override
	public int getDefaultPort()
	{	int result = SettingsManager.getLastPort();
		return result;
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{	if(e.getSource()==publicBox)
		{	System.out.println("Modification de la JCheckBox: "+publicBox.isSelected());
			// TODO à compléter avec le traitement relatif au serveur central...
		}
	}
}
