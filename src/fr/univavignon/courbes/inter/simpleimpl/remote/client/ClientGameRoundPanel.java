	package fr.univavignon.courbes.inter.simpleimpl.remote.client;

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

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Round;
import fr.univavignon.courbes.common.UpdateInterface;
import fr.univavignon.courbes.inter.ClientGameHandler;
import fr.univavignon.courbes.inter.simpleimpl.AbstractRoundPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;
import fr.univavignon.courbes.inter.simpleimpl.local.KeyManager;
import fr.univavignon.courbes.network.ClientCommunication;

/**
 * Panel utilisé pour afficher le jeu proprement dit,
 * i.e. le panel de l'aire de jeu et celui des scores,
 * pour une partie réseau, côté serveur.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class ClientGameRoundPanel extends AbstractRoundPanel implements ClientGameHandler
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;

	/**
	 * Crée une fenêtre contenant le plateau du jeu et les données du jeu.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public ClientGameRoundPanel(MainWindow mainWindow)
	{	super(mainWindow);
	}
	
	/** Moteur Réseau */
	private ClientCommunication clientCom;
	/** Numéro du joueur local */
	private int localPlayerId;
	/** Indique si l'objet représentant la prochaine manche a été reçu */
	private Round newRound = null;
	
	@Override
	protected synchronized void init()
	{	// aspects réseau
		clientCom = mainWindow.clientCom;
		clientCom.setGameHandler(this);
		
		// initialisation standard
		super.init();
		
		// on récupère le numéro du seul joueur local, une fois pour toutes
		localPlayerId = -1;
		int i = 0;
		while(localPlayerId<0 && i<round.players.length)
		{	Player player = round.players[i];
			if(player.local)
				localPlayerId = i;
			i++;
		}

		// on récupère l'aire de jeu et on prévient le serveur qu'on est prêt
		resetRound();
	}
	
	@Override
	public void run()
	{	// on joue la partie (i.e. plusieurs manches)
		playMatch();
		
		// TODO la mise à jour des stats devrait aller ici
		// (soit calcul local, soit synchronisation avec le serveur)
		
		// on repart au menu principal
		clientCom.closeClient();
		clientCom.setGameHandler(null);
		mainWindow.clientCom = null;
		mainWindow.currentRound = null;
		mainWindow.clientPlayer = null;
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
		boolean finished = false;						// indique si la manche est finie, au sens des règles du jeu
		
		List<Integer> prevEliminated = new ArrayList<Integer>();
		newRound = null;
		
		while(running)
		{	long currentTime = System.currentTimeMillis();
			long elapsedTime = currentTime - previousTime;
			previousTime = currentTime;
			
			elapsedPhysTime = elapsedPhysTime + elapsedTime;
			elapsedGraphTime = elapsedGraphTime + elapsedTime;
			elapsedStatTime = elapsedStatTime + elapsedTime;
			
			if(elapsedPhysTime/PHYS_DELAY >= 1)
			{	// on envoie les commandes au serveur
				Direction[] directions = keyManager.retrieveDirections();
				sendDirection(directions); //TODO on pourrait tester si tout n'est pas NONE (auquel cas on n'enverrait rien)
				// on met à jour le moteur physique
				UpdateInterface updateData = clientCom.retrieveUpdate();
				if(updateData==null)
				{	// TODO pas forcément une bonne idée de màj localement...ou alors faut désactiver tout ce qui est aléatoire
//					physicsEngine.update(elapsedPhysTime, directions);
				}
				else
				{	if(updateData instanceof Board)
						round.board = (Board)updateData;
					physicsEngine.forceUpdate(updateData);
//if(updateData instanceof SmallUpdate)
//{	SmallUpdate su = (SmallUpdate)updateData;
//	PhysBoard b = (PhysBoard)physicsEngine.getBoard();
////	System.out.println(su.state+" vs. "+b.state);
//	if(su.state==State.ENTRANCE)
//		System.out.print("");
//}
//System.out.println("["+elapsedTime+"]"+round.board.snakes[0].currentX+" ; "+round.board.snakes[0].currentY);					
//				}
				// on met à jour les scores
				List<Integer> lastEliminated = physicsEngine.getEliminatedPlayers();
if(!lastEliminated.isEmpty())
	System.out.print("");
				if(!finished)
				{	finished = updatePoints(prevEliminated,lastEliminated);
					if(finished)
					{	finalCount = 1;
						updatedEliminatedBy();
					}
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
	 * Envoie au serveur la dernière direction prise par le joueur.
	 * 
	 * @param directions
	 * 		Directions renvoyées par le {@link KeyManager}.
	 */
	private void sendDirection(Direction[] directions)
	{	Direction direction = directions[localPlayerId];
		clientCom.sendCommand(direction);
	}
	
	@Override
	protected synchronized void resetRound()
	{	// réinitialisation locale
		super.resetRound();
		
		// on attend d'avoir récupèré l'aire de jeu (attente passive)
		if(newRound==null)
		{	try
			{	wait();
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
		}
		// on force le moteur physique à utiliser l'aire de jeu reçue du serveur
		round = newRound;
		mainWindow.currentRound = round;
		physicsEngine.setBoard(round.board);
		
		// on vide le buffer des màj qui y sont encore stockées
		clientCom.finalizeRound();
		
		// on indique au serveur qu'on est prêt
		clientCom.sendAcknowledgment();
	}

	@Override
	public synchronized void fetchRound(Round round)
	{	newRound = round;
		
		// on adapte la manche au joueur local
		for(int i=0;i<this.round.players.length;i++)
		{	if(i==localPlayerId)
			{	round.players[i].local = true;
				round.players[i].leftKey = this.round.players[i].leftKey;
				round.players[i].rightKey = this.round.players[i].rightKey;
			}
			else
			{	round.players[i].local = false;
				round.players[i].leftKey = -1;
				round.players[i].rightKey = -1;
			}
		}
		
		// on prévient le thread qui attend cette donnée pour pouvoir continuer l'initialisation
		notify();
	}

	@Override
	public void connectionLost()
	{	SwingUtilities.invokeLater(new Runnable()
		{	@Override
			public void run()
			{	if(!matchOver)
				{	JOptionPane.showMessageDialog(mainWindow, 
						"<html>La connexion avec le serveur a été perdue.</html>");
					clientCom.closeClient();
					clientCom.setGameHandler(null);
					mainWindow.clientCom = null;
					mainWindow.displayPanel(PanelName.MAIN_MENU);
				}
			}
	    });
	}
}
