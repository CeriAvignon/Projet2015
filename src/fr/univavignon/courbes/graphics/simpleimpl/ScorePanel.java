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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;

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

	/**
	 * Crée un panel permettant d'afficher le score en cours de partie.
	 * 
	 * @param players
	 * 		Tableau de joueurs, contenant les informations nécessaires à l'affichage des scores.
	 * 		Ici, on a surtout besoin de connaitre le nombre de joueurs, pour initialiser les labels.
	 */
	public ScorePanel(Player[] players)
	{	// définition du layout du panel
		{	BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
			setLayout(layout);
		}
		// section affichant la limite de points
		{	// on crée le panel pour cette ligne
			JPanel panel = new JPanel();
			BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
			panel.setLayout(layout);
			this.add(panel);
			// on crée le label contenant le texte (constant)
			JLabel titleLabel = new JLabel("Premier à");
			panel.add(titleLabel);
			// on ajoute un séparateur
			panel.add(Box.createGlue());
			// on crée le panel contenant la limite
			limitLabel = new JLabel("NA");
			panel.add(limitLabel);
		}
		// sections affichant chaque joueur
		roundScoreLabels = new JLabel[players.length];
		nameLabels = new JLabel[players.length];
		gameScoreLabels = new JLabel[players.length];
		for(int i=0;i<players.length;i++)
		{	// on crée le panel pour la ligne du joueur
			JPanel panel = new JPanel();
			BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
			panel.setLayout(layout);
			this.add(panel);
			// on crée le label pour le score de la manche
			roundScoreLabels[i] = new JLabel("NA");
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
	 * @param pointLimit
	 * 		La nouvelle limite de points.
	 * @param players
	 * 		Tableau de joueurs, contenant les informations nécessaires à l'affichage des scores.
	 */
	public synchronized void updateData(int pointLimit, Player[] players)
	{	// labels des joueurs
		Color firstColor = null;
		for(int i=0;i<players.length;i++)
		{	Player player = players[i];
			Color color = Constants.PLAYER_COLORS[i];
			int rank = player.currentRank;
			if(rank==1)
				firstColor = color;
			
			int roundPoints = player.roundScore;
			roundScoreLabels[rank-1].setText("+"+roundPoints);
			roundScoreLabels[rank-1].setBackground(color);
			
			String name = player.profile.userName;
			nameLabels[rank-1].setText(name);
			
			int gamePoints = player.totalScore + roundPoints;
			gameScoreLabels[rank-1].setText(Integer.toString(gamePoints));
		}
		
		// label de la limite de points
		limitLabel.setText(Integer.toString(pointLimit));
		limitLabel.setBackground(firstColor);
	}
	
	// TODO la mise en forme pourrait être grandement améliorée pour coller plus au jeu original

	// TODO en réalité, les valeurs affichées en couleur sont les points marqués une fois que le joueur est éliminé.
	// mais celles en n&b sont les points avant la manche + nbre minimal de points pour la manche courante (en fonction de qui reste en jeu)
	// >> ça doit être géré coté IU
}
