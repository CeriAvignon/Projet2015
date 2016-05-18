package fr.univavignon.courbes.agents.groupe08;

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

import java.util.LinkedList;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;
import fr.univavignon.courbes.physics.simpleimpl.PhysBoard;

/**
 * 
 * @author charlie
 * @author sabri
 * @author Alexandre
 *
 */
public class AgentImpl extends Agent
{	
	PhysBoard board = null;
	PhysBoard bdTmp = null;
	/** un tableau contenant les poids lors de la dernière itération **/
	double derniersPoids[] = null;
	/** le playerId de l'ennemi au cas où on veut prendre ses mouvements en compte **/
	int idE;
	/** l'id de notre agent **/
	int idIA;
	/** la dernière direction prise, initialisé à RIGHT pour ne pas rentre dans un mur au début d'une manche **/
	Direction lastDir = Direction.RIGHT;
	/** 
	 * si true, prend en compte les 3 dir de l'IA et les 3 dir de l'ennemi
	 * sinon,prend seulement les 3 dir de l'iA
	 * **/
	boolean calculMvEnnemi = false;
	
	
	/** Affiche l'arbre de recherche et différents infos sur la récursivité en cours**/
	boolean afficherInfosRec = false;
	/** 
	 * 	affiche divers infos, et les parametres du backtracking
	 * affiche le temps d'execution de la fonction poids() recursive
	 * affiche la direction finale prise par l'ia, a la fin de processDirection
	 * 
	 * **/
	boolean afficherInfosInitiales = false;
	boolean longTermFlag = true;
	
	int agentId = -1;
	
	double []cooSafestArea = new double[2];
		
	//METHODES
	
	/**
	 * Crée un agent contrôlant le joueur spécifié
	 * dans la partie courante.
	 * 
	 * @param playerId
	 * 		Numéro du joueur contrôlé par cet agent.
	 */
	public AgentImpl(Integer playerId) 
	{	super(playerId);
		agentId = playerId;
	}
	
	@Override
	public Direction processDirection()
	{	
		checkInterruption();	// on doit tester l'interruption au début de chaque méthode

		//on determine les id
		idIA = getPlayerId();
		if (idIA == 0) idE = 1;
		else		  idE = 0;
		
		//on utilise PhysBoard pour pouvoir accéder à l'attribut itemPopupRate et le mettre à 0
		// pour ne pas générer des items dans le backtracking
		board = (PhysBoard)getBoard();
		
		Direction result = Direction.RIGHT;
		// si la partie a commence
		if(board != null)
		{
			board.itemPopupRate = 0; //pour ne pas générer des items imaginaires dans le backtracking
			if (afficherInfosInitiales)
			{
				double pasDuree = IAConstants.PETIT_PAS_DUREE * IAConstants.NB_PETIT_PAS;
				System.out.println("------------------------------------------------------");
				System.out.println("  idE = " + idE + " idIA = " + idIA);
				System.out.println("  duree PetitPas = " + IAConstants.PETIT_PAS_DUREE + "ms, nb petit pas = " + IAConstants.NB_PETIT_PAS + " -> duree pas = " + pasDuree + " ms");
				System.out.println("  distance PetitPas = " + board.snakes[idIA].movingSpeed * IAConstants.PETIT_PAS_DUREE + "px, , nb petit pas = " + IAConstants.NB_PETIT_PAS + " -> distance pas = " + pasDuree * board.snakes[idIA].movingSpeed + " px");
				
				System.out.println("  soit une vision de  = " + board.snakes[idIA].movingSpeed * pasDuree * IAConstants.PROFONDEUR + "px (profondeur de " + IAConstants.PROFONDEUR + ")");
				System.out.println("  dist IA pr faire 90° = " + (Math.PI/2.) / board.snakes[idIA].turningSpeed * board.snakes[idIA].movingSpeed + " px");
				System.out.println("pos IA : " + board.snakes[idIA].currentX + ", " + board.snakes[idIA].currentY);
				System.out.println("----AVANT RECUSRISIVTE-----------------------------");
			}
			
			getSafestArea(board);
			//System.out.println("Safest Area : x => "+cooSafestArea[0]+" y => "+cooSafestArea[1]);
			
			//calculer le temps de la fonction du backtracking
			long tpsDeb = System.currentTimeMillis();
			double[] poids = poids(board, 0, IAConstants.PROFONDEUR); //lancement de la fonct recrsiv
			long tpsFin = System.currentTimeMillis();
			
			if (afficherInfosInitiales)
			{
				System.out.println("----APRES RECUSRISIVTE-----------------------------");
				System.out.println("*temps d'execution : " + (tpsFin - tpsDeb) + "ms");
			}
			
			//on determine la direction avec le plus gros poids
			int dir = idMax(poids);
			
			if (afficherInfosInitiales)
			{
				System.out.println("Poids calcules : ");
				System.out.println( " -LEFT  : " + poids[0]);
				System.out.println( " -NONE  : " + poids[1]);
				System.out.println( " -RIGHT : " + poids[2]);
			}
			
			//si la situation est desespre, on garde la precedente
			//parfois, l'ia semble indiquer qu'elle n'a aucune chance de survie
			//pourtant, elle peut s'en sortir
			//astuce pour la faire persister dans le dernier mouvement qu'elle a pris
			//avant de se retoruver dans une situation comme celle ci
			if (poids[0] == -1000 && poids[1] == -1000 && poids[2] == -1000)	
				result = lastDir;
			else if (dir == 0) 		
				result =  Direction.LEFT;
			else if (dir == 1)	
				result =  Direction.NONE;
			else				
				result =  Direction.RIGHT;

		}
		
		if (afficherInfosInitiales)
		{
			System.out.println(" ***DECISION : " + result + "***");
			System.out.println("------------------------------------------------------\n");
		}
			
		lastDir = result;
		board = null;
		derniersPoids = null;
		return result;
	}
	
