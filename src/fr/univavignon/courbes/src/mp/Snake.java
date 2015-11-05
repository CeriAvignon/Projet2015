package fr.univavignon.courbes.src.mp;

import java.util.ArrayList;

public class Snake {
	int idPlayer;
	Position head;
	float direction;
	double ray; // radius head
	double speed;
	boolean state; // isAlive
	boolean crashmode; // Nocollide
	boolean invertmode;
	boolean planemode;
	double holerate;
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

