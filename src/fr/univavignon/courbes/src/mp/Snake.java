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

	public Snake(int id)
	{
		idPlayer 	= id;
		head 		= new Position(0,0);
		direction   = 0; 		
		ray 		= 1;  	
		speed 		= 1;		
		state 		= true;
		crashmode 	= false;
		invertmode  = false;
		planemode  = false;
		holerate 	= 1;			
		changes = new ArrayList();
	}
}

