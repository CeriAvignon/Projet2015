package fr.univavignon.courbes.agents;

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

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.physics.PhysicsEngine;

/**
 * Classe chargée de gérer les joueurs artificiels, i.e. les agents.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class AgentManager
{	/** Nom du package contenant le code source des agents */
	private final static String AGENTS_PACKAGE = "fr.univavignon.courbes.agents";
	/** Nom de la classe principale d'un agent (ce <b>nom est imposé</b>) */
	private final static String AGENT_MAIN_CLASS = "AgentImpl";

	/**
	 * Crée un gestionnaire d'agents pour la partie courante.
	 * 
	 * @param players
	 * 		Joueurs participant à la partie.
	 */
	public AgentManager(Player players[])
	{	initAgents(players);
		initDirections(players);
	}
	
    /** Objets implémentant le comportement des agents */
	private Agent agents[];
    /** Gestionnaires de threads pour exécuter les agents */
    private ExecutorService[] executors;
    /** Objets utilisé pour récupérer les résultats des agents */
    private Future<Direction> futures[];
	/** Dernières directions des joueurs */
	private Direction[] lastDirections;
	/** Temps écoulés depuis les dernières mises à jour */
	private long[] elapsedTimes;
	
	/**
	 * Instancie les classes correspondants aux agents participant
	 * à la partie en cours.
	 * <i>Note :</i> on pourrait utiliser un pool contenant tous les threads
	 * gérant les agents. Cependant, on ne saurait pas quel thread est assigné
	 * à quel agent. En cas de blocage par un agent (ex. boucle infini), c'est
	 * un autre agent qui pourrait en faire les frais. Pour cette raison, on
	 * sépare les threads : 1 par executor, avec 1 executor propre à chaque agent.
	 * 
	 * @param players
	 * 		Joueurs de la partie en cours.
	 */
	@SuppressWarnings("unchecked")
	private void initAgents(Player players[])
    {	agents = new Agent[players.length];
    	futures = (Future<Direction>[])new Future[players.length];
    	executors = new ExecutorService[players.length];
    	elapsedTimes = new long[players.length];
    	
    	for(int i=0;i<players.length;i++)
    	{	Player player = players[i];
    		Agent agent = null;
    		ExecutorService executor = null;
			Profile profile = player.profile;
    		String agentName = profile.agent;
    		
    		if(player.local && agentName!=null)
    		{	try
    			{	// on charge la classe
					String packageName = AGENTS_PACKAGE + "." + agentName;
					String classQualifiedName = packageName + "." + AGENT_MAIN_CLASS;
					Class<?> tempClass = Class.forName(classQualifiedName);
					if(!Agent.class.isAssignableFrom(tempClass))
						throw new ClassCastException(classQualifiedName);
					
					// on l'instancie
					agent = (Agent)tempClass.getConstructor(Integer.class).newInstance(new Integer(i));
					final String aName = i+". "+player.profile.userName+" "+"(src="+agentName+")";
					
					// on crée l'exécuteur associé
					executor = Executors.newSingleThreadExecutor(new ThreadFactory()
					{	@Override
						public Thread newThread(Runnable r)
						{	Thread result = new Thread(r);
							result.setName(aName);
							result.setPriority(Thread.MIN_PRIORITY);
							return result;
						}
					});
				}
    			catch (ClassNotFoundException | 
    					InstantiationException | IllegalAccessException | 
    					IllegalArgumentException | InvocationTargetException | 
    					NoSuchMethodException | SecurityException e)
    			{	e.printStackTrace();
				}
    		}
    		
			agents[i] = agent;
			executors[i] = executor;
			futures[i] = null;
			elapsedTimes[i] = 0;
    	}
    }
	
	/**
	 * Initialise le tableau des dernières directions de chaque joueur.
	 * 
	 * @param players
	 * 		Joueurs de la partie en cours.
	 */
	private void initDirections(Player players[])
	{	lastDirections = new Direction[players.length];
		for(int i=0;i<lastDirections.length;i++)
		{	if(players[i].local && players[i].profile.agent!=null)
				lastDirections[i] = Direction.NONE;
			else
				lastDirections[i] = null;
		}
	}
    
	/**
	 * Renvoie les directions courantes de tous les joueurs.
	 * 
	 * @param physicsEngine
	 * 		Moteur physique de la partie en cours.
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @return
	 * 		Directions courantes des joueurs.
	 */
	public Direction[] retrieveDirections(PhysicsEngine physicsEngine, long elapsedTime)
	{	Board boardCopy = null;
		
		// on vérifie si chaque agent a terminé son calcul
		for(int i=0;i<futures.length;i++)
		{	// le joueur doit être un agent
			if(agents[i]!=null)
			{	elapsedTimes[i] = elapsedTimes[i] + elapsedTime;
				
				// il doit être actif
				if(physicsEngine.getBoard().snakes[i].eliminatedBy==null)
				{	// il doit avoir été précédemment invoqué
					if(futures[i]!=null)
					{	// s'il a fini, on récupère son résultat et on le relance
						if(futures[i].isDone())
						{	try
							{	lastDirections[i] = futures[i].get();
								if(boardCopy==null)
									boardCopy = physicsEngine.getBoardCopy();
								agents[i].updateData(boardCopy,elapsedTimes[i]);
								futures[i] = executors[i].submit(agents[i]);
								elapsedTimes[i] = 0;
							}
							catch(CancellationException e)
							{	// tâche annulée avant la fin du calcul
								lastDirections[i] = Direction.NONE;
							}
							catch (InterruptedException | ExecutionException e)
							{	e.printStackTrace();
							}
//							System.out.println(lastDirections[i]);
						}
						// sinon, rien à faire
					}
					// sinon on l'invoque maintenant
					else
						futures[i] = executors[i].submit(agents[i]);
				}
				
				// sinon on termine le traitement
				else
				{	if(futures[i]!=null)
					{	agents[i].stopRequest();
						futures[i].cancel(true);
					}
				}
			}
		}
		
		return lastDirections;
	}
	
	/**
	 * Réinitialise les dernières directions stockées, en prévision
	 * du début d'une nouvelle manche.
	 */
	public void reset()
	{	// directions
		for(int i=0;i<lastDirections.length;i++)
		{	if(agents[i]!=null)
				lastDirections[i] = Direction.NONE;
			else
				lastDirections[i] = null;
		}
		
		// threads
		for(int i=0;i<futures.length;i++)
		{	if(agents[i]!=null)
				agents[i].stopRequest();
			if(futures[i]!=null)
				futures[i].cancel(true);
			
			// ici, si l'agent ne veut pas se terminer, il sera bloqué lors de la prochaine manche
			// pour éviter tout blocage, son code source doit régulièrement invoquer sa méthode checkInterruption.
			// il est recommandé de l'invoquer au début de chaque boucle et de chaque méthode.
		}
	}

	/**
	 * Détruit les objets afin de libérer les ressources à la fin de la partie.
	 */
	public void finish()
	{	reset();
		for(ExecutorService executor: executors)
		{	if(executor!=null)
				executor.shutdown();
		}
	}
}
