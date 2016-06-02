package fr.univavignon.courbes.agents.groupe09.greedy2;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.univavignon.courbes.agents.groupe09.AgentImpl;

/**
 * 
 *
 */
public class Greedy {
	/*
     **********************************************************
     *          Constants of class Greedy
     **********************************************************
     */
    
	/** */
	private final static int EMPTY    = 0;  // empty cell
	/** */
	private final static int OBST     = 1;  // cell with obstacle
	/** */
	private final static int ROBOT    = 2;  // the position of the robot
	 /** */
	private final static int TARGET   = 3;  // the position of the target
	/** */
	private final static int ROUTE    = 6;  // cells that form the robot-to-target path
   /** */
	boolean b;
    /** */
	int[][] grid;        // the grid
    /** */
	boolean found;       // flag that the goal was found
    /** */
	boolean searching;   // flag that the search is in progress
    /** */
	boolean endOfSearch; // flag that the search came to an end
    /** */
	int delay = 0;           // time delay of animation (in msec)
    /** */
	int expanded;        // the number of nodes that have been expanded
    /** */
	int boardWidth ;	//Witdh of the board
    /** */
	int rows ;           // the number of rows of the grid
    /** */
	int columns ;           // the number of columns of the grid
    /** */
    int squareSize;  // the cell size in pixels
    /** */
    int arrowSize = squareSize/2; // the size of the tip of the arrow
                                      // pointing the predecessor cell
    /** */
	ArrayList<CellCurrent> openSet   = new ArrayList<CellCurrent>();// the OPEN SET
    /** */
	ArrayList<CellCurrent> closedSet = new ArrayList<CellCurrent>();// the CLOSED SET
    /** */
	ArrayList<CellCurrent> graph     = new ArrayList<CellCurrent>();// the set of vertices of the graph
                                                    // to be explored by Dijkstra's algorithm
    /** */
	CellCurrent robotStart; // the initial position of the robot
	/** */
	CellCurrent targetPos;  // the position of the target
    
    /**
     * b = true si tu veux tester avec les Grilles.
     * b = false si tu veux tester avec les pixels
     * @param boardWidth ...
     * @param b  ...
     * */
    public Greedy(int boardWidth, boolean b/*, int rows, int columns*/){
    	this.b = b;
    	this.rows = AgentImpl.GRID_ROWS;
    	this.columns = AgentImpl.GRID_COLS;
    	//this.rows = rows;
    	//this.columns = columns;
    	this.boardWidth = boardWidth;
    	grid = new int[rows][columns];
    	squareSize = boardWidth/rows;
    	arrowSize = squareSize/2;
    	
    }
    
    
    /**
     * Auxiliary class that specifies that the cells will be sorted
         * according their 'f' field
     */
    private class CellComparatorByF implements Comparator<CellCurrent>{
        @Override
        public int compare(CellCurrent cell1, CellCurrent cell2){
            return cell1.f-cell2.f;
        }
    } // end nested class CellComparatorByF
    
    /**
     * Auxiliary class that specifies that the cells will be sorted
         * according their 'dist' field
     */
    @SuppressWarnings("unused")
	private class CellComparatorByDist implements Comparator<CellCurrent>{
        @Override
        public int compare(CellCurrent cell1, CellCurrent cell2){
            return cell1.dist-cell2.dist;
        }
    } // end nested class CellComparatorByDist
    
    
    
    

