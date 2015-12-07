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
