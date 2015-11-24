package fr.univavignon.courbes.src.mp;

import java.util.ArrayList;

public class Snake {
	
	private int idPlayer;
	private Position head;
	private float direction;
	private double ray; // radius head
	private double speed;
	private boolean state; // isAlive
	private boolean crashmode; // Nocollide
	private boolean invertmode;
	private boolean planemode;
	private double holerate;
	ArrayList changes; 

	public Snake(int id, Position spawnPosition)
	{
		idPlayer 	= id;
		head 		= spawnPosition;
		direction   = 0; 		
		ray 		= 1;  	
		speed 		= 1;		
		state 		= true;
		crashmode 	= false;
		invertmode  = false;
		planemode   = false;
		holerate 	= 1.0;			
		changes = new ArrayList();
	}
	
	public Position getHead() { return head; }
	
	
	/**
	 * @param strength Force sur un un coté ou l'autre du snake
	 */
	public void changeDirection(float strength)
	{
		
	}
	

}