    /**
     * Expands a node and creates his successors DIAGONAAAAAAAAAAAAAAL
     */
   /* private void expandNode(){
      
            CellCurrent current;
                // Here is the 3rd step of the algorithms A* and Greedy
                // 3. Remove the first state, Si, from OPEN SET,
                // for which f(Si) ≤ f(Sj) for all other
                // open states Sj  ...
                // (sort first OPEN SET list with respect to 'f')
                Collections.sort(openSet, new CellComparatorByF());
                current = openSet.remove(0);
                
            // ... and add it to CLOSED SET.
            closedSet.add(0,current);
            // If the selected node is the target ...
            if (current.row == targetPos.row && current.col == targetPos.col) {
                // ... then terminate etc
                CellCurrent last = targetPos;
                last.prev = current.prev;
                closedSet.add(last);
                found = true;
                return;
            }
            // Count nodes that have been expanded.
            expanded++;
            // Here is the 4rd step of the algorithms
            // 4. Create the successors of Si, based on actions
            //    that can be implemented on Si.
            //    Each successor has a pointer to the Si, as its predecessor.
            //    In the case of DFS and BFS algorithms, successors should not
            //    belong neither to the OPEN SET nor the CLOSED SET.
            ArrayList<CellCurrent> succesors;
            succesors = createSuccesors(current, false);
            // Here is the 5th step of the algorithms
            // 5. For each successor of Si, ...
            for (CellCurrent cell: succesors){
                    // ... calculate the value f(Sj) ...
                    int dxg = current.col-cell.col;
                    int dyg = current.row-cell.row;
                    int dxh = targetPos.col-cell.col;
                    int dyh = targetPos.row-cell.row;
                        // with diagonal movements 
                        // calculate 1000 times the Euclidean distance
                            cell.g = 0;
                        
                        cell.h = (int)((double)1000*Math.sqrt(dxh*dxh + dyh*dyh));
                    
                    cell.f = cell.g+cell.h;
                    // ... If Sj is neither in the OPEN SET nor in the CLOSED SET states ...
                    int openIndex   = isInList(openSet,cell);
                    int closedIndex = isInList(closedSet,cell);
                    if (openIndex == -1 && closedIndex == -1) {
                        // ... then add Sj in the OPEN SET ...
                        // ... evaluated as f(Sj)
                        openSet.add(cell);
                    // Else ...
                    } else {
                        // ... if already belongs to the OPEN SET, then ...
                        if (openIndex > -1){
                            // ... compare the new value assessment with the old one. 
                            // If old <= new ...
                            if (openSet.get(openIndex).f <= cell.f) {
                                // ... then eject the new node with state Sj.
                                // (ie do nothing for this node).
                            // Else, ...
                            } else {
                                // ... remove the element (Sj, old) from the list
                                // to which it belongs ...
                                openSet.remove(openIndex);
                                // ... and add the item (Sj, new) to the OPEN SET.
                                openSet.add(cell);
                            }
                        // ... if already belongs to the CLOSED SET, then ...
                        } else {
                            // ... compare the new value assessment with the old one. 
                            // If old <= new ...
                            if (closedSet.get(closedIndex).f <= cell.f) {
                                // ... then eject the new node with state Sj.
                                // (ie do nothing for this node).
                            // Else, ...
                            } else {
                                // ... remove the element (Sj, old) from the list
                                // to which it belongs ...
                                closedSet.remove(closedIndex);
                                // ... and add the item (Sj, new) to the OPEN SET.
                                openSet.add(cell);
                              
                            }
                        }
                    }
                
            }
        
    } //end expandNode()
    */
    
    
    /**
     * Expands a node and creates his successors NON DIAGONAAAAAAAAL
     */
    private void expandNode(){
   
            CellCurrent current;
           
                // Here is the 3rd step of the algorithms A* and Greedy
                // 3. Remove the first state, Si, from OPEN SET,
                // for which f(Si) ≤ f(Sj) for all other
                // open states Sj  ...
                // (sort first OPEN SET list with respect to 'f')
                Collections.sort(openSet, new CellComparatorByF());
                current = openSet.remove(0);
            
            // ... and add it to CLOSED SET.
            closedSet.add(0,current);
            // Update the color of the cell
            // If the selected node is the target ...
            if (current.row == targetPos.row && current.col == targetPos.col) {
                // ... then terminate etc
                CellCurrent last = targetPos;
                last.prev = current.prev;
                closedSet.add(last);
                found = true;
                return;
            }
            // Count nodes that have been expanded.
            expanded++;
            // Here is the 4rd step of the algorithms
            // 4. Create the successors of Si, based on actions
            //    that can be implemented on Si.
            //    Each successor has a pointer to the Si, as its predecessor.
            //    In the case of DFS and BFS algorithms, successors should not
            //    belong neither to the OPEN SET nor the CLOSED SET.
            ArrayList<CellCurrent> succesors;
            succesors = createSuccesors(current, false);
            // Here is the 5th step of the algorithms
            // 5. For each successor of Si, ...
            for (CellCurrent cell: succesors){
                // ... if we are running DFS ...
               
                    // ... calculate the value f(Sj) ...
//                    int dxg = current.col-cell.col;
//                    int dyg = current.row-cell.row;
                    int dxh = targetPos.col-cell.col;
                    int dyh = targetPos.row-cell.row;
                    
                            // especially for the Greedy ...
                            cell.g = 0;
                        
                        cell.h = Math.abs(dxh)+Math.abs(dyh);
                    
                    cell.f = cell.g+cell.h;
                    // ... If Sj is neither in the OPEN SET nor in the CLOSED SET states ...
                    int openIndex   = isInList(openSet,cell);
                    int closedIndex = isInList(closedSet,cell);
                    if (openIndex == -1 && closedIndex == -1) {
                        // ... then add Sj in the OPEN SET ...
                        // ... evaluated as f(Sj)
                        openSet.add(cell);
                    // Else ...
                    } else {
                        // ... if already belongs to the OPEN SET, then ...
                        if (openIndex > -1){
                            // ... compare the new value assessment with the old one. 
                            // If old <= new ...
                            if (openSet.get(openIndex).f <= cell.f) {
                                // ... then eject the new node with state Sj.
                                // (ie do nothing for this node).
                            // Else, ...
                            } else {
                                // ... remove the element (Sj, old) from the list
                                // to which it belongs ...
                                openSet.remove(openIndex);
                                // ... and add the item (Sj, new) to the OPEN SET.
                                openSet.add(cell);
                                // Update the color of the cell
                            }
                        // ... if already belongs to the CLOSED SET, then ...
                        } else {
                            // ... compare the new value assessment with the old one. 
                            // If old <= new ...
                            if (closedSet.get(closedIndex).f <= cell.f) {
                                // ... then eject the new node with state Sj.
                                // (ie do nothing for this node).
                            // Else, ...
                            } else {
                                // ... remove the element (Sj, old) from the list
                                // to which it belongs ...
                                closedSet.remove(closedIndex);
                                // ... and add the item (Sj, new) to the OPEN SET.
                                openSet.add(cell);
                            }
                        }
                    }
                
            }
        
    } //end expandNode()
    
    
    
    
    
    
    
    
    /**
     * Creates the successors of a state/cell DIAGONAAAAAAAAAAAL
     * 
     * @param current       the cell for which we ask successors
     * @param makeConnected flag that indicates that we are interested only on the coordinates
     *                      of cells and not on the label 'dist' (concerns only Dijkstra's)
     * @return              the successors of the cell as a list
     */
   /* private ArrayList<CellCurrent> createSuccesors(CellCurrent current, boolean makeConnected){
        int r = current.row;
        int c = current.col;
        // We create an empty list for the successors of the current cell.
        ArrayList<CellCurrent> temp = new ArrayList<>();
        // With diagonal movements priority is:
        // 1: Up 2: Up-right 3: Right 4: Down-right
        // 5: Down 6: Down-left 7: Left 8: Up-left
        
        // Without diagonal movements the priority is:
        // 1: Up 2: Right 3: Down 4: Left
        
        // If not at the topmost limit of the grid
        // and the up-side cell is not an obstacle ...
        if (r > 0 && grid[r-1][c] != OBST) {
            CellCurrent cell = new CellCurrent(r-1,c);
            // In the case of Dijkstra's algorithm we can not append to
            // the list of successors the "naked" cell we have just created.
            // The cell must be accompanied by the label 'dist',
            // so we need to track it down through the list 'graph'
            // and then copy it back to the list of successors.
            // The flag makeConnected is necessary to be able
            // the present method createSuccesors() to collaborate
            // with the method findConnectedComponent(), which creates
            // the connected component when Dijkstra's initializes.
           
                // ... update the pointer of the up-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the up-side cell to the successors of the current one. 
                temp.add(cell);
             
        }
        
            // If we are not even at the topmost nor at the rightmost border of the grid
            // and the up-right-side cell is not an obstacle ...
            if (r > 0 && c < columns-1 && grid[r-1][c+1] != OBST &&
                    // ... and one of the upper side or right side cells are not obstacles ...
                    // (because it is not reasonable to allow 
                    // the robot to pass through a "slot")                        
                    (grid[r-1][c] != OBST || grid[r][c+1] != OBST)) {
                		CellCurrent cell = new CellCurrent(r-1,c+1);
                
                    // ... update the pointer of the up-right-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the up-right-side cell to the successors of the current one. 
                    temp.add(cell);
                
            }
        
        // If not at the rightmost limit of the grid
        // and the right-side cell is not an obstacle ...
        if (c < columns-1 && grid[r][c+1] != OBST ) {
            CellCurrent cell = new CellCurrent(r,c+1);
            
                // ... update the pointer of the right-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the right-side cell to the successors of the current one. 
                temp.add(cell);
            
        }
        
            // If we are not even at the lowermost nor at the rightmost border of the grid
            // and the down-right-side cell is not an obstacle ...
            if (r < rows-1 && c < columns-1 && grid[r+1][c+1] != OBST &&
                    // ... and one of the down-side or right-side cells are not obstacles ...
                    (grid[r+1][c] != OBST || grid[r][c+1] != OBST)) {
                CellCurrent cell = new CellCurrent(r+1,c+1);
                
                    // ... update the pointer of the downr-right-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the down-right-side cell to the successors of the current one. 
                    temp.add(cell);
                
            }
        
        // If not at the lowermost limit of the grid
        // and the down-side cell is not an obstacle ...
        if (r < rows-1 && grid[r+1][c] != OBST) {
            CellCurrent cell = new CellCurrent(r+1,c);
            
               // ... update the pointer of the down-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the down-side cell to the successors of the current one. 
                temp.add(cell);
            
        }
        
            // If we are not even at the lowermost nor at the leftmost border of the grid
            // and the down-left-side cell is not an obstacle ...
            if (r < rows-1 && c > 0 && grid[r+1][c-1] != OBST &&
                    // ... and one of the down-side or left-side cells are not obstacles ...
                    (grid[r+1][c] != OBST || grid[r][c-1] != OBST)) {
                CellCurrent cell = new CellCurrent(r+1,c-1);
               
                    // ... update the pointer of the down-left-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the down-left-side cell to the successors of the current one. 
                    temp.add(cell);
                
            
        }
        // If not at the leftmost limit of the grid
        // and the left-side cell is not an obstacle ...
        if (c > 0 && grid[r][c-1] != OBST ) {
            CellCurrent cell = new CellCurrent(r,c-1);
            
               // ... update the pointer of the left-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the left-side cell to the successors of the current one. 
                temp.add(cell);
            
        }
        
            // If we are not even at the topmost nor at the leftmost border of the grid
            // and the up-left-side cell is not an obstacle ...
            if (r > 0 && c > 0 && grid[r-1][c-1] != OBST &&
                    // ... and one of the up-side or left-side cells are not obstacles ...
                    (grid[r-1][c] != OBST || grid[r][c-1] != OBST)) {
                CellCurrent cell = new CellCurrent(r-1,c-1);
                
                    // ... update the pointer of the up-left-side cell so it points the current one ...
                    cell.prev = current;
                    // ... and add the up-left-side cell to the successors of the current one. 
                    temp.add(cell);
                
            }
        
        // When DFS algorithm is in use, cells are added one by one at the beginning of the
        // OPEN SET list. Because of this, we must reverse the order of successors formed,
        // so the successor corresponding to the highest priority, to be placed
        // the first in the list.
        // For the Greedy, A* and Dijkstra's no issue, because the list is sorted
        // according to 'f' or 'dist' before extracting the first element of.
       
        return temp;
    } */// end createSuccesors()
    
    
    
    
    /**
     * Creates the successors of a state/cell  nooooooon DIAAAAAGOOOOOOONAL
     * 
     * @param current       the cell for which we ask successors
     * @param makeConnected flag that indicates that we are interested only on the coordinates
     *                      of cells and not on the label 'dist' (concerns only Dijkstra's)
     * @return              the successors of the cell as a list
     */
    private ArrayList<CellCurrent> createSuccesors(CellCurrent current, boolean makeConnected){
        int r = current.row;
        int c = current.col;
        // We create an empty list for the successors of the current cell.
        ArrayList<CellCurrent> temp = new ArrayList<>();
        // With diagonal movements priority is:
        // 1: Up 2: Up-right 3: Right 4: Down-right
        // 5: Down 6: Down-left 7: Left 8: Up-left
        
        // Without diagonal movements the priority is:
        // 1: Up 2: Right 3: Down 4: Left
        
        // If not at the topmost limit of the grid
        // and the up-side cell is not an obstacle ...
        if (r > 0 && grid[r-1][c] != OBST  ? true :
                      isInList(openSet,new CellCurrent(r-1,c)) == -1 &&
                      isInList(closedSet,new CellCurrent(r-1,c)) == -1) {
            CellCurrent cell = new CellCurrent(r-1,c);
            // In the case of Dijkstra's algorithm we can not append to
            // the list of successors the "naked" cell we have just created.
            // The cell must be accompanied by the label 'dist',
            // so we need to track it down through the list 'graph'
            // and then copy it back to the list of successors.
            // The flag makeConnected is necessary to be able
            // the present method createSuccesors() to collaborate
            // with the method findConnectedComponent(), which creates
            // the connected component when Dijkstra's initializes.
            
                // ... update the pointer of the up-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the up-side cell to the successors of the current one. 
                temp.add(cell);
             
        }
        
        // If not at the rightmost limit of the grid
        // and the right-side cell is not an obstacle ...
        if (c < columns-1 && grid[r][c+1] != OBST ? true :
                      isInList(openSet,new CellCurrent(r,c+1)) == -1 &&
                      isInList(closedSet,new CellCurrent(r,c+1)) == -1) {
            CellCurrent cell = new CellCurrent(r,c+1);
           
                // ... update the pointer of the right-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the right-side cell to the successors of the current one. 
                temp.add(cell);
            
        }
        // If not at the lowermost limit of the grid
        // and the down-side cell is not an obstacle ...
        if (r < rows-1 && grid[r+1][c] != OBST  ? true :
                      isInList(openSet,new CellCurrent(r+1,c)) == -1 &&
                      isInList(closedSet,new CellCurrent(r+1,c)) == -1) {
            CellCurrent cell = new CellCurrent(r+1,c);
            
               // ... update the pointer of the down-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the down-side cell to the successors of the current one. 
                temp.add(cell);
            
        }
        
        // If not at the leftmost limit of the grid
        // and the left-side cell is not an obstacle ...
        if (c > 0 && grid[r][c-1] != OBST  ? true :
                      isInList(openSet,new CellCurrent(r,c-1)) == -1 &&
                      isInList(closedSet,new CellCurrent(r,c-1)) == -1) {
            CellCurrent cell = new CellCurrent(r,c-1);
           
               // ... update the pointer of the left-side cell so it points the current one ...
                cell.prev = current;
                // ... and add the left-side cell to the successors of the current one. 
                temp.add(cell);
            
        }
      
      
        
        return temp;
    } // end createSuccesors()
    
    
    
    
    
    
    
    
    /**
     * Returns the index of the cell 'current' in the list 'list'
     *
     * @param list    the list in which we seek
     * @param current the cell we are looking for
     * @return        the index of the cell in the list
     *                if the cell is not found returns -1
     */
    private int isInList(ArrayList<CellCurrent> list, CellCurrent current){
        int index = -1;
        for (int i = 0 ; i < list.size(); i++) {
            if (current.row == list.get(i).row && current.col == list.get(i).col) {
                index = i;
                break;
            }
        }
        return index;
    } // end isInList()
   