	/**
	 * Fonction récursive qui retourne le score du 'board'
	 * @param bd le 'board' sur lequel on backtrack
	 * @param niv le niveau du backtracking
	 * @param lim le dernier niveau du backtracking (c.f : IAConstants.PROFONDEUR)
	 * @return le poids (score) du 'board'
	 * 
	 * @author Charlie
	 * @author Alexandre (modifs mineurs)
	 */
	double[] poids(PhysBoard bd, int niv, int lim)
	{
		checkInterruption();
		
		if (afficherInfosRec)
		{
			for (int i = 0; i < niv; i++) System.out.print("\t");
			System.out.println(niv + "/" + lim + " |  coo IA : (" + bd.snakes[idIA].currentX + "," + bd.snakes[1].currentY + ") crntAngle = "+ bd.snakes[idIA].currentAngle + " rad");
		}
				
		//si l'IA meurt, on elague
		if (bd.snakes[idIA].eliminatedBy != null)
		{
			if (afficherInfosRec)
			{
				for (int i = 0; i < niv; i++) System.out.print("\t");
				System.out.println("IA MORT elimnatedBy : " + bd.snakes[this.getPlayerId()].eliminatedBy + " poids = -1000");
			}
			
			double[] tab = {IAConstants.MORT_IA,IAConstants.MORT_IA,IAConstants.MORT_IA};
			derniersPoids = tab;
			return tab;
		}
		//si on arrive en branche, on evalue la board
		else if  (niv == lim)
		{
			if (afficherInfosRec)
			{
				for (int i = 0; i < niv; i++) System.out.print("\t");
				System.out.println("BRANCHE poids = 0");
			}
			
			double poids = evaluer(bd);
			double[] tab = {poids,poids,poids};
			// si derniersPoids n'a pas été encore initialisé, on l'initialise
			if (derniersPoids == null){
				derniersPoids = tab;
			}
			//si ça vaut la peine, on met dans tab la moyenne des poids de cette itération et l'itération précédente
			//c'est utile car l'IA, quand il s'approche d'un item, backtrack et ne voit plus l'item dans la prochaine itération
			// donc ici ça permet de lui dire "attention ici il y avait un item avant"
			if (caVautLaPeine(tab, derniersPoids)){
				moyenneTab(tab, derniersPoids);
			}
			derniersPoids = tab;
			return tab;
		}
		//SINON
		else
		{
			double moyenne[] = new double[3]; //tab retourne
			int iDir = 0;
			
			//enumeration des directions
			double[] pds;
			Direction[] commandes = new Direction[bd.snakes.length];
			Direction[] mouvements = {Direction.LEFT, Direction.NONE, Direction.RIGHT};
			for (Direction dirIA : mouvements)
			{
				//si on enumere les dir de l'ennemi
				//on imbrique un autre for
				if (calculMvEnnemi)
				{
					for (Direction dirE : mouvements)
					{
						checkInterruption();
						
						//on met a none les directions de chaque snake
						for (int i = 0; i < bd.snakes.length; i++)
							commandes[i] = Direction.NONE;
						//puis on applique les directions en cours de test
						commandes[idIA] = dirIA;
						commandes[idE] = dirE;
						
						bdTmp = new PhysBoard((PhysBoard) bd);
						//on applique plusieurs petits pas pour faire le grand pas
						for (int i = 0; i < IAConstants.NB_PETIT_PAS; i++)
							bdTmp.update(IAConstants.PETIT_PAS_DUREE, commandes);
						
						if (afficherInfosRec)
						{
							for (int i = 0; i < niv; i++) System.out.print("\t");
							System.out.println(" -IA = " + dirIA +" et E = " + dirE);
						}
						
						//on calcule le poids de cette nouvelle board
						
						pds = poids(bdTmp, niv+1, lim);
						if (derniersPoids == null){
							derniersPoids = pds;
						}
						if (caVautLaPeine(pds, derniersPoids)){
							moyenneTab(pds, derniersPoids);
						}
						moyenne[iDir] += moyTab(pds);
					}
					moyenne[iDir] = moyenne[iDir] / 3.;
				}
				//si on ne prend pas en compte les direction de l'ennemi
				//on le fait simplement avancer tout droit
				else
				{
					
					///on applique a la board les mouvement en cours d'enumeration
					
					//on met a none les directions de chaques snakes
					for (int i = 0; i < bd.snakes.length; i++)
						commandes[i] = Direction.NONE;
					//puis on applique a l'ia la direction en cours de test
					commandes[idIA] = dirIA;
					
					bdTmp = new PhysBoard((PhysBoard) bd);
					//on applique plusieurs petits pas pour faire le grand pas
					for (int i = 0; i < IAConstants.NB_PETIT_PAS; i++)
						bdTmp.update(IAConstants.PETIT_PAS_DUREE, commandes);
					
					if (afficherInfosRec)
					{
						for (int i = 0; i < niv; i++) System.out.print("\t");
						System.out.println(" -IA = " + dirIA);
					}
					
					//on calcule le poids de cette nouvelle board
					pds = poids(bdTmp, niv+1, lim);
					if (derniersPoids == null){
						derniersPoids = pds;
					}
					if (caVautLaPeine(pds, derniersPoids)){
						moyenneTab(pds, derniersPoids);
					}
					derniersPoids = pds;
					moyenne[iDir] = moyTab(pds);
				}
				
				if (afficherInfosRec)
				{
					for (int i = 0; i < niv; i++) System.out.print("\t");
					System.out.println(" ->MOYENNE POIDS: " + moyenne[iDir]);
				}
				
				iDir++;
			}
			bdTmp = null;
			return moyenne;
		}
		
	}
	

