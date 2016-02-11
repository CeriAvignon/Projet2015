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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.univavignon.courbes.inter.simpleimpl.MainWindow;

/**
 * Panel de base comportant des boutons pour avancer et reculer dans
 * l'enchaînement de menus. Elle doit être spécialisée de manière
 * à intégrer divers composant physiques en plus des boutons. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public abstract class AbstractConfigurationPanel extends JPanel implements ActionListener
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
	public AbstractConfigurationPanel(MainWindow mainWindow, String title)
	{	super();
		this.mainWindow = mainWindow;
		
		init(title);
	}
	
	/** Fenêtre contenant ce panel */
	public MainWindow mainWindow;
	/** Bouton permettant de revenir au menu principal */
	public JButton backButton;
	/** Bouton permettant de démarrer la partie */
	public JButton nextButton;
	
	/**
	 * Initialisation des composants de l'interface graphique.
	 * @param title
	 * 		Titre du panel.
	 */
	protected void init(String title)
	{	BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		
		initTitle(title);
		
		initContent();
		
		add(Box.createVerticalGlue());
		
		initButtons();
	}
	
	/**
	 * Initialise le titre du panel.
	 * 
	 * @param title
	 * 		Titre du panel.
	 */
	private void initTitle(String title)
	{	JLabel titleLabel = new JLabel(title);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		Font font = titleLabel.getFont();
		font = new Font(font.getName(),Font.PLAIN,20);
		titleLabel.setFont(font);
		
		Dimension winDim = mainWindow.getPreferredSize();
		Dimension dim = new Dimension(winDim.width,30);
		titleLabel.setPreferredSize(dim);
		titleLabel.setMaximumSize(dim);
		titleLabel.setMinimumSize(dim);
		
//		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//		titleLabel.setBorder(border);
		
		titleLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(titleLabel);
		
		add(Box.createVerticalStrut(20));
	}
	
	/**
	 * Initialise le contenu du panel (en plus des deux boutons par défaut).
	 */
	protected abstract void initContent();
	
	/**
	 * Initialise les boutons de ce panel.
	 */
	protected void initButtons()
	{	JPanel buttonPanel = new JPanel();
		BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS);
		buttonPanel.setLayout(layout);
		add(buttonPanel);

		backButton = new JButton("Retour");
		backButton.addActionListener(this);
		buttonPanel.add(backButton);
		
		buttonPanel.add(Box.createHorizontalGlue());
		
		nextButton = new JButton("Continuer");
		nextButton.addActionListener(this);
		buttonPanel.add(nextButton);
	}
	
	/**
	 * Passe à l'étape suivante de la configuration
	 * de la partie, ou bien début la partie elle-même
	 * (en fonction du type de partie).
	 */
	protected abstract void nextStep();
	
	/**
	 * Revient à l'étape précédente de la configuration
	 * de la partie, ou bien au menu principal
	 * (en fonction du type de partie).
	 */
	protected abstract void previousStep();
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==backButton)
			previousStep();
		else if(e.getSource()==nextButton)
			nextStep();
	}
}
