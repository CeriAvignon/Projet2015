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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.univavignon.courbes.inter.simpleimpl.AbstractConfigurationPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;

/**
 * Classe permettant à l'utilisateur de spécifier les information de connexion
 * au serveur, pour configurer une partie réseau.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public abstract class AbstractConnectionPanel extends AbstractConfigurationPanel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construit un nouveau panel chargé de connecter le client à son serveur.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 * @param title
	 * 		Titre de ce panel.
	 */
	public AbstractConnectionPanel(MainWindow mainWindow, String title)
	{	super(mainWindow,title);
	}
	
	/** Saisie de l'adresse IP */
	protected JTextField ipTextField;
	/** Saisie du port */
	protected JTextField portTextField;
	
	@Override
	protected void initContent()
	{	Dimension winDim = mainWindow.getPreferredSize();
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
		
		ipTextField = new JTextField(getDefaultIp());
		dim = new Dimension((int)(winDim.width*0.4),height);
		ipTextField.setPreferredSize(dim);
		ipTextField.setMaximumSize(dim);
		ipTextField.setMinimumSize(dim);
		panel.add(ipTextField);
		
		panel.add(Box.createGlue());
		
		JLabel portLabel = new JLabel("Port : ");
		panel.add(portLabel);
		
		portTextField = new JTextField(Integer.toString(getDefaultPort()));
		dim = new Dimension((int)(winDim.width*0.2),height);
		portTextField.setPreferredSize(dim);
		portTextField.setMaximumSize(dim);
		portTextField.setMinimumSize(dim);
		panel.add(portTextField);
		
		add(panel);
	}
	
	/**
	 * Renvoie l'adresse IP par défaut.
	 * 
	 * @return
	 * 		Adresse IP par défaut.
	 */
	public abstract String getDefaultIp();
	
	/**
	 * Renvoie le port TCP par défaut.
	 * 
	 * @return
	 * 		Port TCP par défaut.
	 */
	public abstract int getDefaultPort();
}
