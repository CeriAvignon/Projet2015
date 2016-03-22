package fr.univavignon.courbes.physics.simpleimpl;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Constants;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.ItemInstance;
import fr.univavignon.courbes.common.ItemType;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.SmallUpdate;
import fr.univavignon.courbes.common.Snake;

/**
 * Classe fille de {@link Board}, permettant d'intégrer
 * des méthodes propres au Moteur Physique. 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class PhysBoard extends Board
{	/** Numéro de série (pour {@code Serializable}) */
	private static final long serialVersionUID = 1L;
	/** Générateur aléatoire utilisé lors de l'apparition d'items */
	private static final Random RANDOM = new Random();
	
	/**
	 * Constructeur nécessaire pour le mode réseau utilisant
	 * Kryonet. Ne pas utiliser ce constructeur.
	 */
	public PhysBoard()
	{	super();
		// rien à faire
	}
	
	/**
	 * Crée une nouvelle aire de jeu, à initialiser ensuite.
	 * 
	 * @param width
	 * 		Largeur de l'aire, en pixels.
	 * @param height
	 * 		Hauteur de l'aire, en pixels.
	 */
	public PhysBoard(int width, int height)
	{	super(width,height);
		
		items = new ArrayList<ItemInstance>();
		currentItems = new LinkedList<PhysItemInstance>();
		removedItems = new ArrayList<Integer>();
		totalTime = 0;
		mustClean = false;
		lastEliminated = new ArrayList<Integer>();
	}
	
	/**
	 * Crée une nouvelle aire de jeu, qui est une copie de
	 * celle passée en paramètre.
	 * 
	 * @param board
	 * 		L'aire de jeu à recopier.
	 */
	public PhysBoard(PhysBoard board)
	{	super(board.width,board.height);
		
		// classe Board
		this.state = board.state;
		this.hasBorder = board.hasBorder;
		
		this.snakes = new Snake[board.snakes.length];
		for(int i=0;i<snakes.length;i++)
		{	PhysSnake snake = (PhysSnake)board.snakes[i];
			PhysSnake copy = new PhysSnake(snake);
			this.snakes[i] = copy;
		}
		
		this.items = new ArrayList<ItemInstance>();
		Iterator<ItemInstance> it = board.items.iterator();
		while(it.hasNext())
		{	PhysItemInstance item = (PhysItemInstance)it.next();
			PhysItemInstance copy = new PhysItemInstance(item);
			this.items.add(copy);
		}
		
		// classe PhysBoard
		this.removedItems = new ArrayList<Integer>(board.removedItems);
		this.currentItems = new ArrayList<PhysItemInstance>();
		for(PhysItemInstance item: board.currentItems)
		{	PhysItemInstance copy = new PhysItemInstance(item);
			this.items.add(copy);
		}
		
		this.itemPopupRate = board.itemPopupRate;
		this.totalTime = board.totalTime;
		this.mustClean = board.mustClean;
		
		this.lastEliminated = new ArrayList<Integer>(board.lastEliminated);
	}
	
	/** File contenant les items affectant actuellement cette aire de jeu */
	public List<PhysItemInstance> currentItems;
	/** Liste contenant les numéros des items disparaissant de l'aire de jeu lors de cette itération */
	public List<Integer> removedItems;
	/** Probabilité courante qu'un item apparaisse à chaque ms */
	public float itemPopupRate;
	/** Temps total écoulé depuis le début de la manche */
	public long totalTime;
	/** Marqueur indiquant qu'il faut nettoyer l'aire de jeu des traînées */
	public boolean mustClean;
	/** Liste des joueurs éliminés lors de la dernière itération */ 
	private List<Integer> lastEliminated;
	/** Objet représentant la dernière mise à jour minimale (pour le réseau) */
	public SmallUpdate smallUpdate;
	
	/**
	 * Initialise l'aire de jeu pour une manche.
	 * 
	 * @param playerNbr
	 * 		Nombre de joueurs participants à la manche.
	 */
	public void init(int playerNbr)
	{	snakes = new Snake[playerNbr];
		for(int i=0;i<playerNbr;i++)
		{	PhysSnake snake = new PhysSnake(i,this);
			snakes[i] = snake;
		}
	}
	
	/**
	 * Initialise l'aire de jeu de manière à tester le
	 * jeu (relativement) facilement.
	 */
	public void initDemo()
	{	// on initialise les serpents (on suppose qu'il y en a seulement 2)
		snakes = new Snake[2];
		// premier joueur
		PhysSnake snake0 = new PhysSnake(0,this,width*3/4,height*3/4);
		snakes[0] = snake0;
		snake0.currentAngle = 0;
		// second joueur
		PhysSnake snake1 = new PhysSnake(1,this,width/2,height/2);
		snakes[1] = snake1;
		snake1.movingSpeed = 0;	// ce joueur ne doit pas bouger
		
		// on rajoute un bout de trainée au deuxième serpent, pour pouvoir tester les collisions
		int x1 = snakes[1].currentX;
		int y0 = snakes[1].currentY - snakes[1].headRadius;
		for(int x=Constants.BORDER_THICKNESS;x<x1;x++)
		{	for(int dy=0;dy<snakes[1].headRadius*2;dy++)
			{	Position pos = new Position(x,y0+dy);
				snakes[1].newTrail.add(pos);
			}
		}
		Position center = new Position(snakes[1].currentX,snakes[1].currentY);
		Set<Position> disk = GeometricTools.processDisk(center, snakes[1].headRadius);
		snakes[1].newTrail.addAll(disk);
		
		// on rajoute les items
		int sep = (int)((width-2*Constants.BORDER_THICKNESS-2*Constants.ITEM_RADIUS*ItemType.values().length)/(ItemType.values().length+1f));
		int x = Constants.BORDER_THICKNESS;
		int y = Constants.BORDER_THICKNESS + sep + Constants.ITEM_RADIUS;
		for(ItemType itemType: ItemType.values())
		{	x = x + sep + Constants.ITEM_RADIUS;
			PhysItemInstance item = new PhysItemInstance(itemType,x,y);
			x = x + Constants.ITEM_RADIUS;
			items.add(item);
		}
	}
	
	/**
	 * Réinitialise les charactéristiques de l'aire de jeu
	 * qui sont susceptibles de changer à chaque itération.
	 */
	private void resetCharacs()
	{	itemPopupRate = Constants.BASE_ITEM_POPUP_RATE;
		hasBorder = true;
		
		smallUpdate = new SmallUpdate(snakes.length);
		smallUpdate.hasBorder = hasBorder;
		removedItems.clear();
	}
	
	/**
	 * Mise à jour de l'aire de jeu et de tout ce
	 * qu'elle contient.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param commands
	 * 		Commandes de chaque joueur.
	 */
	public void update(long elapsedTime, Direction[] commands)
	{	// on réinitialise les paramètres de l'aire de jeu susceptibles de changer à chaque itération
		resetCharacs();
		
		// si on est au début de jeu, on met l'état à jour 
		updateState(elapsedTime);
		
		// on met à jour les items (items encore en jeu et items collectifs ramassés)
		updateItems(elapsedTime);
		
		// on met à jour les serpents
		lastEliminated = updateSnakes(elapsedTime,commands);
		
		// si nécessaire, on nettoie l'aire de jeu des trainées des serpents 
		if(mustClean)
			cleanSnakes();
		
		// on finalise l'objet de mise à jour
		completeSmallUpdate();
	}
	
	/**
	 * Met à jour l'état d'entrée du jeu, i.e. la période
	 * en début de manche, pendant laquelle les collisions
	 * sont désactivées.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateState(long elapsedTime)
	{	totalTime = totalTime + elapsedTime;
		if(totalTime <= Constants.PRESENTATION_DURATION)
			state = State.PRESENTATION;
		else if(totalTime <= Constants.PRESENTATION_DURATION+Constants.ENTRANCE_DURATION)
			state = State.ENTRANCE;
		else
			state = State.REGULAR;
	}
	
	/**
	 * Mise à jour des items situés dans l'aire de jeu
	 * et de l'effet des items collectifs.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateItems(long elapsedTime)
	{	// màj des items présents dans l'aire de jeu
		{	// on réalise la mise à jour des items
			Iterator<ItemInstance> it = items.iterator();
			List<Integer> removedItems = new ArrayList<Integer>();
			while(it.hasNext())
			{	PhysItemInstance item = (PhysItemInstance)it.next();
				boolean remove = item.updateLife(elapsedTime);
				if(remove)
				{	it.remove();
					removedItems.add(item.itemId);
				}
			}
		}
		
		// màj des items collectifs déjà ramassés par des joueurs
		{	Iterator<PhysItemInstance> it = currentItems.iterator();
			while(it.hasNext())
			{	PhysItemInstance item = it.next();
				boolean remove = item.updateEffect(elapsedTime, this);
				if(remove)
					it.remove();
			}
		}
		
		// faut-il faire apparaitre un item?
		float p = RANDOM.nextFloat();
		if(items.size()<Constants.MAX_ITEM_NBR && state==State.REGULAR && p<itemPopupRate*elapsedTime)
		{	PhysItemInstance item = generateItem();
//item.type = ItemType.COLLECTIVE_CLEAN;		
			if(item!=null)
			{	items.add(item);
				// on ajoute le nouvel item dans l'objet de mise à jour
				smallUpdate.newItem = new PhysItemInstance(item);
			}
		}
	}
	
	/**
	 * Mise à jour des serpents.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 * @param commands
	 * 		Commandes de chaque joueur.
	 * @return
	 * 		Numéros des joueurs éliminés lors de cette itération (dans l'ordre d'élimination).
	 */
	private List<Integer> updateSnakes(long elapsedTime, Direction[] commands)
	{	List<Integer> result = new ArrayList<Integer>();
		
		// on évite de traiter les serpents toujours dans le même ordre, par souci d'équité
		List<Integer> idx = new ArrayList<Integer>();
		for(int i=0;i<snakes.length;i++)
			idx.add(i);
		Collections.shuffle(idx);
		
		// on traite ensuite chaque serpent, dans l'ordre prédéterminé
		for(int i: idx)
		{	Snake s = snakes[i];
			PhysSnake snake = (PhysSnake)s;
			Direction dir = commands[i];
			if(dir==null)
				dir = Direction.NONE;
			boolean eliminated = snake.update(this,elapsedTime,dir);
			if(eliminated)
				result.add(i);
		}
		
		return result;
	}
	
	/**
	 * Supprime les trainées de tous les serpents.
	 */
	private void cleanSnakes()
	{	mustClean = false;
		for(Snake s: snakes)
		{	PhysSnake snake = (PhysSnake)s;
			snake.oldTrail.clear();
			snake.newTrail.clear();
			snake.clearedTrail = true;
		}
	}
	
	/**
	 * Génère un item de type et de position aléatoires.
	 * 
	 * @return
	 * 		L'item généré, ou {@code null} s'il n'y a pas actuellement la place nécessaire 
	 * 		sur l'aire de jeu.
	 */
	private PhysItemInstance generateItem()
	{	PhysItemInstance result = null;
		int margin = 10;
		
		// tirage a sort de la position de l'item
		// pour bien faire, il faudrait tirer au sort parmi toutes les positions disponibles, mais cela serait bien trop long
		// donc on va au plus rapide : on tire parmi toutes les positions, et on teste si elle est disponible. sinon, on recommence.
		// on met une limite sur le nombre d'essais, au cas où l'aire de jeu serait encombrée
		int x,y,i=0;
		boolean available = false;
		do
		{	// on tire la position au sort, hors-bordure
			x = RANDOM.nextInt(width-(Constants.BORDER_THICKNESS+Constants.ITEM_RADIUS+margin)*2)+Constants.BORDER_THICKNESS+Constants.ITEM_RADIUS+margin;
			y = RANDOM.nextInt(height-(Constants.BORDER_THICKNESS+Constants.ITEM_RADIUS+margin)*2)+Constants.BORDER_THICKNESS+Constants.ITEM_RADIUS+margin;
			
			// on récupère un disque représentant l'espace occupé par l'item (plus une marge)
			Position center = new Position(x,y);
			int radius = Constants.ITEM_RADIUS+margin;
			Set<Position> itemReach = GeometricTools.processDisk(center, radius);
			available = true;
			
			// on compare aux autres items
			{	Iterator<ItemInstance> it = items.iterator();
				while(it.hasNext() && available)
				{	ItemInstance item = it.next();
					Position c = new Position(item.x,item.y);
					Set<Position> ir = GeometricTools.processDisk(c, radius);
					available = !itemReach.removeAll(ir);
				}
			}
			
			// on compare aux trainées des serpents
			{	int s = 0;
				while(s<snakes.length && available)
				{	PhysSnake snake = (PhysSnake)snakes[s];
					available = !itemReach.removeAll(snake.oldTrail);
					available = !itemReach.removeAll(snake.newTrail);
					s++;
				}
			}
			
			i++;
		}
		while(i<10 && !available);
		
		if(available)
			result = new PhysItemInstance(x,y);
		return result;
	}

	/**
	 * Normalise la position de manière à tenir compte des limites de l'aire
	 * de jeu. Autrement dit, si une position sort de l'aire de jeu, on la
	 * modifie de manière à apparaître de l'autre côté. Par exemple, si elle
	 * est trop basse, on la fait réapparaitre en haut de l'aire de jeu, de
	 * manière à donner l'illusion que les côtés opposés sont reliés.
	 * 
	 * @param position
	 * 		Position à normaliser.
	 * @return
	 * 		Une nouvelle position correspondant à la normalisation de l'ancienne. 
	 */
	public Position normalizePosition(Position position)
	{	int x = (position.x + width) % width;
		int y = (position.y + height) % height;
		Position result = new Position(x,y);
		return result;
	}

	/**
	 * Normalise l'ensemble des positions spécifié, cf. la méthode
	 * {@link #normalizePosition(Position)} pour plus de détails.
	 * 
	 * @param positions
	 * 		Ensemble de positions à normaliser.
	 * @return
	 * 		Un nouvel ensemble contenant les position normalisées. 
	 */
	public Set<Position> normalizePositions(Set<Position> positions)
	{	Set<Position> result = new TreeSet<Position>();
		for(Position pos: positions)
		{	Position copy = normalizePosition(pos);
			result.add(copy);
		}
		return result;
	}
	
	/**
	 * Renvoie la liste des numéros des joueurs éliminés lors de la dernière
	 * itéation  (dans l'ordre d'élimination).
	 * 
	 * @return
	 * 		Numéros des joueurs éliminés lors de cette itération (dans l'ordre d'élimination).
	 */
	public List<Integer> getEliminatedPlayers()
	{	return lastEliminated;
	}
	
	/**
	 * Finalise l'objet utilisé pour effectuer des mises à jour minimales.
	 */
	private void completeSmallUpdate()
	{	// général
		smallUpdate.state = state;
		smallUpdate.hasBorder = hasBorder;
		
		// items
		smallUpdate.removedItems = new int[removedItems.size()];
		for(int i=0;i<removedItems.size();i++)
			smallUpdate.removedItems[i] = removedItems.get(i);
		
		// serpents
		for(int i=0;i<snakes.length;i++)
		{	PhysSnake snake = (PhysSnake)snakes[i];
			smallUpdate.clearedTrail[i] = snake.clearedTrail;
			smallUpdate.posX[i] = snake.realX;
			smallUpdate.posY[i] = snake.realY;
			smallUpdate.headRadiuses[i] = snake.headRadius;
			smallUpdate.eliminatedBy[i] = snake.eliminatedBy;
			smallUpdate.connected[i] = snake.connected;
			smallUpdate.fly[i] = snake.fly;
			{	ItemInstance[] ci = new ItemInstance[snake.currentItems.size()];
				Iterator<ItemInstance> it = snake.currentItems.iterator();
				int j = 0;
				while(it.hasNext())
				{	PhysItemInstance item = (PhysItemInstance)it.next();
					ci[j] = new PhysItemInstance(item);
					j++;
				}
				smallUpdate.currentItems.add(ci);
			}
			TreeSet<Position> nt = new TreeSet<Position>(snake.newTrail);
			smallUpdate.newTrails.add(nt);
		}
		
		// joueurs éliminés
		smallUpdate.lastEliminated = new int[lastEliminated.size()];
		for(int i=0;i<lastEliminated.size();i++)
			smallUpdate.lastEliminated[i] = lastEliminated.get(i);
	}
	
	/**
	 * Effectue une mise à jour minimale basée sur les données
	 * transmises en paramètre.
	 * 
	 * @param smallUpdate
	 * 		Données minimales nécessaires à une mise à jour de l'aire de jeu.
	 */
	public void forceUpdate(SmallUpdate smallUpdate)
	{	// aire de jeu
		state = smallUpdate.state;
		hasBorder = smallUpdate.hasBorder;
		
		// items
		for(int i=0;i<smallUpdate.removedItems.length;i++)
		{	int itemId = smallUpdate.removedItems[i];
			boolean found = false;
			Iterator<ItemInstance> it = items.iterator();
			while(!found && it.hasNext())
			{	PhysItemInstance item = (PhysItemInstance)it.next();
				if(item.itemId==itemId)
				{	it.remove();
					found = true;
				}
			}
		}
		if(smallUpdate.newItem!=null)
			items.add((PhysItemInstance)smallUpdate.newItem);
		
		// serpents
		for(int i=0;i<snakes.length;i++)
		{	PhysSnake snake = (PhysSnake)snakes[i];
			snake.clearedTrail = smallUpdate.clearedTrail[i];
			snake.realX = smallUpdate.posX[i];
			snake.realY = smallUpdate.posY[i];
			snake.currentX = (int)Math.round(snake.realX);
			snake.currentY = (int)Math.round(snake.realY);
			snake.headRadius = smallUpdate.headRadiuses[i];
			snake.eliminatedBy = smallUpdate.eliminatedBy[i];
			snake.connected = smallUpdate.connected[i];
			snake.fly = smallUpdate.fly[i];
			snake.newTrail.clear();
			snake.newTrail.addAll(smallUpdate.newTrails.get(i));
			snake.currentItems.clear();
			for(ItemInstance ii: smallUpdate.currentItems.get(i))
				snake.currentItems.offer(ii);
		}
		
		lastEliminated = new ArrayList<Integer>();
		for(int i: smallUpdate.lastEliminated)
			lastEliminated.add(i);
	}
}
