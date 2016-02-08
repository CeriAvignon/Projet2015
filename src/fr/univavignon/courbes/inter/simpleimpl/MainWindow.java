package fr.univavignon.courbes.inter.simpleimpl;

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
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.ErrorHandler;
import fr.univavignon.courbes.inter.simpleimpl.config.local.LocalGameConfigPanel;
import fr.univavignon.courbes.inter.simpleimpl.config.local.LocalGameRoundPanel;
import fr.univavignon.courbes.inter.simpleimpl.profiles.ProfileListPanel;

/**
 * Menu principal du jeu.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class MainWindow extends JFrame implements ErrorHandler, WindowListener
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée le menu principal et tous ses composants graphiques.
	 */
	public MainWindow()
	{	super();
		
		initWindow();
	}
	
	/** Panel correspondant au menu principal */
	private MainMenuPanel mainMenuPanel;
	/** Panel actuellement affiché */
	private JPanel currentPanel;
	/** Manche en cours de jeu */
	public Round currentRound;
	
	/**
	 * Initialise la fenêtre.
	 */
	private void initWindow()
	{	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	
		setTitle("Courbes");
		try
		{	String iconPath = "res/images/icon.png";
			File iconFile = new File(iconPath);
			Image img = ImageIO.read(iconFile);
			setIconImage(img);
		}
		catch(IOException e)
		{	e.printStackTrace();
		}
		
		Dimension dim = new Dimension(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setResizable(false);
		
		mainMenuPanel = new MainMenuPanel(this);
		currentPanel = mainMenuPanel;
		getContentPane().add(currentPanel);
		
		pack();
		setLocationRelativeTo(null);
		addWindowListener(this);
		setVisible(true);
	}
	
	/**
	 * Checks if saving is required,
	 * then close the window.
	 */
	public void closeWindow()
	{	// on tue éventuellement le processus en cours de jeu
		if(currentPanel instanceof LocalGameRoundPanel)
		{	LocalGameRoundPanel lgrp = (LocalGameRoundPanel)currentPanel;
			lgrp.stop();
		}
		
		// on ferme la fenêtre
		dispose();
		System.exit(0);
	}

	/**
	 * Type énuméré utilisé pour basculer d'un panel à un autre.
	 * 
	 * @author	L3 Info UAPV 2015-16
	 */
	public enum PanelName
	{	/** Menu principal */
		MAIN_MENU,
		/** Configuration d'une partie locale */
		LOCAL_GAME_CONFIG,
		/** Aire de jeu d'une partie locale */
		LOCAL_GAME_ROUND,
		/** Configuration d'une partie serveur */
		SERVER_GAME_CONFIG,
		/** Configueration d'une partie client */
		CLIENT_GAME_CONFIG,
		/** Liste des profils */
		PROFILE_LIST;
	}
	
	/**
	 * Change le panel actuellement affiché dans cette fenêtre.
	 * 
	 * @param panelName
	 * 		Nom du panel à afficher dans cette fenêtre.
	 */
	public void displayPanel(PanelName panelName)
	{	getContentPane().remove(currentPanel);
		switch(panelName)
		{	case MAIN_MENU:
				currentPanel = mainMenuPanel;
				break;
			case LOCAL_GAME_CONFIG:
				currentPanel = new LocalGameConfigPanel(this);
				break;
			case LOCAL_GAME_ROUND:
				currentPanel = new LocalGameRoundPanel(this);
				break;
			case SERVER_GAME_CONFIG:
				break;
			case CLIENT_GAME_CONFIG:
				break;
			case PROFILE_LIST:
				currentPanel = new ProfileListPanel(this);
				break;
		}
			
		getContentPane().add(currentPanel);
		validate();
		repaint();		
	}
	
	@Override
	public void displayError(String errorMessage)
	{	JOptionPane.showMessageDialog(this, errorMessage, "Erreur", JOptionPane.WARNING_MESSAGE);
		System.out.println("ERROR: "+errorMessage);
	}

	@Override
	public void windowOpened(WindowEvent e)
	{	// pas utilisé
	}

	@Override
	public void windowClosing(WindowEvent e)
	{	closeWindow();
	}

	@Override
	public void windowClosed(WindowEvent e)
	{	// pas utilisé
	}

	@Override
	public void windowIconified(WindowEvent e)
	{	// pas utilisé
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{	// pas utilisé
	}

	@Override
	public void windowActivated(WindowEvent e)
	{	// pas utilisé
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{	// pas utilisé
	}
}