    /**
     * Returns the predecessor of cell 'current' in list 'list'
     *
     * @param list      the list in which we seek
     * @param current   the cell we are looking for
     * @return          the predecessor of cell 'current'
     */
    @SuppressWarnings("unused")
	private CellCurrent findPrev(ArrayList<CellCurrent> list, CellCurrent current){
        int index = isInList(list, current);
        return list.get(index).prev;
    } // end findPrev()
    
    /**
     * Returns the distance between two cells
     *
     * @param u the first cell
     * @param v the other cell
     * @return  the distance between the cells u and v
     */
   /* private int distBetween(CellCurrent u, CellCurrent v){
        int dist;
        int dx = u.col-v.col;
        int dy = u.row-v.row;
            // with diagonal movements 
            // calculate 1000 times the Euclidean distance
            dist = (int)((double)1000*Math.sqrt(dx*dx + dy*dy));
        
        return dist;
    }*/ // end distBetween()
    
    
    
    /**
     * Returns the distance between two cells
     *
     * @param u the first cell
     * @param v the other cell
     * @return  the distance between the cells u and v
     */
    @SuppressWarnings("unused")
	private int distBetween(CellCurrent u, CellCurrent v){
        int dist;
        int dx = u.col-v.col;
        int dy = u.row-v.row;
       
            // without diagonal movements
            // calculate Manhattan distances
            dist = Math.abs(dx)+Math.abs(dy);
        
        return dist;
    } // end distBetween()
    
