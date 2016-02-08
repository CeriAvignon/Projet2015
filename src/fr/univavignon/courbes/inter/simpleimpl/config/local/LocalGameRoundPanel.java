package fr.univavignon.courbes.inter.simpleimpl.config.local;

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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.graphics.GraphicDisplay;
import fr.univavignon.courbes.graphics.simpleimpl.GraphicDisplayImpl;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.physics.PhysicsEngine;
import fr.univavignon.courbes.physics.simpleimpl.PhysicsEngineImpl;

/**
 * Panel utilisé pour afficher le jeu proprement dit,
 * i.e. le panel de l'aire de jeu et celui des scores.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class LocalGameRoundPanel extends JPanel implements Runnable
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	/** Nombre désiré de mises à jour physiques par seconde */
	private final static int UPS = 60;
	/** Nombre désiré de mises à jour graphiques par seconde */
	private final static int FPS = 60;
	/** Délai associé à une itération forcée */
	private final static long FORCED_ITERATION_STEP = 50;
	/** délai entre deux màj physiques en ms */
	private double PHYS_DELAY = 1000f / UPS; 
	/** délai entre deux màj graphiques en ms */
	private double GRAPH_DELAY = 1000f / FPS;
	
	/**
	 * Crée une fenêtre contenant le plateau du jeu et les données du jeu.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public LocalGameRoundPanel(MainWindow mainWindow)
	{	super();
		
		this.mainWindow = mainWindow;
		init();
		start();
	}
	
	/** Fenêtre principale du jeu */
	private MainWindow mainWindow;
	/** Objet représentant la manche courante */
	private Round round;
	/** Tableaux des joueurs participant à la partie */
	private Player[] players;
	/** Moteur Physique */
	private PhysicsEngine physicsEngine;
	/** Aire de jeu */
	private Board board;
	/** Moteur Graphique */
	private GraphicDisplay graphicDisplay;
	/** Panel affichant l'aire de jeu */
	private JPanel boardPanel;
	/** Panel affichant le score */
	private JPanel scorePanel;
	/** Objet gérant les touches */
	private KeyManager keyManager;
	/** Processus utilisé pour exécuter le jeu */
	private Thread loopThread;
	/** Indique si le jeu est en cours (permet de savoir si la manche est finie) */
	private boolean running;
	/** Afficher les stats dans la console, pour le dégugage */
	private boolean showStats = false;
	
	/**
	 * Initialise le panel et les objets qu'il utilise.
	 */
	private void init()
	{	round = mainWindow.currentRound;
		players = round.players;
	
		physicsEngine = new PhysicsEngineImpl();
		physicsEngine.init(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT, players);
		board = physicsEngine.getBoard();
		round.board = board;
		
		graphicDisplay = new GraphicDisplayImpl();
		graphicDisplay.init(round);
		boardPanel = graphicDisplay.getBoardPanel();
		scorePanel = graphicDisplay.getScorePanel();
		
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS);
		setLayout(layout);
		add(scorePanel);
		add(Box.createHorizontalGlue());
		add(boardPanel);
		
		keyManager = new KeyManager(players);
		mainWindow.setFocusable(true);
		mainWindow.requestFocusInWindow();
		mainWindow.addKeyListener(keyManager);
	}
	
	@Override
	public void run()
	{	int phyUpdateNbr = 0;							// dernier nombre de màj physiques (stats)						
		int graphUpdateNbr = 0;							// dernier nombre de màj graphiques (stats)
		long elapsedStatTime = 0;						// temps écoulé depuis le dernier affichage des stats
		long elapsedPhysTime = 0;						// temps écoulé depuis la dernière màj physique
		long elapsedGraphTime = 0;						// temps écoulé depuis la dernière màj graphique
		long previousTime = System.currentTimeMillis();	// date de l'itération précédente
	
		while(running)
		{	long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - previousTime;
			previousTime = currentTime;
			
			// si on est en pause, on se contente de dessiner, sans mise à jour physique
			if(keyManager.isPaused() && !keyManager.getPassIteration())
			{	graphicDisplay.update(round);
			}
			
			else
			{	// si on passe simplement une itération
				if(keyManager.getPassIteration())
				{	keyManager.setPassIteration(false);
					elapsedTime = FORCED_ITERATION_STEP;	// on fait simplement un pas de FORCED_ITERATION_STEP ms
				}
				
				elapsedPhysTime = elapsedPhysTime + elapsedTime;
				elapsedGraphTime = elapsedGraphTime + elapsedTime;
				elapsedStatTime = elapsedStatTime + elapsedTime;
				
				if(elapsedPhysTime/PHYS_DELAY >= 1)
				{	Direction[] directions = keyManager.retrieveDirections();
					physicsEngine.update(elapsedPhysTime, directions);
					phyUpdateNbr++;
					elapsedPhysTime = 0;
				}
	
				if(elapsedGraphTime/GRAPH_DELAY >= 1)
				{	graphicDisplay.update(round);
					graphUpdateNbr++;
					elapsedGraphTime = 0;
				}
	
				if(elapsedStatTime >= 1000)
				{	if(showStats)
						System.out.println("UPS: "+phyUpdateNbr+" -- FPS: "+graphUpdateNbr);
					graphUpdateNbr = 0;
					phyUpdateNbr = 0;
					elapsedStatTime = 0;
				}
			}
		}
	}
	
	/**
	 * Démarre la boucle minimale en créant le processus nécessaire.
	 */
	public synchronized void start()
	{	running = true;
		loopThread = new Thread(this,"Courbes -- Round");
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
}
