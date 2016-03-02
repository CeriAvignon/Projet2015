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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;

/**
 * Panel permettant de sélectionner les joueurs participant à une partie.
 * La classe doit être spécialisée pour s'adapter aux différents types
 * de parties possibles : locale, réseau/client, réseau/serveur.
 * 
 * @param <T>
 * 		Type de joueur (local vs. distant) traité par ce panel.
 *  
 * @author	L3 Info UAPV 2015-16
 */
public abstract class AbstractPlayerSelectionPanel<T> extends AbstractConfigurationPanel
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée et initialise le panel permettant de sélectionner
	 * les participants locaux à une partie.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 * @param title
	 * 		Titre du panel.
	 */
	public AbstractPlayerSelectionPanel(MainWindow mainWindow, String title)
	{	super(mainWindow, title);
	}
	
	/** Label associé à la combobox portant sur le nombre de joueurs */
	public JLabel comboLabel;
	/** Combobox permettant de sélectionner le nombre de joueurs */
	public JComboBox<Integer> playerNbrCombo;
	/** Liste des profils sélectionnés */
	public List<T> selectedProfiles;
	/** Panel affichant les profils sélectionnés */
	public JPanel playersPanel;
	
	@Override
	protected void initContent()
	{	selectedProfiles = new ArrayList<T>();
		
		initDimensions();
		initPlayerCombo();
		initPlayersPanel();
	}
	
	/**
	 * Renvoie le texte associé au label de la combobox controlant
	 * le nombre de joueurs à sélectionner.
	 * 
	 * @return
	 * 		Texte associé à la combobox. 
	 */
	protected abstract String getComboText();
	
	/**
	 * Renvoie le nombre minimal de joueurs locaux nécessaire
	 * pour la partie.
	 * 
	 * @return
	 * 		Nombre minimal de joueurs locaux recquis. 
	 */
	protected abstract int getMinPlayerNbr();
	
	/**
	 * Renvoie le nombre maximal de joueurs locaux autorisé
	 * pour la partie.
	 * 
	 * @return
	 * 		Nombre maximal de joueurs locaux autorisé. 
	 */
	protected abstract int getMaxPlayerNbr();
	
	/**
	 * Initialise différentes valeurs utilisées pour la mise en forme.
	 */
	protected abstract void initDimensions();

	/**
	 * Initialise le combobox permettant de choisir
	 * le nombre de joueurs.
	 */
	protected void initPlayerCombo()
	{	JPanel panel = new JPanel(new FlowLayout());
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		Dimension winDim = mainWindow.getPreferredSize();
		Dimension dim = new Dimension(winDim.width,30);
		panel.setPreferredSize(dim);
		panel.setMinimumSize(dim);
		panel.setMaximumSize(dim);
		add(panel);
		
		comboLabel = new JLabel(getComboText());
		panel.add(comboLabel);
		int minNumber = getMinPlayerNbr();
		int maxNumber = getMaxPlayerNbr();
		Integer values[] = new Integer[maxNumber-minNumber+1];
		for(int i=minNumber;i<=maxNumber;i++)
			values[i-minNumber] = i;
		playerNbrCombo = new JComboBox<Integer>(values);
		int selectedVal = 0;
		if(minNumber==0)
			selectedVal = 1;
		playerNbrCombo.setSelectedIndex(selectedVal);
		dim = new Dimension(50,30);
		playerNbrCombo.setPreferredSize(dim);
		playerNbrCombo.setMinimumSize(dim);
		playerNbrCombo.setMaximumSize(dim);
		
		playerNbrCombo.addActionListener(this);
		panel.add(playerNbrCombo);
		
		panel.add(Box.createHorizontalGlue());
	}
	
	/**
	 * Initialise le panel contenant les composants
	 * relatifs aux profils sélectionnés.
	 */
	protected abstract void initPlayersPanel();
	
	/**
	 * Ajoute les composants permettant de sélectionner un 
	 * nouveau profil.
	 */
	protected abstract void addProfile();
	
	/**
	 * Détermine si la configuration du jeu est complète
	 * et consistante : comparaison des profils, des commandes, etc.
	 * 
	 * @return
	 * 		{@code true} ssi le jeu est correctement configuré.
	 */
	protected abstract boolean checkConfiguration();
	
	/**
	 * Initialise (partiellement) la manche à partir des paramètres
	 * sélectionnés par l'utilisateur.
	 * 
	 * @return
	 * 		L'objet représentant la manche.
	 */
	protected abstract Round initRound();
	
	/**
	 * Réagit à une modification de la combobox.
	 */
	protected abstract void comboboxChanged();
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	super.actionPerformed(e);
		
		if(e.getSource()==playerNbrCombo)
			comboboxChanged();
	}
}
