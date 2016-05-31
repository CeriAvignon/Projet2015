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

/**
  * Helper class that represents the cell of the grid.
  */
public  class CellCurrent {
	int row;   // the row number of the cell(row 0 is the top)
    int col;   // the column number of the cell (Column 0 is the left)
    int g;     // the value of the function g of Greedy algorithms
    int h;     // the value of the function h of Greedy algorithms
    int f;     // the value of the function h of Greedy algorithms
    int dist;  // the distance of the cell from the initial position of the robot
    // Ie the label that updates the Dijkstra's algorithm
    CellCurrent prev; // Each state corresponds to a cell
                       // and each state has a predecessor which
                       // is stored in this variable
    public CellCurrent(int row, int col){
               this.row = row;
               this.col = col;
            }
        } // end nested class Cell