		/**
		 * Evalue la board passé en paramètre
		 * @param bd board à évaluer
		 * @return le poids du board
		 * 
		 * @author Charlie
		 */
		double evaluer(Board bd)
		{
			
			//valeur de poids renvoye, modifie par les conditions suivantes.
			double poids = 0;
			
			//MORT ENNEMI
			if (bd.snakes[idE].eliminatedBy != null)
			{
				poids += IAConstants.MORT_ENNEMI;
			}
			//ITEMS RECUPEREES PAR L'IA
			LinkedList<ItemInstance> itemsIA = itemsRecolteParIA(bd);
			//ITEMS RECUPEREES PAR LES AUTRES JOUEURS
			LinkedList<ItemInstance> itemsAutres = itemsRecolteParAutres(bd);
			
			poids += evalueItemsAutres(itemsAutres);
			poids += evalueItemsIA(itemsIA);
			
			double headX = bd.snakes[agentId].currentX;
			double headY = bd.snakes[agentId].currentY;		
			double distance = Math.sqrt(Math.pow(headX-cooSafestArea[0], 2) + Math.pow(headY-cooSafestArea[1], 2));		
			poids += 1000 - distance;
			
			return poids;
		}
		
		/**
		 * fonction qui renvoie les items que l'ia a attrapé dans ces tests de BT
		 * @param board le 'board' imaginaire du backtracking
		 * @return une liste d'items
		 * 
		 * @author Alexandre
		 * @author Charlie
		 */
		LinkedList<ItemInstance> itemsRecolteParIA(Board board)
		{
			LinkedList<ItemInstance> items = new LinkedList<ItemInstance>();
			
			//on enuemre les items prises pendant le BT
			for (ItemInstance item : board.snakes[idIA].currentItems)
			{
				// on rajoute l'item à la liste si le temps de son existance est inférieur au temps du backtracking
				// donc on rajoute l'item que si elle était prise pendant l'itération du backtracking
				if ( (item.type.duration - item.remainingTime) < (IAConstants.NB_PETIT_PAS * IAConstants.PETIT_PAS_DUREE) )
				{
					items.add(item);
				}
			}
			return items;
		}
		
		
		
