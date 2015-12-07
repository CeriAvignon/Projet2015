<<<<<<< HEAD:src/fr/univavignon/courbes/common/Round.java
package fr.univavignon.courbes;

public class Round 
{
	private Board game;
	private Item item;
	
	
	public Round()
	{
		game = new Board();
		item = new Item();
	}
}
=======
package fr.univavignon.courbes;

public class Round 
{
	private Board game;
	private Item item;
	
	
	public Round()
	{
		game = new Board();
		item = new Item();
	}
	
	public void roundInit(Board board)
	{
		// initialise les données du moteur physique
		// en utilisant board
	}
	
	
	public void roundUpdate(long elapsedTime)
	{
		// maj suivant le temps
	}
	
	public void forceUpdate(Board board)
	{
		// remplace le board actuel par celui en parametre
	}
}
>>>>>>> 7c32b7e412d00ee50a38af320ead8d73b18e127f:src/fr/univavignon/courbes/Round.java
