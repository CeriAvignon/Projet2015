package fr.univavignon.courbes.inter.simpleimpl.config;

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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 *  
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Game extends JFrame {
	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	public static final String CONSTANTE_1 = "Round";
	private JPanel gameBoard, dataBoard;
	private int tempWidth, tempHeight;
	private boolean fin;
	private int ids[];
	private Map<Integer, Direction> commands;
	private Map<Integer, int[]> commandsPlayer;
	private ArrayList<LocalProfileSelectorOoold> players;

	/**
	 * Créer une fenêtre contenant le plateau du jeu et les données du jeu.
	 * 
	 * @param width
	 *            Largeur de la fenêtre.
	 * @param height
	 *            Hauteur de la fenêtre.
	 */
	public Game(int width, int height, ArrayList<LocalProfileSelectorOoold> players) {
		super();

		tempWidth = width;
		tempHeight = height;
		this.players = players;

		commandsPlayer = new HashMap<>();

		/* For each player */
		for (LocalProfileSelectorOoold lps : players) {

			/* Set its keys */
			KeyEvent left = lps.getC_profile().getLeft();
			KeyEvent right = lps.getC_profile().getLeft();

			int[] playerKeys = new int[2];
			playerKeys[0] = left.getKeyCode();
			playerKeys[1] = right.getKeyCode();

		}
		// commandsPlayer = _commandsPlayer;

		JPanel game = new JPanel(new GridBagLayout());
		game.setPreferredSize(new Dimension(100, 100));
		gameBoard = new JPanel(new GridLayout());
		gameBoard.setPreferredSize(new Dimension(100, 100));
		dataBoard = new JPanel(new GridLayout());
		dataBoard.setPreferredSize(new Dimension(100, 100));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.75;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		game.add(gameBoard, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.25;
		gbc.weighty = 1;
		gbc.gridx = 1;
		game.add(dataBoard, gbc);
		this.add(game);
		this.setVisible(true);
		gameThread thread = new gameThread();
		thread.start();
	}
}