		/**
		 * fonction qui renvoie les items que les autres joueurs a attrapé dans les tests de BT
		 * @param board le 'board' imaginaire du backtracking
		 * @return une liste d'items
		 * 
		 * @author Alexandre
		 * @author Charlie
		 */
		LinkedList<ItemInstance> itemsRecolteParAutres(Board board)
		{
			LinkedList<ItemInstance> items = new LinkedList<ItemInstance>();
			
			//on enuemre les items prises pendant le BT
			for (Snake snake : board.snakes){
				if (snake.playerId != idIA){
				for (ItemInstance item : board.snakes[snake.playerId].currentItems)
				{
					// on rajoute l'item à la liste si le temps de son existance est inférieur au temps du backtracking
					// donc on rajoute l'item que si elle était prise pendant l'itération du backtracking
					if ( (item.type.duration - item.remainingTime) < (IAConstants.NB_PETIT_PAS * IAConstants.PETIT_PAS_DUREE) )
					{
						items.add(item);
					}
				}
				}
			}
			
			return items;
		}
		
		
		
		/**
		 * Fonction qui évalue les items que l'IA a pris pendant le BT
		 * et modifie le poids selon le type d'item prise
		 * ça ne prend en compte que les item qui affect l'agent ou tout les monde en même temps
		 * @param itemsIA liste d'items que l'IA a pris
		 * @return un poids à rajouter au poids principal (ça peut être négatif)
		 * 
		 * @author Alexandre
		 */
		double evalueItemsIA(LinkedList<ItemInstance> itemsIA){
			double poidsBis = 0;
			for (ItemInstance item : itemsIA)
			{
				switch (item.type) {
				case COLLECTIVE_CLEAN:
					poidsBis += IAConstants.COLLECTIVE_CLEAN;
					break;
					
				case COLLECTIVE_TRAVERSE:
					poidsBis += IAConstants.COLLECTIVE_TRAVERSE;
					break;
					
				case USER_FAST:
					poidsBis += IAConstants.USER_FAST;
					break;
					
				case USER_FLY:
					poidsBis += IAConstants.USER_FLY;
					break;
					
				case USER_SLOW:
					poidsBis += IAConstants.USER_SLOW;
					break;
				default:
					break;
				}
			}
			return poidsBis;
		}
	
		
		/**
		 * Fonction qui évalue les items que les autres joueurs ont prisent pendant le BT
		 * et modifie le poids selon le type d'item prise
		 * ça ne prend en compte que les items qui affectent les autres joueurs ou tout le monde en même temps
		 * @param itemsIA liste d'items que l'IA a pris
		 * @return un poids à rajouter au poids principal (ça peut être négatif)
		 * 
		 * @author Alexandre
		 */
		double evalueItemsAutres(LinkedList<ItemInstance> itemsAutres){
			double poidsBis = 0;
			for (ItemInstance item : itemsAutres)
			{
				switch (item.type) {
					
				case OTHERS_FAST:
					poidsBis += IAConstants.OTHERS_FAST;
					break;
					
				case OTHERS_SLOW:
					poidsBis += IAConstants.OTHERS_SLOW;
					break;
					
				case OTHERS_THICK:
					poidsBis += IAConstants.OTHERS_THICK;
					break;
					
				default:
					break;
				}
			}
			return poidsBis;
		}
	
	/**
	* Découper l'aire de jeu en plusieurs parties et calculer des statistiques
	* sur chaque partie découper (Nombre d'item, Variance des corps de snakes...)
	* 
	* Cette fonction met a meilleure partie de l'aire de jeu
	* 
	* @param : Board 
	* 	l'aire de jeu actuelle utilisé pour estimer la partie la plus sure de la board
	* 
	* @author Sabri
	*/
	void getSafestArea(Board bd)
	{
		double []coo = new double[2];
		PhysBoard tmpBoard = new PhysBoard((PhysBoard) bd);
		
		double headX = bd.snakes[agentId].currentX;
		double headY = bd.snakes[agentId].currentY;		
		
		double []safestArea = new double[4];
		safestArea[0]=0; // Le nombre d'item max
		safestArea[3]=0;
		int upperBoundX, upperBoundY, co = 0;
		double ratio;
		// 1- Pour commencer on fait les items 
		if(!tmpBoard.items.isEmpty())
		{
			for(int i=0; i<bd.height; i+=bd.height/4)
			{
				for(int j=0;j<bd.width; j+=bd.width/4)
				{
					upperBoundX = i + bd.height/4-1;
					upperBoundY = j + bd.width/4-1;
					// Ignorer l'emplacement actuelle du snake
					if(headX < i || headX > upperBoundX || headY < i || headY > upperBoundY)
					{
						for(int k=0;k<tmpBoard.items.size(); k++)
						{
							if(tmpBoard.items.get(k).x > i && tmpBoard.items.get(k).x<upperBoundX 
														   && tmpBoard.items.get(k).y > j
														   && tmpBoard.items.get(k).y < upperBoundY)
								{
									co++;
								}
						
						}
						ratio = ratioSafestArea(i,upperBoundX,j,upperBoundY,tmpBoard);
						if(ratio <= safestArea[3] && co >= safestArea[0])
						{
							safestArea[0]=co;
							safestArea[1]= bd.height/8 + i;	// Utilisé pour renvoyer le centre de l'aire le plus "sure"
							safestArea[2]= bd.width/8 + j;
							safestArea[3]=ratio;
							if(longTermFlag)
							{
								//System.out.println("Safest Area => x : "+safestArea[1]+" y : "+safestArea[2]);
								//System.out.println("taux de pixels : "+ratio);
							}
							
						}
						co=0;
					}		
				}
			}
		}
		cooSafestArea[0] = safestArea[1];
		cooSafestArea[1] = safestArea[2];

	}
	
