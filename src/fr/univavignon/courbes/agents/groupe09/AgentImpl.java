package fr.univavignon.courbes.agents.groupe09;

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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import fr.univavignon.courbes.agents.Agent;
import fr.univavignon.courbes.agents.groupe09.greedy2.Greedy;
import fr.univavignon.courbes.common.Board;
import fr.univavignon.courbes.common.Direction;
import fr.univavignon.courbes.common.Position;
import fr.univavignon.courbes.common.Snake;

/**
 * 
 * @author Mickaël Beguin 
 * @author Hicham Belghiti-Alaoui
 * @author Aghiles Meghari
 * @author Kousseila Messas
 */
public class AgentImpl extends Agent{

	public enum DirNode{
		NONE,
		GAUCHE,
		BAS,
		DROITE,
		HAUT,
		GAUCHE_BAS,
		GAUCHE_HAUT,
		DROITE_HAUT,
		DROITE_BAS
	}


	private Snake agentSnake;
	private boolean path;
	private Greedy pathFinder;
	public final static int GRID_ROWS = 22;
	public final static int GRID_COLS = 22;
	private static final int TRESH_PATH = 100;
	private static final int TRESH_BORDER = 60;
	
	List<Point> tmpObstacles;
	List<Point> pathResult;
	Point lastPosSnake;
	Point tmpPosSnake;

	final double angles[] = {0, 7*Math.PI/4, 3*Math.PI/2, 5*Math.PI/4, Math.PI, 3*Math.PI/4, Math.PI/2, Math.PI/4};


	public AgentImpl(Integer playerId) {
		super(playerId);

		pathResult = new ArrayList<Point>();
		tmpObstacles = new ArrayList<Point>();
		pathFinder = new Greedy(800, true);
	}

	@Override
	public Direction processDirection() {
		checkInterruption();
		Board board = getBoard();
		if(board == null) {	// partie pas encore commencée : on ne fait rien
			System.out.println("Board null");
			return Direction.NONE;
		}
		else{
			agentSnake = board.snakes[getPlayerId()];
			if(agentSnake.eliminatedBy != null)
				path = false;
			if(!path ) {
				createSimplePath();
			}
			Direction d = processBorder(); // la priorité est d'eviter la bordure
			if(d!=Direction.NONE)
				return d;
			return processPathDirection(); // suivre le chemin donné par l'algorithme
		}
	}


	/**
	 * Calcule la direction nécéssaire à l'évitement d'un obstacle en fonciton de l'angle
	 * et de la position d'un serpent
	 * @return La direction à choisir
	 */
	private Direction processBorder() {
		
		int height = getBoard().height;
		int width = getBoard().width;
		float angle = agentSnake.currentAngle;
		
		if(agentSnake.currentY >= height-TRESH_BORDER && angle > Math.PI/2 && angle < Math.PI)
			return Direction.RIGHT;
		if(agentSnake.currentY >= height-TRESH_BORDER && angle < Math.PI/2 && angle > 0)
			return Direction.LEFT;
		if(agentSnake.currentY <= TRESH_BORDER && angle > 3*Math.PI/2 && angle < 2*Math.PI)
			return Direction.RIGHT;
		if(agentSnake.currentY <= TRESH_BORDER && angle < 3*Math.PI/2 && angle > Math.PI)
			return Direction.LEFT;
		if(agentSnake.currentX <= TRESH_BORDER && angle < 3*Math.PI/2 && angle > Math.PI)
			return Direction.RIGHT;
		if(agentSnake.currentX <= TRESH_BORDER && angle > Math.PI/2 && angle < Math.PI)
			return Direction.LEFT;
		if(agentSnake.currentX >= width-TRESH_BORDER && angle > 3*Math.PI/2 && angle < 2*Math.PI)
			return Direction.LEFT;
		if(agentSnake.currentX >= width-TRESH_BORDER && angle < Math.PI/2 && angle > 0)
			return Direction.RIGHT;
		return Direction.NONE;
	}

