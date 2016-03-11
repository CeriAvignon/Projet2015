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

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Player;
import fr.univavignon.courbes.common.Profile;

/**
 * Classe chargée de gérer les joueurs artificiels, i.e. les agents.
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class AgentManager
{	/** Nom du package contenant le code source des agents */
	private final static String AGENTS_PACKAGE = "fr.univavignon.courbes.agents";
	/** Nom du dossier correspondant au package contenant tous les agents */
	private final static String AGENTS_FOLDER = "fr" + File.separator + "univavignon" + File.separator + "courbes" + File.separator + "agents";
	/** Nom de la classe principale d'un agent (nom imposé) */
	private final static String AGENT_MAIN_CLASS = "AgentImpl";
	/** Nom du fichier correspondant à la classe principale (compilée) d'un agent (nom imposé) */ 
	private final static String AGENT_MAIN_FILE = AGENT_MAIN_CLASS + ".class";

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
	private Callable<Direction> agents[];
    /** Gestionnaires de threads pour exécuter les agents */
    private ExecutorService[] executors;
    /** Objets utilisé pour récupérer les résultats des agents */
    private Future<Agent> futures[];
	/** Dernières directions des joueurs */
	private Direction[] lastDirections;
	
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
    {	agents = (Callable<Direction>[]) new Callable[players.length];
    	futures = (Future<Agent>[])new Future[players.length];
    	executors = new ExecutorService[players.length];
    	
    	for(int i=0;i<players.length;i++)
    	{	Player player = players[i];
    		Agent agent = null;
    		ExecutorService executor = null;
			Profile profile = player.profile;
    		String agentName = profile.agent;
    		
    		if(agentName!=null)
    		{	try
    			{	// on vérifie que le fichier de la classe principale existe
					String packageFolder = AGENTS_FOLDER + File.separator + agentName;
					String classFile = packageFolder + File.separator + AGENT_MAIN_FILE;
					File file = new File(classFile);
					if(!file.exists())
						throw new FileNotFoundException(classFile);
					
					// on charge la classe
					String packageName = AGENTS_PACKAGE + "." + agentName;
					String classQualifiedName = packageName + "." + AGENT_MAIN_CLASS;
					Class<?> tempClass = Class.forName(classQualifiedName);
					if(!Agent.class.isAssignableFrom(tempClass))
						throw new ClassCastException(classQualifiedName);
					
					// build an instance
					agent = (Agent)tempClass.getConstructor().newInstance();
					
					// create the associated executor
					executor = Executors.newSingleThreadExecutor();
				}
    			catch (FileNotFoundException | ClassNotFoundException | 
    					InstantiationException | IllegalAccessException | 
    					IllegalArgumentException | InvocationTargetException | 
    					NoSuchMethodException | SecurityException e)
    			{	e.printStackTrace();
				}
    		}
    		
			agents[i] = agent;
			executors[i] = executor;
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
		{	if(players[i].profile.agent!=null)
				lastDirections[i] = Direction.NONE;
			else
				lastDirections[i] = null;
		}
	}
    
	/**
	 * Renvoie les directions courantes de tous les joueurs.
	 * À noter qu'il s'agit d'une copie du tableau stocké dans ce
	 * gestionnaire de touches, afin d'éviter tout problème d'accès
	 * concurrent. 
	 * 
	 * @return
	 * 		Directions courantes des joueurs.
	 */
	public synchronized Direction[] retrieveDirections()
	{	Direction[] result = Arrays.copyOf(lastDirections, lastDirections.length);
		return result;
	}
	
	/**
	 * Réinitialise les dernières directions stockées, en prévision
	 * du début d'une nouvelle manche.
	 */
	public synchronized void reset()
	{	// directions
		for(int i=0;i<lastDirections.length;i++)
		{	if(agents[i]!=null)
				lastDirections[i] = Direction.NONE;
			else
				lastDirections[i] = null;
		}
		
		// threads
		for(int i=0;i<futures.length;i++)
		{	if(futures[i]!=null)
			{	futures[i].cancel(true);
				// ici, si l'agent ne veut pas se terminer, il sera bloqué lors de la prochaine manche
			}
		}
	}
}
