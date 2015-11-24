package fr.univavignon.courbes;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Shift + ctrl + o     to import automaticly 
public class Snake 
{
	private int idPlayer;
	private Position head; 			//Position = (x,y)
	private double direction; 		//angle
	private double ray;				//Size
	private double speed;
	private boolean state;			// 0 = dead 	   ----- 1 = alive
	private boolean crashMode;		// 0 = nocollision ----- 1 = collision   
	private boolean invertMode; 	// 0 = normal keys ----- 1 = invert keys
	private double holeRate;		// % hole apparition
	private boolean planeMode; 		// 0 = not plane   ----- 1 = plane mode
	private HashMap<Item,double> changes;     		// List of Items

	
	public Snake(int id)
	{
		idPlayer 	= id;
		head 		= new Position();
		direction   = -90.0; 		// vers le haut
		ray 		= 3.0;  		// pixel ?
		speed 		= 1.0;			// pixel/frame ?
		state 		= true;
		crashMode 	= true;
		invertMode  = false;
		holeRate 	= 5.0;			// 5% ?
		planeMode  = false;
		changes = new HashMap<Item,double>(50);
	}
	
	/***************************************************************************************************************************************************************\
	/																																							   *\
	/																		GETTERS & SETTERS																	   *\
	/																																							   *\
	/***************************************************************************************************************************************************************/
	public int getID()
	{
		return idPlayer;
	}
	
	public boolean isAlive()
	{
		return state;
	}
	
	public boolean iscrashMode()
	{
		return crashMode;
	}
	
	public boolean isInversed()
	{
		return invertMode;
	}
	
	public boolean isFlying()
	{
		return planeMode;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double rateHoles()
	{
		return holeRate;
	}
	
	public double getSize()
	{
		return ray;
	}
	
	public Position getPosition()
	{
		return head;
	}
	
	/***************************************************************************************************************************************************************\
	/																																							   *\
	/																		ACTIONS																				   *\
	/																																							   *\
	/***************************************************************************************************************************************************************/

	
	public void turnLeft()
	{
		direction--;
	}
	
	public void turnRight()
	{
		direction++;
	}
	
	public void newChange(Changes object)
	{
		// a voir plus tard
	}
	
	public void getChanges()
	{
		// a voir plus tard
	}
}


	
//double axeX1=cos(angle1*PI/180.0);
//double axeY1=sin(angle1*PI/180.0);