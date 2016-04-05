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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.inter.simpleimpl.SettingsManager;

/**
 * Panel utilisé pour afficher le score de la manche en cours.
 * En l'état, on se contente d'utiliser des composants Swing, mais
 * le panel pourrait tout aussi bien être dessiné entièrement, pour
 * obtenir un rendu similaire à celui du jeu original, et avec des
 * animations.
 * <br/>
 * <b>Remarque :</b> la mis en forme pourrait grandement être améliorée
 * pour ressembler plus au jeu original.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ScorePanel extends JPanel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Hauteur d'une ligne dans ce panel */
	private final static int ROW_HEIGHT = 30;
	
	/**
	 * Crée un panel permettant d'afficher le score en cours de manche.
	 * 
	 * @param playerNbr
	 * 		Nombre de joueurs à afficher.
	 */
	public ScorePanel(int playerNbr)
	{	// définition du layout du panel
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
		
		int boardHeight = SettingsManager.getBoardHeight();
		Dimension dim = new Dimension(Constants.SCORE_WIDTH,boardHeight);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		
		initHeader();
		initRows(playerNbr);
	}
	
	/**
	 * Initialisation des composants statiques,
	 * i.e. qui ne vont pas évoluer au cours du temps.
	 */
	private void initHeader()
	{	// on crée le panel pour cette ligne
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		panel.setLayout(layout);
		Dimension dim = new Dimension(Constants.SCORE_WIDTH,ROW_HEIGHT);
		panel.setPreferredSize(dim);
		panel.setMinimumSize(dim);
		panel.setMaximumSize(dim);
		add(panel);

		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		// on crée le label contenant le texte (constant)
		JLabel titleLabel = new JLabel("Premier à");
		titleLabel.setBorder(border);
		titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dim = new Dimension((int)(Constants.SCORE_WIDTH*0.7),ROW_HEIGHT);
		titleLabel.setPreferredSize(dim);
		titleLabel.setMaximumSize(dim);
		titleLabel.setMinimumSize(dim);
		panel.add(titleLabel);
		
		// on ajoute un séparateur
		panel.add(Box.createGlue());
		
		// on crée le label contenant la limite
		limitLabel = new JLabel("NA");
		limitLabel.setOpaque(true);
		limitLabel.setBorder(border);
		limitLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dim = new Dimension((int)(Constants.SCORE_WIDTH*0.3),ROW_HEIGHT);
		limitLabel.setPreferredSize(dim);
		limitLabel.setMaximumSize(dim);
		limitLabel.setMinimumSize(dim);
		panel.add(limitLabel);
	}
	
	/**
	 * Initialisation des composants utilisés pour 
	 * afficher les scores, noms des joueurs, etc.
	 * 
	 * @param playerNbr
	 * 		Nombre de joueurs à afficher.
	 */
	private void initRows(int playerNbr)
	{	Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		
		roundScoreLabels = new JLabel[playerNbr];
		nameLabels = new JLabel[playerNbr];
		gameScoreLabels = new JLabel[playerNbr];
		for(int i=0;i<playerNbr;i++)
		{	// on crée le panel pour la ligne du joueur
			JPanel panel = new JPanel();
			BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
			panel.setLayout(layout);
			Dimension dim = new Dimension(Constants.SCORE_WIDTH,ROW_HEIGHT);
			panel.setPreferredSize(dim);
			panel.setMinimumSize(dim);
			panel.setMaximumSize(dim);
			add(panel);
			
			// on crée le label pour le score de la manche
			roundScoreLabels[i] = new JLabel("NA");
			roundScoreLabels[i].setOpaque(true);
			roundScoreLabels[i].setBorder(border);
			roundScoreLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			dim = new Dimension((int)(Constants.SCORE_WIDTH*0.15),ROW_HEIGHT);
			roundScoreLabels[i].setPreferredSize(dim);
			roundScoreLabels[i].setMaximumSize(dim);
			roundScoreLabels[i].setMinimumSize(dim);
			panel.add(roundScoreLabels[i]);
			
			// on ajoute un séparateur
//			panel.add(Box.createGlue());
			
			// on ajoute le label pour le nom du joueur
			nameLabels[i] = new JLabel("NA");
			nameLabels[i].setBorder(border);
			nameLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			dim = new Dimension((int)(Constants.SCORE_WIDTH*0.55),ROW_HEIGHT);
			nameLabels[i].setPreferredSize(dim);
			nameLabels[i].setMaximumSize(dim);
			nameLabels[i].setMinimumSize(dim);
			panel.add(nameLabels[i]);
			
			// on ajoute un séparateur
//			panel.add(Box.createGlue());
			
			// on crée le label pour le score total du joueur
			gameScoreLabels[i] = new JLabel("NA");
			gameScoreLabels[i].setBorder(border);
			gameScoreLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			dim = new Dimension((int)(Constants.SCORE_WIDTH*0.3),ROW_HEIGHT);
			gameScoreLabels[i].setPreferredSize(dim);
			gameScoreLabels[i].setMaximumSize(dim);
			gameScoreLabels[i].setMinimumSize(dim);
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
		List<Player> sortedPlayers = new ArrayList<Player>(Arrays.asList(round.players));
		Collections.sort(sortedPlayers,Player.POINTS_COMPARATOR);
		int rank = 1;
		for(Player player: sortedPlayers)
		{	int playerId = player.playerId;
			boolean alive = round.board.snakes[playerId].eliminatedBy==null;
			Color color = Constants.PLAYER_COLORS[playerId];
			if(rank==1)
				firstColor = color;
			
			int roundPoints = player.roundScore;
			roundScoreLabels[rank-1].setText("+"+roundPoints);
			roundScoreLabels[rank-1].setBackground(color);
//			roundScoreLabels[rank-1].setEnabled(alive);
			
			String name = player.profile.userName;
			nameLabels[rank-1].setText(name);
			nameLabels[rank-1].setEnabled(alive);
			
			int gamePoints = player.totalScore;
			gameScoreLabels[rank-1].setText(Integer.toString(gamePoints));
//			gameScoreLabels[rank-1].setEnabled(alive);
			
			rank++;
		}
		
		// label de la limite de points
		limitLabel.setText(Integer.toString(round.pointLimit));
		limitLabel.setBackground(firstColor);
	}
}
