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

import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.graphics.GraphicDisplay;
import fr.univavignon.courbes.graphics.simpleimpl.GraphicDisplayImpl;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.local.KeyManager;
import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.simpleimpl.PhysicsEngineImpl;

/**
 * Panel utilisé pour afficher le jeu proprement dit,
 * i.e. le panel de l'aire de jeu et celui des scores.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public abstract class AbstractRoundPanel extends JPanel implements Runnable
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Nombre désiré de mises à jour physiques par seconde */
	protected final static int UPS = 60;
	/** Nombre désiré de mises à jour graphiques par seconde */
	protected final static int FPS = 60;
	/** Délai associé à une itération forcée */
	protected final static long FORCED_ITERATION_STEP = 50;
	/** délai entre deux màj physiques en ms */
	protected double PHYS_DELAY = 1000f / UPS; 
	/** délai entre deux màj graphiques en ms */
	protected double GRAPH_DELAY = 1000f / FPS;
	
	/**
	 * Crée une fenêtre contenant le plateau du jeu et les données du jeu.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public AbstractRoundPanel(MainWindow mainWindow)
	{	super();
		
		this.mainWindow = mainWindow;
		init();
		start();
	}
	
	/** Fenêtre principale du jeu */
	protected MainWindow mainWindow;
	/** Objet représentant la manche courante */
	protected Round round;
	/** Moteur Physique */
	protected PhysicsEngine physicsEngine;
	/** Moteur Graphique */
	protected GraphicDisplay graphicDisplay;
	/** Panel affichant l'aire de jeu */
	protected JPanel boardPanel;
	/** Panel affichant le score */
	protected JPanel scorePanel;
	/** Objet gérant les touches */
	protected KeyManager keyManager;
	/** Processus utilisé pour exécuter le jeu */
	protected Thread loopThread;
	/** Indique si le jeu est en cours (permet de savoir si la manche est finie) */
	protected boolean running;
	/** Afficher les stats dans la console, pour le dégugage */
	protected boolean showStats = false;
	/** Score total de chaque joueur */
	protected int[] totalPoints;
	/** Indique si la partie est finie, ou encore en cours */
	protected boolean matchOver = false;
	/** Indique quel serpent a été éliminé par quoi : {@code null} pour pas éliminé, une <i>valeur négative</i> pour la bordure, et {@code playerId} pour un serpent (possiblement le joueur lui-même) */
	protected Integer[] eliminatedBy;
	
	/**
	 * Initialise le panel et les objets qu'il utilise.
	 */
	protected void init()
	{	round = mainWindow.currentRound;
		
		physicsEngine = new PhysicsEngineImpl();
		physicsEngine.init(round.players.length);
		round.board = physicsEngine.getBoard();
		round.pointLimit = Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(round.players.length);
		
		graphicDisplay = new GraphicDisplayImpl();
		graphicDisplay.init(round.players.length);
		boardPanel = graphicDisplay.getBoardPanel();
		scorePanel = graphicDisplay.getScorePanel();
		
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS);
		setLayout(layout);
		
		add(Box.createHorizontalGlue());
		add(scorePanel);
		add(Box.createHorizontalGlue());
		add(boardPanel);
		add(Box.createHorizontalGlue());
		
		keyManager = new KeyManager(round.players);
		mainWindow.setFocusable(true);
		mainWindow.requestFocusInWindow();
		mainWindow.addKeyListener(keyManager);
	}
	
	/**
	 * Effectue la partie tout entière, i.e. plusieurs manches.
	 */
	protected void playMatch()
	{	totalPoints = new int[round.players.length];
		Arrays.fill(totalPoints, 0);

		do
		{	// on joue le round
			playRound();
			
			// on met à jour les score totaux
			Player[] players = round.players;
			for(int i=0;i<players.length;i++)
				totalPoints[i] = players[i].totalScore;

			// on compare le score le plus élevé et la limite
			int maxIdx = 0;
			for(int i=0;i<totalPoints.length;i++)
			{	if(totalPoints[i]>totalPoints[maxIdx])
					maxIdx = i;
			}
			matchOver = totalPoints[maxIdx]>=Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(totalPoints.length);
			
			// on affiche éventuellement le vainqueur de la partie
			if(matchOver)
			{	Profile profile = players[maxIdx].profile;
				String name = profile.userName;
				JOptionPane.showMessageDialog(mainWindow, "Le joueur "+name+"a gagné la partie !");
			}
			
			// ou bien celui de la manche, et on recommence
			else
			{	int maxIdx2 = 0;
				for(int i=0;i<players.length;i++)
				{	if(players[i].roundScore>players[maxIdx2].roundScore)
					maxIdx2 = i;
				}
				Profile profile = players[maxIdx2].profile;
				String name = profile.userName;
				JOptionPane.showMessageDialog(mainWindow, "Le joueur "+name+" a gagné la manche !");
				
				resetRound();
			}
		}
		while(!matchOver);
	}
	
	/**
	 * Effectue une manche du jeu.
	 */
	public abstract void playRound();
	
	/**
	 * Recalcule les points des joueurs en fonction des éliminations
	 * qui se sont produites lors de la dernière itération. La fonction
	 * renvoie aussi un booléen indiquant si la manche est finie ou pas.
	 * 
	 * @param prevEliminated
	 * 		Liste des numéros des joueurs éliminés lors des itérations précédentes.
	 * @param lastEliminated
	 * 		Liste des numéros des joueurs éliminés lors de l'itération en cours.
	 * @return
	 * 		{@code true} ssi la manche doit se terminer.
	 */
	protected boolean updatePoints(List<Integer> prevEliminated, List<Integer> lastEliminated)
	{	boolean result = false;
		
		Player[] players = round.players;
		int refScores[] = Constants.POINTS_FOR_RANK.get(players.length);
		
		if(!lastEliminated.isEmpty())
		{	prevEliminated.addAll(lastEliminated);
			
			// points de ceux qui ont déjà été éliminés
			int rank = players.length;
			for(int i=0;i<prevEliminated.size();i++)
			{	int playerId = prevEliminated.get(i);
				Player player = players[playerId];
				player.roundScore = refScores[rank-1];
				player.totalScore = totalPoints[playerId] + player.roundScore;
				rank--;
			}
			
			// estimation pessimiste de ceux qui n'ont pas encore été éliminés
			for(Player player: players)
			{	int playerId = player.playerId;
				if(!prevEliminated.contains(playerId))
				{	player.roundScore = refScores[rank-1];
					player.totalScore = totalPoints[playerId] + player.roundScore;
				}
			}
			
			// on teste la fin de la manche
			result = prevEliminated.size()>=players.length-1;
			
			// on met à jour la limite de points
			updatePointLimit();
		}
		
		return result;
	}

	/**
	 * Démarre la boucle minimale en créant le processus nécessaire.
	 */
	public synchronized void start()
	{	running = true;
		loopThread = new Thread(this,"Courbes-Round");
		loopThread.start();
	}
	
	/**
	 * Arrête la boucle minimale.
	 */
	public synchronized void stop()
	{	running = false;
		mainWindow.removeKeyListener(keyManager);
//		System.exit(0);
	}

	/**
	 * Réinitialise partiellement l'objet représentant
	 * la partie, pour pouvoir enchaîner une autre manche.
	 */
	protected void resetRound()
	{	keyManager.reset();
		graphicDisplay.reset();
	
		Snake[] snakes = round.board.snakes;
		boolean[] connected = new boolean[snakes.length];
		for(int i=0;i<snakes.length;i++)
			connected[i] = snakes[i].connected;
		physicsEngine.init(round.players.length);
		round.board = physicsEngine.getBoard();
		snakes = round.board.snakes;
		for(int i=0;i<snakes.length;i++)
			snakes[i].connected = connected[i];
		for(Player player: round.players)
			player.roundScore = 0;
		round.pointLimit = Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(round.players.length);
		running = true;
	}
	
	/**
	 * Met à jour la limite de points en fonction du nombre de
	 * joueurs en jeu et de leur score.
	 */
	protected void updatePointLimit()
	{	// on ne fait pas varier la limite en cours de partie, mais c'est possible de le faire ici
		round.pointLimit = Constants.POINT_LIMIT_FOR_PLAYER_NBR.get(round.players.length);
	}
	
	/**
	 * Met à jour la liste des valeurs indiquand qui
	 * a été éliminé par qui.
	 */
	protected void updatedEliminatedBy()
	{	Snake[] snakes = round.board.snakes;
		eliminatedBy = new Integer[snakes.length];
		for(int i=0;i<snakes.length;i++)
		{	eliminatedBy[i] = snakes[i].eliminatedBy;
//			System.out.print(" "+eliminatedBy[i]);
		}
//		System.out.println();
	}
}
