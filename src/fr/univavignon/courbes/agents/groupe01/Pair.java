package fr.univavignon.courbes.agents.groupe01;

/**
 * @author uapv1504059
 *
 * @param <U> First type of the pair
 * @param <V> Second type of the pair
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