	/**
	 * Fonction qui retourne le taux de pixels disponible sur une partie de la 'Board' passée en paramètre
	 * @param lowBoundX : borne inférieure sur les abscisses d'une partie du board
	 * @param upBoundX :  borne supérieure les abscisses d'une partie du board
	 * @param lowBoundY : borne inférieure sur les coordonnées d'une partie du board
	 * @param upBoundY : borne supérieure sur les coordonnées d'une partie du board
	 * 
	 * @return double 
	 * 			le taux des pixels disponibles sur cette partie
	 * 
	 * @author Sabri
	 */
	double ratioSafestArea(int lowBoundX, int upBoundX, int lowBoundY, int upBoundY, PhysBoard tmpBoard)
	{
		double surface, ratio=0; 
		int co=0;
		boolean positionExist=false;
		for(int idS=0; idS<tmpBoard.snakes.length; idS++)
		{	
			// Parcourir la zone passée en paramètre
			for(int i=lowBoundX; i<upBoundX; ++i)
			{
				for(int j=lowBoundY; j<upBoundY; ++j)
				{
					positionExist = tmpBoard.snakes[idS].oldTrail.contains(new Position(i,j));
					if(positionExist) co++;
				}
			}
		}
		surface = (upBoundX - lowBoundX)*(upBoundY - lowBoundY);
		return (co/surface);
	}
	
	
	/**
	 * fonction qui renvoie la moyenne des valeurs du tableau
	 * @param tab tableau à évaluer
	 * @return la moyenne de ce tableau
	 */
	double moyTab(double[] tab)
	{
		double total = 0;
		for (double val : tab)
			total += val;
		
		return (total / tab.length);
	}
	
	
	/**
	 * renvoie l'id du nombre max dans le tableau
	 * si il y a plusieurs occurences du maximum
	 * renvoie au hasard l'id de l'une d'elle
	 * @param tab tableau à traiter
	 * @return un id
	 */	
	int idMax(double[] tab)
	{
		int id = 0;
		for (int i = 0; i < tab.length; i++)
		{
			//si on trouve une occurence du maximum
			if (tab[id] == tab[i])
				if (Math.random() > 0.5) id = i;

			//si on trouve une valeur sup
			if (tab[id] < tab[i])
				id = i;
		}
		
		return id;
	}
	
	
	/**
	 * une fonction qui change les valeurs du tableau tab1 en la moyenne des valeurs des tableau passées en paramètres
	 * @param tab1 premier tableau 
	 * @param tab2 deuxième tableau 
	 * @author Alexandre
	 */
	void moyenneTab (double [] tab1, double [] tab2){
		for (int i=0; i<tab1.length; i++){
			tab1[i] = (tab1[i]+tab2[i])/2;
	}
	
	}
	
	
	/**
	 * Une fonction qui calcul la différence entre les valeurs de 2 tableaux, et
	 * renvoie si il y a une grande différence ou pas
	 * On l'utilise pour voir s'il y a un changement dramatique dans les poids entre 2 itéartions
	 * @param tab1 premier tableau
	 * @param tab2 deuxième tableau
	 * @return true ou false
	 * 
	 * @author Alexandre
	 */
	boolean caVautLaPeine(double [] tab1, double [] tab2){
		int compteur = 0;
		for (int i=0; i<tab1.length; i++){
			if ((tab2[i] - tab1[i]) >= IAConstants.CHANGEMENT_CRITIQUE ){
				compteur++;
			}
		}
		if (compteur == 3) return true;
		return false;
	}
	

}