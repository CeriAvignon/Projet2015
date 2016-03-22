package fr.univavignon.courbes.inter.simpleimpl.profiles;

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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;

/**
 * Panel destiné à afficher la liste des profils existants.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ProfileListPanel extends JPanel implements ActionListener, FocusListener
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Nom par défaut pour le champ texte */
	private static final String DEFAULT_NAME = "Nom";
	/** Pays par défaut pour le champ texte */
	private static final String DEFAULT_COUNTRY = "Pays";
	
	/**
	 * Crée un nouveau panel destiné à afficher la liste des profils.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale contenant ce panel.
	 */
	public ProfileListPanel(MainWindow mainWindow)
	{	super();
		this.mainWindow = mainWindow;
		
		init();
	}
	
	/** Fenêtre contenant ce panel */
	private MainWindow mainWindow;
	/** Table affichée par ce panel */
	private JTable playerTable;
	/** Scrollpane contenu dans ce panel pour afficher la table */
	private JScrollPane scrollPane; 
	/** Champ texte contenant le nom d'un nouveau profil */
	private JTextField nameField;
	/** Champ texte contenant le pays d'un nouveau profil */
	private JTextField countryField;
	/** Bouton pour revenir au menu principal */
	private JButton backButton;
	/** Bouton pour ajouter le nouveau profil */
	private JButton addButton;
	/** Bouton pour supprimer le profil sélectionné */
	private JButton removeButton;
	
	/**
	 * Méthode principale d'initialisation du panel.
	 */
	private void init()
	{	BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
	
		initTablePanel();
		
		add(Box.createVerticalGlue());
		
		initTextFields();
		initButtonsPanel();
	}
	
	/**
	 * Initialisation de la table affichée par ce panel.
	 */
	private void initTablePanel()
	{	playerTable = new JTable();
		playerTable.setAutoCreateRowSorter(true);
		
		playerTable.setModel(new ProfileTableModel());
		
		scrollPane = new JScrollPane
		(	playerTable,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension frameDim = mainWindow.getPreferredSize();
		int boardHeight = SettingsManager.getBoardHeight();
		Dimension dim = new Dimension(frameDim.width,(int)(boardHeight*0.8));
		scrollPane.setPreferredSize(dim);
		scrollPane.setMaximumSize(dim);
		scrollPane.setMinimumSize(dim);
		add(scrollPane);
	}
	
	/**
	 * Initialisation des champs texte contenus dans ce panel.
	 */
	private void initTextFields()
	{	JLabel newPlayerLabel = new JLabel("Nouveau profil");
		newPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(newPlayerLabel);
		
		Dimension frameDim = mainWindow.getPreferredSize();
		Dimension dim = new Dimension(frameDim.width,30);
		
		nameField = new JTextField(DEFAULT_NAME);
		nameField.addFocusListener(this);
		nameField.setPreferredSize(dim);
		nameField.setMaximumSize(dim);
		nameField.setMinimumSize(dim);
		add(nameField);

		countryField = new JTextField(DEFAULT_COUNTRY);
		countryField.addFocusListener(this);
		countryField.setPreferredSize(dim);
		countryField.setMaximumSize(dim);
		countryField.setMinimumSize(dim);
		add(countryField);
	}
	
	/**
	 * Initialisation des boutons contenus dans ce panel.
	 */
	private void initButtonsPanel()
	{	JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		
		backButton = new JButton("Retour");
		backButton.addActionListener(this);
		panel.add(backButton);
		
		panel.add(Box.createHorizontalGlue());
		
		removeButton = new JButton("Supprimer");
		removeButton.addActionListener(this);
		panel.add(removeButton);
		
		panel.add(Box.createHorizontalGlue());
		
		addButton = new JButton("Ajouter");
		addButton.addActionListener(this);
		panel.add(addButton);
		
		add(panel);
	}
	
	/**
	 * Validation de l'ajout d'un nouveau profil.
	 */
	private void addPlayer()
	{	String userName = nameField.getText();
		String country = countryField.getText();

		// on vérifie que les champs ont été remplis, et que le nom n'est pas déjà pris
		if(!userName.isEmpty() && !country.isEmpty() && !ProfileManager.containsUserName(userName))
		{	// on crée le profil
			Profile profile = new Profile();
			profile.userName = userName;
			profile.country = country;
			profile.eloRank = ProfileManager.getProfiles().size()+1;
			
			// on le rajoute à la liste
			ProfileManager.addProfile(profile);
			
			// on le rajoute dans la table
			ProfileTableModel model = (ProfileTableModel) playerTable.getModel();
			model.addProfile(profile);
			
			// on réinitialise les champs texte
			nameField.setText(DEFAULT_NAME);
			countryField.setText(DEFAULT_COUNTRY);
		}
	}
	
	/**
	 * Suppression d'un profil existant.
	 */
	private void removePlayer()
	{	int selected = playerTable.getSelectedRow();
		
		if(selected>=0)
		{	// on récupère le profil
			List<Profile> profiles = new ArrayList<Profile>(ProfileManager.getProfiles());
			Profile profile = profiles.get(selected);
			
			// on supprime le profil de la liste
			ProfileManager.removeProfile(profile);
			
			// on le supprime de la table
			ProfileTableModel model = (ProfileTableModel) playerTable.getModel();
			model.removeProfile(selected);
		}
	}
	
	@Override
	public void focusGained(FocusEvent e)
	{	if(e.getSource()==nameField)
		{	nameField.setText("");
		}
		else if(e.getSource()==countryField)
		{	countryField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e)
	{	// pas utilisé
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==backButton)
			mainWindow.displayPanel(PanelName.MAIN_MENU);
		else if(e.getSource()==addButton)
			addPlayer();
		else if(e.getSource()==removeButton)
			removePlayer();
	}
}