    /**
         * Calculates the path from the target to the initial position
         * of the robot, counts the corresponding steps
         * and measures the distance traveled.
		 * Diaaagonnnnal
         */
    /*private void plotRoute(){
        searching = false;
        endOfSearch = true;
        int steps = 0;
        double distance = 0;
        int index = isInList(closedSet,targetPos);
        CellCurrent cur = closedSet.get(index);
        grid[cur.row][cur.col]= TARGET;
        do {
            steps++;
                int dx = cur.col-cur.prev.col;
                int dy = cur.row-cur.prev.row;
                distance += Math.sqrt(dx*dx + dy*dy);
            
            cur = cur.prev;
            grid[cur.row][cur.col] = ROUTE;
        } while (!(cur.row == robotStart.row && cur.col == robotStart.col));
        grid[robotStart.row][robotStart.col]=ROBOT;
        String msg;
        System.out.println("\nSteps :"+steps+", Node Expands :"+expanded);
      
    } */// end plotRoute()
    
    
    	   /**
    	    * Non Diagooooooooonal
            * Calculates the path from the target to the initial position
            * of the robot, counts the corresponding steps
            * and measures the distance traveled.
            */
            private void plotRoute(){
                searching = false;
                endOfSearch = true;
                int steps = 0;
//                double distance = 0;
                int index = isInList(closedSet,targetPos);
                CellCurrent cur = closedSet.get(index);
                grid[cur.row][cur.col]= TARGET;
                do {
                    steps++;
                     
//                        distance++;
                    
                    cur = cur.prev;
                    grid[cur.row][cur.col] = ROUTE;
                } while (!(cur.row == robotStart.row && cur.col == robotStart.col));
                grid[robotStart.row][robotStart.col]=ROBOT;
//                String msg = String.format("Nodes expanded: %d, Steps: %d, Distance: %.3f",expanded,steps,distance); 
                System.out.println("\nSteps :"+steps+", Node Expands :"+expanded);
              
            } // end plotRoute()
            /**
             * 
             */
    private void fillGrid() {
       searching = false;
        
       robotStart.g = 0;
       robotStart.h = 0;
       robotStart.f = 0;
        
       expanded = 0;
       found = false;
       searching = false;
       endOfSearch = false;
        
        openSet.removeAll(openSet);
        openSet.add(robotStart);
        closedSet.removeAll(closedSet);
        grid[targetPos.row][targetPos.col] = TARGET; 
        grid[robotStart.row][robotStart.col] = ROBOT;
        
    }
    
