package fr.univavignon.courbes.graphics.simpleimpl;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Round;

/**
 * Panel utilisé pour afficher le score de la manche en cours.
 * En l'état, on se contente d'utiliser des composants Swing, mais
 * le panel pourrait tout aussi bien être dessiné entièrement, pour
 * obtenir un rendu similaire à celui du jeu original, et avec des
 * animations.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ScorePanel extends JPanel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Largeur du panel */
	private final static int ROW_HEIGHT = 30;
	
	/**
	 * Crée un panel permettant d'afficher le score en cours de partie.
	 * 
	 * @param players
	 * 		Tableau de joueurs, contenant les informations nécessaires à l'affichage des scores.
	 * 		Ici, on a surtout besoin de connaitre le nombre de joueurs, pour initialiser les labels.
	 * @param width
	 * 		Largeur du panel de score.
	 */
	public ScorePanel(Player[] players, int width)
	{	// définition du layout du panel
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		panelWidth = width;
		
		initHeader();
		initRows(players);
		
		add(Box.createVerticalGlue());
	}
	
	/** Largeur du panel */
	private int panelWidth;
	
	/**
	 * Initialisation des composants statiques,
	 * i.e. qui ne vont pas évoluer au cours du temps.
	 */
	private void initHeader()
	{	// on crée le panel pour cette ligne
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(layout);
		Dimension dim = new Dimension(panelWidth,ROW_HEIGHT);
		panel.setPreferredSize(dim);
		panel.setMinimumSize(dim);
		panel.setMaximumSize(dim);
		add(panel);
		
		// on crée le label contenant le texte (constant)
		JLabel titleLabel = new JLabel("Premier à");
		panel.add(titleLabel);
		
		// on ajoute un séparateur
		panel.add(Box.createGlue());
		
		// on crée le label contenant la limite
		limitLabel = new JLabel("NA");
		limitLabel.setOpaque(true);
		panel.add(limitLabel);
	}
	
	/**
	 * Initialisation des composants utilisés pour 
	 * afficher les scores, noms des joueurs, etc.
	 * 
	 * @param players
	 * 		Tableau des joueurs à afficher.
	 */
	private void initRows(Player[] players)
	{	roundScoreLabels = new JLabel[players.length];
		nameLabels = new JLabel[players.length];
		gameScoreLabels = new JLabel[players.length];
		for(int i=0;i<players.length;i++)
		{	// on crée le panel pour la ligne du joueur
			JPanel panel = new JPanel();
			BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
			panel.setLayout(layout);
			Dimension dim = new Dimension(panelWidth,ROW_HEIGHT);
			panel.setPreferredSize(dim);
			panel.setMinimumSize(dim);
			panel.setMaximumSize(dim);
			add(panel);
			// on crée le label pour le score de la manche
			roundScoreLabels[i] = new JLabel("NA");
			roundScoreLabels[i].setOpaque(true);
			dim = new Dimension(50,100);
			roundScoreLabels[i].setMaximumSize(dim);
			panel.add(roundScoreLabels[i]);
			// on ajoute un séparateur
			panel.add(Box.createGlue());
			// on ajoute le label pour le nom du joueur
			nameLabels[i] = new JLabel("NA");
			panel.add(nameLabels[i]);
			// on ajoute un séparateur
			panel.add(Box.createGlue());
			// on crée le label pour le score total du joueur
			gameScoreLabels[i] = new JLabel("NA");
			panel.add(gameScoreLabels[i]);
		}
	}
	
	/** Label affichant la limite de score */
	private JLabel limitLabel;
	/** Labels affichant les noms des joueurs */
	private JLabel[] nameLabels;
	/** Labels affichant les scores pour la manche en cours */
	private JLabel[] roundScoreLabels;
	/** Labels affichant les scores totaux pour la partie */
	private JLabel[] gameScoreLabels;
	
	/**
	 * Méthode utilisée pour mettre à jour le contenu du panel de score.
	 * 
	 * @param round
	 * 		La manche en cours.
	 */
	public synchronized void updateData(Round round)
	{	// labels des joueurs
		Color firstColor = null;
		List<Player> sortedPlayers =Arrays.asList(round.players);
		Collections.sort(sortedPlayers,PLR_COMP);
		int rank = 1;
		for(Player player: sortedPlayers)
		{	int playerId = player.playerId;
			Color color = Constants.PLAYER_COLORS[playerId];
			if(rank==1)
				firstColor = color;
			
			int roundPoints = player.roundScore;
			roundScoreLabels[rank-1].setText("+"+roundPoints);
			roundScoreLabels[rank-1].setBackground(color);
			
			String name = player.profile.userName;
			nameLabels[rank-1].setText(name);
			
			int gamePoints = player.totalScore + roundPoints;
			gameScoreLabels[rank-1].setText(Integer.toString(gamePoints));
			
			rank++;
		}
		
		// label de la limite de points
		limitLabel.setText(Integer.toString(round.pointLimit));
		limitLabel.setBackground(firstColor);
	}
	
	/** Compare deux joueurs en fonction de leur rang */
	private final static Comparator<Player> PLR_COMP = new Comparator<Player>()
	{	@Override
		public int compare(Player player1, Player player2)
		{	int result = player1.currentRank - player2.currentRank;
			return result;
		}
	};
	
	// TODO la mise en forme pourrait être grandement améliorée pour coller plus au jeu original
}
