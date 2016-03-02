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

import java.util.ArrayList;
import java.util.List;

import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.inter.simpleimpl.AbstractRoundPanel;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow;
import fr.univavignon.courbes.inter.simpleimpl.MainWindow.PanelName;

/**
 * Panel utilisé pour afficher le jeu proprement dit,
 * i.e. le panel de l'aire de jeu et celui des scores
 * pour une partie locale.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class LocalGameRoundPanel extends AbstractRoundPanel
{	/** Numéro de série de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Crée une fenêtre contenant le plateau du jeu et les données du jeu.
	 * 
	 * @param mainWindow
	 * 		Fenêtre principale.
	 */
	public LocalGameRoundPanel(MainWindow mainWindow)
	{	super(mainWindow);
	}

	@Override
	public void run()
	{	playMatch();
		
		// TODO la mise à jour des stats devrait être faite ici
		
		// on repart au menu principal
		mainWindow.currentRound = null;
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
				{	// on récupère les commandes des joueurs
					Direction[] directions = keyManager.retrieveDirections();
					// on met à jour le moteur physique
					physicsEngine.update(elapsedPhysTime, directions);
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
	}
}
