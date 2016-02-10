package fr.univavignon.courbes.inter.simpleimpl.local;

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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;

/**
 * Panel permettant de configurer une partie locale.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class LocalGameConfigPanel extends JPanel implements ActionListener
{	/** Numéro de série */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée et initialise le panel permettant de configurer
	 * une partie locale.
	 * 
	 * @param mainWindow
	 * 		Fenêtre contenant ce panel.
	 */
	public LocalGameConfigPanel(MainWindow mainWindow)
	{	super();
		this.mainWindow = mainWindow;
		
		init();
	}
	
	/** Fenêtre contenant ce panel */
	public MainWindow mainWindow;
	/** Combobox permettant de sélectionner le nombre de joueurs */
	private JComboBox<Integer> playerNbrCombo;
	/** Liste des profils sélectionnés */
	public List<LocalPlayerConfig> selectedProfiles = new ArrayList<LocalPlayerConfig>();
	/** Bouton permettant de revenir au menu principal */
	private JButton backButton;
	/** Bouton permettant de démarrer la partie */
	private JButton startButton;
	/** Panel affichant les profils sélectionnés */
	private JPanel playersPanel;
	/** Largeur des noms */
	public int nameWidth;
	/** Largeur des touches */
	public int keyWidth;
	
	/**
	 * Initialisation des composants de l'interface graphique.
	 */
	private void init()
	{	Dimension winDim = mainWindow.getPreferredSize();
		nameWidth = (int)(winDim.width*0.5);
		keyWidth = (int)(winDim.width*0.2);
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		
		initPlayerCombo();
		initPlayersPanel();
		
		add(Box.createVerticalGlue());
		
		initButtons();
	}
	
	/**
	 * Initialise le combobox permettant de choisir
	 * le nombre de joueurs.
	 */
	private void initPlayerCombo()
	{	JPanel panel = new JPanel(new FlowLayout());
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		Dimension winDim = mainWindow.getPreferredSize();
		Dimension dim = new Dimension(winDim.width,30);
		panel.setPreferredSize(dim);
		panel.setMinimumSize(dim);
		panel.setMaximumSize(dim);
		add(panel);
//		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
//		panel.setBackground(Color.RED);

		panel.add(new JLabel("Nombre de joueurs: "));
		
		Integer values[] = {2,3,4,5,6};
		playerNbrCombo = new JComboBox<Integer>(values);
		playerNbrCombo.setSelectedIndex(0);
		dim = new Dimension(40,30);
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
	private void initPlayersPanel()
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

		JLabel rightLabel = new JLabel("Gauche");
		rightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(keyWidth,height);
		rightLabel.setPreferredSize(dim);
		rightLabel.setMaximumSize(dim);
		rightLabel.setMinimumSize(dim);
		rightLabel.setBorder(border);
		titlePanel.add(rightLabel);

		titlePanel.add(Box.createHorizontalGlue());

		JLabel leftLabel = new JLabel("Droite");
		leftLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dim = new Dimension(keyWidth,height);
		leftLabel.setPreferredSize(dim);
		leftLabel.setMaximumSize(dim);
		leftLabel.setMinimumSize(dim);
		leftLabel.setBorder(border);
		titlePanel.add(leftLabel);

		addLocalProfile();
		addLocalProfile();
	}
	
	/**
	 * Initialise les boutons de ce panel.
	 */
	private void initButtons()
	{	JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
		panel.setLayout(layout);
		add(panel);

		backButton = new JButton("Retour");
		backButton.addActionListener(this);
		panel.add(backButton);
		
		panel.add(Box.createHorizontalGlue());
		
		startButton = new JButton("Démarrer");
		startButton.addActionListener(this);
		panel.add(startButton);
	}
	
	/**
	 * Détermine si la configuration du jeu est complète
	 * et consistante : comparaison des profils, des commandes, etc.
	 * 
	 * @return
	 * 		{@code true} ssi le jeu est correctement configuré.
	 */
	protected boolean isReadyToStartGame()
	{	boolean isReady = true;
		
		// on compare toutes les paires de profils sélectionnés
		int i1 = 0;
		while(i1<selectedProfiles.size() && isReady)
		{	LocalPlayerConfig lps1 = selectedProfiles.get(i1);
			Player player1 = lps1.player;
			int left1 = player1.leftKey;
			int right1 = player1.rightKey;
			Profile profile1 = player1.profile;
			
			if(left1==right1 || profile1==null || left1==-1 || right1==-1)
				isReady = false;
			
			else
			{	int i2 = i1 + 1;
				while(i2<selectedProfiles.size() && isReady)
				{	LocalPlayerConfig lps2 = selectedProfiles.get(i2);
					Player player2 = lps2.player;
					int left2 = player2.leftKey;
					int right2 = player2.rightKey;
					Profile profile2 = player2.profile;
					
					if(profile2==null || profile1.equals(profile2)
						|| left1==left2 || left1==right2 || right1==left2 || right1==right2)
						isReady = false;
					else
						i2++;
				}
				i1++;
			}
		}
		
		return isReady;
	}
	
	/**
	 * Ajoute les composants permettant de sélectionner un 
	 * nouveau profil.
	 */
	private void addLocalProfile()
	{	LocalPlayerConfig lps = new LocalPlayerConfig(this);
		selectedProfiles.add(lps);
		playersPanel.add(lps);
		
		validate();
		repaint();
	}

	/**
	 * Initialise (partiellement) la manche à partir des paramètres
	 * sélectionnés par l'utilisateur.
	 * 
	 * @return
	 * 		L'objet représentant la manche.
	 */
	private Round initRound()
	{	Round result = new Round();
		
		// initialisation des joueurs
		result.players = new Player[selectedProfiles.size()];
		for(int i=0;i<selectedProfiles.size();i++)
		{	LocalPlayerConfig lcp = selectedProfiles.get(i);
			Player player = lcp.player;
			player.playerId = i;
			result.players[i] = player;
		}
		
		result.pointLimit = Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(selectedProfiles.size());
		return result;
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==backButton)
		{	mainWindow.displayPanel(PanelName.MAIN_MENU);
		}
		
		else if(e.getSource()==startButton)
		{	if(isReadyToStartGame())
			{	Round round = initRound();
				mainWindow.currentRound = round;
				mainWindow.displayPanel(PanelName.LOCAL_GAME_ROUND);
			}
			else
			{	JOptionPane.showMessageDialog(mainWindow, 
					"<html>Les données des joueurs locaux ne sont pas correctement remplies. Vérifiez que :" +
					"<br>- tous les profils sont définis et différents, et que" +
					"<br>- toutes les commandes sont définies et différentes.</html>");
			}
		}
		
		else if(e.getSource()==playerNbrCombo)
		{	int oldPlayerNbr = selectedProfiles.size();
			int newPlayerNbr = (int) playerNbrCombo.getSelectedItem();
			
			if(oldPlayerNbr<newPlayerNbr)
			{	for(int i=oldPlayerNbr;i<newPlayerNbr;i++)
					addLocalProfile();
			}
			else
			{	for(int i=oldPlayerNbr;i>newPlayerNbr;i--)
				{	selectedProfiles.remove(i-1);
					playersPanel.remove(i);
					playersPanel.repaint();
				}
			}
		}
	}
}
