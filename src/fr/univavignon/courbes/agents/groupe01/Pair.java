package fr.univavignon.courbes.agents.groupe01;

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
 *
 * @param <U> First type of the pair
 * @param <V> Second type of the pair
 * 
 * @author Maxime Dapp 
 * @author Pierre Fageot
 * @author Quentin Roux
 * @author Gaetan Schmidt
 */
public class Pair<U, V> {

	 /**
	     * The first element of this <code>Pair</code>
	     */
	    private U first;

	    /**
	     * The second element of this <code>Pair</code>
	     */
	    private V second;

	    /**
	     * Constructs a new <code>Pair</code> with the given values.
	     * 
	     * @param first  the first element
	     * @param second the second element
	     */
	    public Pair(U first, V second) {

	        this.first = first;
	        this.second = second;
	    }
	    
	    /**
	     * Constructeur par defaut
	     */
	    public Pair() {
	    	
	    }
	    
	    /**
	     * @return the first value of the pair
	     */
	    public U getFirst(){
	    	return this.first;
	    }
	    
	    /**
	     * @return the second value of the pair
	     */
	    public V getSecond(){
	    	return this.second;
	    }
}

	//getter for first and second