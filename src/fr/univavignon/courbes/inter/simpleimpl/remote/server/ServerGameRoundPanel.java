package fr.univavignon.courbes.inter.simpleimpl.remote.server;

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

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ServerGameHandler;
import fr.univavignon.courbes.inter.simpleimpl.AbstractRoundPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.network.ServerCommunication;

/**
 * Panel utilisé pour afficher le jeu proprement dit,
 * i.e. le panel de l'aire de jeu et celui des scores,
 * pour une partie réseau, côté serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ServerGameRoundPanel extends AbstractRoundPanel implements ServerGameHandler
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;

	/**
	 * Crée une fenêtre contenant le plateau du jeu et les données du jeu.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public ServerGameRoundPanel(MainWindow mainWindow)
	{	super(mainWindow);
	}
	
	/** Nombre de joueurs locaux */
	private int localPlayerNbr;
	/** Indique le nombre de clients prêts */
	private int readyClientNbr;
	/** Indique les numéros des clients */
	private List<Integer> clientIndices;
	/** Moteur Réseau */
	private ServerCommunication serverCom;
	
	@Override
	protected void init()
	{	// aspects réseau
		serverCom = mainWindow.serverCom;
		serverCom.setGameHandler(this);
		
		// initialisation standard
		super.init();
		
		// on identifie les numéros des clients une fois pour toutes
		clientIndices = new ArrayList<Integer>();
		localPlayerNbr = 0;
		Player[] players = round.players;
		for(int i=0;i<players.length;i++)
		{	Player player = players[i];
			if(player.local)
				localPlayerNbr++;
			else
				clientIndices.add(i);
		}

		// on envoie l'aire de jeu et on attend les clients
		resetRound();
	}
	
	@Override
	public void run()
	{	// on joue la partie (i.e. plusieurs manches)
		playMatch();
		
		// TODO la mise à jour des stats irait ici
		
		// on repart au menu principal
		serverCom.closeServer();
		serverCom.setGameHandler(null);
		mainWindow.serverCom = null;
		mainWindow.currentRound= null;
		mainWindow.displayPanel(PanelName.MAIN_MENU);
	}
	
	@Override
	public void playRound()
	{	int phyUpdateNbr = 0;							// dernier nombre de màj physiques (stats)						
		int graphUpdateNbr = 0;							// dernier nombre de màj graphiques (stats)
		long elapsedStatTime = 0;						// temps écoulé depuis le dernier affichage des stats
		long elapsedPhysTime = 0;						// temps écoulé depuis la dernière màj physique
		long elapsedGraphTime = 0;						// temps écoulé depuis la dernière màj graphique
		long previousTime = System.currentTimeMillis();	// date de l'itération précédente
		long finalCount = 0;							// décompte pour la toute fin de manche
		int physUpdates = 0;							// nombre de petites màj physiques depuis la dernière grosse 
		boolean finished = false;						// indique si la manche est finie, au sens des règles du jeu
		
		List<Integer> prevEliminated = new ArrayList<Integer>();
		readyClientNbr = 0;
		
		while(running)
		{	long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - previousTime;
			previousTime = currentTime;
			
			elapsedPhysTime = elapsedPhysTime + elapsedTime;
			elapsedGraphTime = elapsedGraphTime + elapsedTime;
			elapsedStatTime = elapsedStatTime + elapsedTime;
			
			if(elapsedPhysTime/PHYS_DELAY >= 1)
			{	// on récupère les commandes des joueurs
				Direction[] directions = keyManager.retrieveDirections();
				completeDirections(directions);
				// on met à jour le moteur physique et on envoie aux clients
				physicsEngine.update(elapsedPhysTime, directions);
//System.out.println("["+elapsedTime+"]"+round.board.snakes[0].currentX+" ; "+round.board.snakes[0].currentY);
				physUpdates++;
				UpdateInterface updateData = physicsEngine.getSmallUpdate();
				if(physUpdates==20)
					updateData = physicsEngine.getBoardCopy();
				serverCom.sendUpdate(updateData);
				// on met à jour les scores
				List<Integer> lastEliminated = physicsEngine.getEliminatedPlayers();
				if(!finished)
				{	finished = updatePoints(prevEliminated,lastEliminated);
					if(finished)
					{	finalCount = 1;
						updatedEliminatedBy();
					}
				}
				phyUpdateNbr++;
				elapsedPhysTime = 0;
			}

			if(elapsedGraphTime/GRAPH_DELAY >= 1)
			{	graphicDisplay.update(round);
				graphUpdateNbr++;
				elapsedGraphTime = 0;
			}

			if(finalCount>0)
			{	finalCount = finalCount + elapsedTime;
				if(finalCount>=Constants.END_DURATION)
					running = false;
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
	
	/**
	 * Reçoit un tableau contenant les directions des joueurs locaux,
	 * et y rajoute celles des joueurs distants.
	 * 
	 * @param localDirections
	 * 		Tableau des directions des joueurs locaux, à compléter.
	 */
	private void completeDirections(Direction[] localDirections)
	{	Direction[] clientDirections = serverCom.retrieveCommands();
		if(clientDirections!=null)
		{	int j = 0;
			for(int i: clientIndices)
				localDirections[i] = clientDirections[j];
		}
	}
	
	@Override
	protected synchronized void resetRound()
	{	// réinitialisation locale
		super.resetRound();
		
		// on transmet aux clients
		serverCom.sendRound(round);
		
		// on vide le buffer des màj qui y sont encore stockées
		serverCom.finalizeRound();
		
		// on attend que les clients soient prêts (attente passive)
		while(readyClientNbr<clientIndices.size())
		{	try
			{	wait();
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized void fetchAcknowledgment(int index)
	{	
		readyClientNbr++;
		notify();
	}
	
	/**
	 * Traitement d'une perte de connexion (on a besoin
	 * de cette méthode séparée, afin de la synchroniser).
	 * 
	 * @param index
	 * 		Numéro du client concerné.
	 */
	private synchronized void processLostConnection(int index)
	{	int playerId = localPlayerNbr + index;
		round.board.snakes[playerId].connected = false;
		clientIndices.remove(new Integer(playerId));
		notify();
	}
	
	@Override
	public void connectionLost(final int index)
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	processLostConnection(index);
			}
	   });
	}
}

// TODO rajouter un contrôle de la fréq de mise à jour réseau ?
// TODO dans MP client, éviter tout ce qui dépend du hasard: pas d'item, pas de trous dans traines...
