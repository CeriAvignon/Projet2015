package fr.univavignon.courbes.physics.groupe10;


/**
 * Classe énuméré qui donne un type de collsion
 * <ul>
 * 		<li> BORDER : collision a un bord</li>
 * 		<li> SNAKE : collision avec le corp d'un snake</li>
 * 		<li> ITEM : collison a un item</li>
 * 		<li> NONE : pas de collisions</li>
 * </ul>
 *
 */
public enum Collision {
	
	/**
	 * objet de type 'collision a un bord'
	 */
	BORDER,
	
	/**
	 * objet de type 'collision a au corp d'un snake'
	 */
	SNAKE,
	
	/**
	 * objet de type 'collision a un item'
	 */
	ITEM,
	
	/**
	 * objet de type 'pas de collision'
	 */
	NONE;
	
	

}