    //Vider la Grille des obstacles
    /**
     * 
     */
    public void emptyObstacle(){
    	for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
            	if (grid[r][c] == OBST){
            		grid[r][c] = EMPTY;
            	}
            }
        }
    }
    
    //Ajouter des obstacles
    /**
     * 
     * @param x ...
     * @param y ...
     */
    public void setObstacle(int x, int y){
    	grid[y][x] = OBST;
    }
    
    /***
     * 
     * @param x ...
     * @param y ...
     */
    public void unsetObstacle(int x, int y){
    	grid[y][x] = EMPTY;
    }
    
    //Chercher un chemin
    /***
     * 
     * @param xSource ...
     * @param ySource ...
     * @param xTarget ...
     * @param yTarget ...
     * @return ...
     */
    public List<Point> path(int xSource, int ySource, int xTarget ,int yTarget){
    	if(b){
	    	robotStart = new CellCurrent(rangGreed(ySource),rangGreed(xSource));
	        targetPos = new CellCurrent(rangGreed(yTarget),rangGreed(xTarget));
    	}else{
    		robotStart = new CellCurrent(ySource,xSource);
	        targetPos = new CellCurrent(yTarget,xTarget);
    	}
    		
        fillGrid();
    	searching = true;
//        boolean end = false;
    	while(true){
	        if (openSet.isEmpty()) {
	            endOfSearch = true;
	            grid[robotStart.row][robotStart.col]=ROBOT;
	            break;
	        } else {
	            expandNode();
	            if (found) {
	            	endOfSearch = true;
	                plotRoute();
	                break;
	            }
	        }
    	}
        return path();
    }
  
  
  
    //Affichage du chemin
    /**
     * 
     * @return ...
     */
   private List<Point> path(){
	   List<Point> cellsPath = new ArrayList<Point>();
    	if(openSet.size() != 0){
			for (int r = 0; r < rows; r++) {
		        for (int c = 0; c < columns; c++) {
		        	if (grid[r][c] == ROUTE) {
		        		cellsPath.add(new Point(c, r));
		            }
		        }
			}
			Collections.reverse(cellsPath);
    		return cellsPath;
    	}else
    		return null;
    }
   
   //Change to Row Grille
   /**
    * 
    * @param y ...
    * @return ...
    */
   public int rangGreed(int y){
	   return((y) / squareSize);
   }
   
    
    
}