	/**
	 * Permet la génération d'un chemin avec l'algorithme greedy (non utilisé dans la version finale)
	 */
	public void createPath() {
		checkInterruption();
		lastPosSnake = new Point(Integer.MAX_VALUE,Integer.MAX_VALUE); // Valeurs qui ne peuvent pas être mal interétées
		Point nodeSnake = getGridPosition(agentSnake.currentX, agentSnake.currentY);
		pathResult.clear();
		setObstaclesAroundSnake();
		pathResult.addAll(pathFinder.path(agentSnake.currentX, agentSnake.currentY, 10, 780));
		if(pathResult == null)
			createPath();
		if(pathResult.get(0).distance(nodeSnake) > pathResult.get(pathResult.size()-1).distance(nodeSnake)) // Corrige le bug du finder qui reverse le path
			Collections.reverse(pathResult);
		unsetObstaclesAroundSnake();
		tmpPosSnake = nodeSnake;
		path = true;
		System.out.println("CHEMIN CREE snake: " + agentSnake.currentX + " " + agentSnake.currentY  +pathResult);
	}


	/**
	 * Permet la génération d'un chemin aléatoire mais atteignable physiquement
	 */
	public void createSimplePath() {
		checkInterruption();
		lastPosSnake = new Point(Integer.MAX_VALUE,Integer.MAX_VALUE); // Valeurs qui ne peuvent pas être mal interétées
		Point nodeSnake = getGridPosition(agentSnake.currentX, agentSnake.currentY);
		pathResult.clear();
		int indexNearest = getNearestAngle();
		DirNode lastDir[] = new DirNode[2];
		Point res;
		DirNode first;
		switch(indexNearest) {
		case 0:
			first = DirNode.DROITE;
			res = applyDirPath(nodeSnake, first);
			if(res == null) {
				if(nodeSnake.y<GRID_ROWS/2)
					first = DirNode.DROITE_BAS;
				else
					first = DirNode.DROITE_HAUT;
				res = applyDirPath(nodeSnake, first);
			}
			break;
		case 1:
			first = DirNode.DROITE_HAUT;
			res = applyDirPath(nodeSnake, first);
			break;
		case 2:
			first = DirNode.HAUT;
			res = applyDirPath(nodeSnake, first);
			if(res == null) {
				if(nodeSnake.x<GRID_COLS/2)
					first = DirNode.DROITE_HAUT;
				else
					first = DirNode.GAUCHE_HAUT;
				res = applyDirPath(nodeSnake, first);
			}
			break;
		case 3:
			first = DirNode.GAUCHE_HAUT;
			res = applyDirPath(nodeSnake, first);
			break;
		case 4:
			first = DirNode.GAUCHE;
			res = applyDirPath(nodeSnake, first);
			if(res == null) {
				if(nodeSnake.y<GRID_ROWS/2)
					first = DirNode.GAUCHE_BAS;
				else
					first = DirNode.GAUCHE_HAUT;
				res = applyDirPath(nodeSnake, first);
			}

			break;
		case 5:
			first = DirNode.GAUCHE_BAS;
			res = applyDirPath(nodeSnake, first);
			break;
		case 6:
			first = DirNode.BAS;
			res = applyDirPath(nodeSnake, first);
			if(res == null) {
				if(nodeSnake.x<GRID_COLS/2)
					first = DirNode.DROITE_BAS;
				else
					first = DirNode.GAUCHE_BAS;
				res = applyDirPath(nodeSnake, first);
			}
			break;
		case 7:
			first = DirNode.DROITE_BAS;
			res = applyDirPath(nodeSnake, first);
			break;
		default:
			res = null;
			first = DirNode.NONE;
		}
		if(res != null)
			pathResult.add(res);
		else
			return;
		Arrays.fill(lastDir, first);

		List <DirNode> possibilities = null;
		int rndVal;
		int miss;
		Random rand = new Random();
		for(int i=0; i<3;i++) {
			boolean flg = false;
			miss = 0;
			switch(lastDir[1]) { // On traite les possibilitées
			case GAUCHE:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.GAUCHE_BAS,DirNode.GAUCHE,DirNode.GAUCHE_HAUT));
				break;
			case BAS:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.GAUCHE_BAS,DirNode.BAS,DirNode.DROITE_BAS));
				break;
			case DROITE:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.DROITE,DirNode.DROITE_BAS,DirNode.DROITE));
				break;
			case HAUT:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.HAUT,DirNode.DROITE_HAUT,DirNode.GAUCHE_HAUT));
				break;
			case GAUCHE_BAS:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.GAUCHE,DirNode.BAS));
				break;
			case GAUCHE_HAUT:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.GAUCHE,DirNode.HAUT));
				break;
			case DROITE_HAUT:
				possibilities = new ArrayList<DirNode>(Arrays.asList(DirNode.DROITE,DirNode.HAUT));
				break;
			case DROITE_BAS:
				possibilities =new ArrayList<DirNode>( Arrays.asList(DirNode.DROITE,DirNode.BAS));
				break;
			}
			int lim = possibilities.size();
			while(miss < lim && !flg) {
				rndVal = rand.nextInt(possibilities.size());
				res = applyDirPath(pathResult.get(pathResult.size()-1), possibilities.get(rndVal));
				if(res == null) {
					miss++;
					possibilities.remove(rndVal);
				} else {
					pathResult.add(res);
					lastDir[0] = lastDir[1];
					lastDir[1] = possibilities.get(rndVal);
					flg = true;
				}
			}
		}


		System.out.println("CHEMIN CREE snake: " + agentSnake.currentX + " " + agentSnake.currentY  +pathResult);
		tmpPosSnake = nodeSnake;
		path = true;
	}



	/**
	 * Verifie si la direction que le serpent souhaite prendre est cohérente (ex: dans le plateau)
	 * et applique la direction au point
	 * @param node Noeud sur lequel le serpent est
	 * @param dir La direction qu'il souhaite prendre
	 * @return Le point résultant, null si position non cohérente
	 */
	private Point applyDirPath(Point node, DirNode dir) {
		checkInterruption();
		Point res;
		switch(dir) {
		case GAUCHE:
			res = new Point(node.x-1,node.y); 
			if (inGrid(res)  && res.x> 0 && !isObstacle(res)) return res;
			else  return null;
		case BAS:
			res = new Point(node.x,node.y+1);
			if (inGrid(res) && res.y<GRID_ROWS-1 && !isObstacle(res)) return res;
			else  return null;
		case DROITE:
			res =  new Point(node.x+1,node.y);
			if (inGrid(res) && res.x<GRID_COLS-1 && !isObstacle(res)) return res;
			else  return null;
		case HAUT:
			res =  new Point(node.x+1,node.y-1);
			if (inGrid(res) && res.y > 0 && !isObstacle(res)) return res;
			else  return null;
		case GAUCHE_BAS:
			res =  new Point(node.x-1,node.y+1);
			if (inGrid(res) && !isObstacle(res)) return res;
			else  return null;
		case GAUCHE_HAUT:
			res =  new Point(node.x-1,node.y-1);
			if (inGrid(res) && !isObstacle(res)) return res;
			else  return null;
		case DROITE_HAUT:
			res =  new Point(node.x+1,node.y-1);
			if (inGrid(res) && !isObstacle(res)) return res;
			else  return null;
		case DROITE_BAS:
			res =  new Point(node.x+1,node.y+1);
			if (inGrid(res) && !isObstacle(res)) return res;
			else  return null;
		}
		return null;
	}

	/**
	 * Teste si une position est dans la grille
	 * @param node Noeud a tester
	 * @return True si le noeud existe, false sinon
	 */
	private boolean inGrid(Point node) {
		checkInterruption();
		return node.x < GRID_COLS && node.y < GRID_ROWS && node.x >= 0 && node.y >= 0;
	}

	/**
	 * Teste si une traînée de serpent est présente dans le noeud que l'on teste
	 * @param node Noeud à tester
	 * @return true si le noeud est un obstacle, false sinon
	 */
	private boolean isObstacle(Point node ) {
		checkInterruption();
		Board board = getBoard();
		int cellRay = (int) ((getBoard().width/GRID_ROWS)*0.5); // Moitié du coté d'un noeud (un noeud est carré)
		Point nPixel = getBoardPosition(node.x,node.y);
		int x = nPixel.x - cellRay;
		int y = nPixel.y - cellRay;
		int lx = x+cellRay;
		int ly = y+cellRay;

		Set<Position> trails = new TreeSet<Position>();
		for(int i=0;i<board.snakes.length;++i)
		{	checkInterruption();	// on doit tester l'interruption au début de chaque boucle
		Snake snake = board.snakes[i];
		trails.addAll(snake.oldTrail);
		}
		for(;y<ly;y++) {
			for(;x<lx;x++){
				if(trails.contains(new Position(x,y))) {
					System.out.println("Obstacle detecté");
					return true;
				}
			
			}
		}
		return false;
	}

	/**
	 * Génére la direction selon le futur chemin donné et la position du snake
	 * @return La direction à prendre pour suivre le chemin généré
	 */
	public Direction processPathDirection() {
		checkInterruption();

		if(pathResult.size()==0) { // Chemin finit
			path = false; 
			createSimplePath();
			return Direction.NONE;
		}
		Point nodeSnake = getGridPosition(agentSnake.currentX, agentSnake.currentY);
		Point nNode = pathResult.get(0); // n+1 Next node
		int cellRay = (int) ((getBoard().width/GRID_ROWS)*0.5); // Moitié du coté d'un noeud (un noeud est carré)

		double distNextNode = (new Point(agentSnake.currentX, agentSnake.currentY).distance(getBoardPosition(nNode.x,nNode.y)));
		System.out.println("Node snake" + nodeSnake + "  Distance next node : " + distNextNode);

		if(distNextNode > TRESH_PATH) { // Génerer un nouveau chemin si le serpent est trop éloigné du prochain noeud
			createSimplePath();
		}
		if(nodeSnake.x == nNode.x && nodeSnake.y == nNode.y) { // Le serpent à atteint le node n+1
			System.out.println("Node atteint ns:" + nNode + " TmpPosSnake " + tmpPosSnake);
			lastPosSnake = tmpPosSnake;
			tmpPosSnake = nNode;
			if(pathResult.size()> 1) {
				pathResult.remove(0);
				return processPathDirection();
			} 
		}

		System.out.println("lastPosSnake: " + lastPosSnake);

		/** HAUT-GAUCHE **/
		if(nodeSnake.x-nNode.x == 1 && nodeSnake.y-nNode.y == 1) { 
			System.out.println("HAUT GAUCHE");
			if(lastPosSnake.x-nodeSnake.x==1 && lastPosSnake.y-nodeSnake.y==0) // Venant de  droite
				return Direction.RIGHT;
			else if(lastPosSnake.x-nodeSnake.x==0 && lastPosSnake.y-nodeSnake.y==1) // Venant de  bas
				return Direction.LEFT;
			else if(lastPosSnake.x-nodeSnake.x==1 && lastPosSnake.y-nodeSnake.y==1) { // Venant de haut gauche
				Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
				Point ACell = new Point(midCell.x+cellRay, midCell.y+cellRay);
				Point BCell = new Point(midCell.x-cellRay, midCell.y-cellRay);
				float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
				if(pf > 0 && agentSnake.currentAngle > 5*Math.PI/4) {
					return Direction.LEFT;
				}
				if(pf < 0 && agentSnake.currentAngle < 5*Math.PI/4){
					return Direction.RIGHT;
				}
			}
		}

		/** HAUT-DROITE **/
		else if(nodeSnake.x-nNode.x == -1 && nodeSnake.y-nNode.y == 1) { 
			System.out.println("HAUT DROITE");
			if(lastPosSnake.x-nodeSnake.x==-1 && lastPosSnake.y-nodeSnake.y==0){ // Venant de gauche
				System.out.println("VIENT DE GAUCHE");
				return Direction.LEFT;
			}
			else if(lastPosSnake.x-nodeSnake.x==0 && lastPosSnake.y-nodeSnake.y==1) {// Venant de  bas
				System.out.println("VIENT DE BAS");
				return Direction.RIGHT;
			}

			else if(lastPosSnake.x-nodeSnake.x==-1 && lastPosSnake.y-nodeSnake.y==1) { // Venant de haut gauche
				Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
				Point ACell = new Point(midCell.x-cellRay, midCell.y+cellRay);
				Point BCell = new Point(midCell.x+cellRay, midCell.y-cellRay);
				float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
				if(pf > 0 && agentSnake.currentAngle > 7*Math.PI/4) {
					return Direction.LEFT;
				}
				if(pf < 0 && agentSnake.currentAngle < 7*Math.PI/4){
					return Direction.RIGHT;
				}
			}
		}


		/** BAS-GAUCHE **/
		else if(nodeSnake.x-nNode.x == 1 && nodeSnake.y-nNode.y == -1) { 
			System.out.println("BAS GAUCHE");
			if(lastPosSnake.x-nodeSnake.x==1 && lastPosSnake.y-nodeSnake.y==0) // Venant de droite
				return Direction.LEFT;
			if(lastPosSnake.x-nodeSnake.x==0 && lastPosSnake.y-nodeSnake.y==-1) // Venant du  haut
				return Direction.RIGHT;
			if(lastPosSnake.x-nodeSnake.x==1 && lastPosSnake.y-nodeSnake.y==-1) { // Venant de haut droite
				System.out.println("viens de haut droite");
				Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
				Point ACell = new Point(midCell.x+cellRay, midCell.y-cellRay);
				Point BCell = new Point(midCell.x-cellRay, midCell.y+cellRay);
				float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
				if(pf > 0 && agentSnake.currentAngle > 3*Math.PI/4) {
					return Direction.LEFT;
				}
				if(pf < 0 && agentSnake.currentAngle < 3*Math.PI/4){
					return Direction.RIGHT;
				}
			}
		}
		/** BAS-DROITE **/
		else if(nodeSnake.x-nNode.x == -1 && nodeSnake.y-nNode.y == -1) { 
			System.out.println("BAS DROITE");
			if(lastPosSnake.x-nodeSnake.x==-1 && lastPosSnake.y-nodeSnake.y==0) // Venant de gauche
				return Direction.RIGHT;
			if(lastPosSnake.x-nodeSnake.x==0 && lastPosSnake.y-nodeSnake.y==-1) // Venant du  haut
				return Direction.LEFT;
			if(lastPosSnake.x-nodeSnake.x==-1 && lastPosSnake.y-nodeSnake.y==-1) { // Venant de haut droite
				System.out.println("viens de haut droite");
				Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
				Point ACell = new Point(midCell.x-cellRay, midCell.y-cellRay);
				Point BCell = new Point(midCell.x+cellRay, midCell.y+cellRay);
				float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
				if(pf > 0 && agentSnake.currentAngle > Math.PI/4) {
					return Direction.LEFT;
				}
				if(pf < 0 && agentSnake.currentAngle < Math.PI/4){
					return Direction.RIGHT;
				}
			}
		}


		/** GAUCHE **/
		else if(nodeSnake.x-nNode.x == 1 && nodeSnake.y-nNode.y == 0) {
			if(lastPosSnake.x-nodeSnake.x == 1 && lastPosSnake.y-nodeSnake.y == 1) // Stabilisation aprés un haut-gauche
				return Direction.LEFT;
			if(lastPosSnake.x-nodeSnake.x == 1 && lastPosSnake.y-nodeSnake.y == -1) // Stabilisation aprés un bas-gauche
				return Direction.RIGHT;

			Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
			Point ACell = new Point(midCell.x+cellRay, midCell.y);
			Point BCell = new Point(midCell.x-cellRay, midCell.y);
			float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
			if(pf > 0 && agentSnake.currentAngle > Math.PI)
				return Direction.LEFT;
			if(pf < 0 && agentSnake.currentAngle < Math.PI)
				return Direction.RIGHT;
		} 

		/** HAUT **/
		else if(nodeSnake.x-nNode.x == 0 && nodeSnake.y-nNode.y == 1) { 
			if(lastPosSnake.x-nodeSnake.x == 1 && lastPosSnake.y-nodeSnake.y == 1) // Stabilisation aprés un haut-gauche
				return Direction.RIGHT;
			if(lastPosSnake.x-nodeSnake.x == -1 && lastPosSnake.y-nodeSnake.y == 1) // Stabilisation aprés un haut-droite
				return Direction.LEFT;

			Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
			Point ACell = new Point(midCell.x, midCell.y+cellRay);
			Point BCell = new Point(midCell.x, midCell.y-cellRay);
			float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
			if(pf > 0 && agentSnake.currentAngle > 3*Math.PI/2)
				return Direction.LEFT;
			if(pf < 0 && agentSnake.currentAngle < 3*Math.PI/2)
				return Direction.RIGHT;
		}

		/** BAS **/
		else if(nodeSnake.x-nNode.x == 0 && nodeSnake.y-nNode.y == -1) { 
			System.out.println("BAS");
			if(lastPosSnake.x-nodeSnake.x == 1 && lastPosSnake.y-nodeSnake.y == -1) {// Stabilisation aprés un bas-gauche
				System.out.println("STABILISATION APRES UN BAS GAUCHE");
				return Direction.LEFT;
			}
			if(lastPosSnake.x-nodeSnake.x == -1 && lastPosSnake.y-nodeSnake.y == -1) {// Stabilisation aprés un bas-droite
				return Direction.RIGHT;
			}

			Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
			Point ACell = new Point(midCell.x, midCell.y-cellRay);
			Point BCell = new Point(midCell.x, midCell.y+cellRay);
			float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
			if(pf > 0 && agentSnake.currentAngle > Math.PI/2)
				return Direction.LEFT;
			if(pf < 0 && agentSnake.currentAngle < Math.PI/2)
				return Direction.RIGHT;
		}

		/** DROITE **/
		else if(nodeSnake.x-nNode.x == -1 && nodeSnake.y-nNode.y == 0) { 
			if(lastPosSnake.x-nodeSnake.x == -1 && lastPosSnake.y-nodeSnake.y >= 1) {// Stabilisation aprés un haut-droite
				System.out.println("STABILISATION APRES UN HAUT DROITE");
				return Direction.RIGHT;
			}
			if(lastPosSnake.x-nodeSnake.x == -1 && lastPosSnake.y-nodeSnake.y == -1) {// Stabilisation aprés un bas-droite
				System.out.println("STABILISATION APRES UN BAS DROITE");
				return Direction.LEFT;
			}

			Point midCell = getBoardPosition(nodeSnake.x, nodeSnake.y);
			Point ACell = new Point(midCell.x-cellRay, midCell.y);
			Point BCell = new Point(midCell.x+cellRay, midCell.y);
			float pf = produitVectoriel(ACell.x,ACell.y,BCell.x,BCell.y,agentSnake.currentX,agentSnake.currentY);
			System.out.println("DROITE   " + pf + "   " + agentSnake.currentAngle); // debug
			if(pf > 0 && agentSnake.currentAngle > 0 && agentSnake.currentAngle < Math.PI/2)
				return Direction.LEFT;
			if(pf < 0 && agentSnake.currentAngle < 2*Math.PI && agentSnake.currentAngle > 3*Math.PI/2)
				return Direction.RIGHT;
		}


		System.out.println("No direction");
		return Direction.NONE;


	}



	/**
	 * Permet d'enlever les obstacles autour du snake
	 */
	public void unsetObstaclesAroundSnake() {
		for(Point p : tmpObstacles) {
			pathFinder.unsetObstacle(p.x, p.y);
		}
	}

	/**
	 * Permet de savoir dans quel case de la grille se situe un pixel
	 * @param x Coord x
	 * @param y Coord y
	 * @return Le noeud de la grille correspondant
	 */
	public Point getGridPosition(int x, int y) {
		checkInterruption();
		return new Point(pathFinder.rangGreed(x), pathFinder.rangGreed(y));
	}

	/**
	 * Retourne le centre du noeud correspondant aux coordonées données
	 * @param xGrid Coord x du noeud
	 * @param yGrid Coord y du noeud
	 * @return Le point de la Board lié (1 pixel)
	 */
	public Point getBoardPosition(int xGrid, int yGrid) {
		checkInterruption();
		return new Point((int) ((xGrid+1)*(800/GRID_COLS)-((800/GRID_COLS)/2))
				,(int) ((yGrid+1)*(800/GRID_ROWS)-((800/GRID_ROWS)/2)));

	}

	/**
	 * Calcule le produit vectoriel, utile pour savoir si un point se trouve d'un coté
	 * ou d'un autre d'une droite
	 * @return Le produit vectoriel du point par rapport à la droite
	 */
	public static float produitVectoriel(int xaDroite, int yaDroite, int xbDroite, int ybDroite, int xPt, int yPt) {
		return ((xbDroite-xaDroite)*(yPt-yaDroite)-(ybDroite-yaDroite)*(xPt-xaDroite));
	}

	/**
	 * Retourne l'angle le plus proche du serpent dans un intervalle de Pi/4
	 * @return l'angle le plus proche
	 */
	public int getNearestAngle() {
		checkInterruption();
		double snakeAngle = agentSnake.currentAngle;

		double nearestAngle = 2*Math.PI;
		int indexNearest = -1;
		for(int i=0; i<angles.length; i++){
			if(snakeAngle > 5*Math.PI/3+Math.PI/8 || snakeAngle < Math.PI/4-Math.PI/8) {
				indexNearest = 0;
				break;
			}
			if(Math.abs(snakeAngle - angles[i]) <  nearestAngle) {
				nearestAngle = Math.abs(snakeAngle - angles[i]);
				indexNearest = i;
			}
		}

		return indexNearest;
	}

	/**
	 * Permet le placement d'obstacles autour du snake (non utilisé dans la version finale)
	 */
	public void setObstaclesAroundSnake() {
		checkInterruption();
		int sY = pathFinder.rangGreed(agentSnake.currentY);
		int sX = pathFinder.rangGreed(agentSnake.currentX);

		final Point pointsAround[] = {
				new Point(sX+1, sY-1),
				new Point(sX, sY-1),
				new Point(sX-1, sY-1),
				new Point(sX-1, sY),
				new Point(sX-1, sY+1),
				new Point(sX, sY+1),
				new Point(sX+1, sY+1),
				new Point(sX+1, sY)
		};

		int indexNearest = getNearestAngle();

		List<Point> open = new ArrayList<Point>();
		switch(indexNearest) {
		case 0:
			open.add(new Point(sX+1,sY));
			break;
		case 1:
			open.add(new Point(sX+1,sY));
			open.add(new Point(sX+1,sY-1));
			open.add(new Point(sX,sY-1));
			break;
		case 2:
			open.add(new Point(sX,sY-1));
			break;
		case 3:
			open.add(new Point(sX,sY-1));
			open.add(new Point(sX-1,sY-1));
			open.add(new Point(sX-1,sY));
			break;
		case 4:
			open.add(new Point(sX-1,sY));
			break;
		case 5:
			open.add(new Point(sX-1,sY));
			open.add(new Point(sX-1,sY+1));
			open.add(new Point(sX,sY+1));
			break;
		case 6:
			open.add(new Point(sX,sY+1));
			break;
		case 7:
			open.add(new Point(sX,sY+1));
			open.add(new Point(sX+1,sY+1));
			open.add(new Point(sX+1,sY));

			break;
		}

		for(Point p : pointsAround) {
			boolean isOpen = false;
			for(int i=0; i< open.size(); i++) {
				if(p.x == open.get(i).x && p.y == open.get(i).y) {
					isOpen = true;
					break;
				}
			}
			if(!isOpen) {
				tmpObstacles.add(p);
				pathFinder.setObstacle(p.x, p.y);
				System.out.println("Point bloqué : " + p);
			}
		}

	}

}
