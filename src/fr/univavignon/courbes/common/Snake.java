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
	private boolean crashmode;		// 0 = nocollision ----- 1 = collision   
	private boolean invertmode; 	// 0 = normal keys ----- 1 = invert keys
	private double holerate;		// % hole apparition
	private boolean planemode; 		// 0 = not plane   ----- 1 = plane mode
	private List changes;     		// List of Items

	
	public Snake(int id)
	{
		idPlayer 	= id;
		head 		= new Position();
		direction   = -90.0; 		// vers le haut
		ray 		= 3.0;  		// pixel ?
		speed 		= 1.0;			// pixel/frame ?
		state 		= true;
		crashmode 	= true;
		invertmode  = false;
		holerate 	= 5.0;			// 5% ?
		planemode  = false;
		changes = new ArrayList();
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
	
	public boolean isCrashMode()
	{
		return crashmode;
	}
	
	public boolean isInversed()
	{
		return invertmode;
	}
	
	public boolean isFlying()
	{
		return planemode;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double rateHoles()
	{
		return holerate;
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
		changes.add(object);
	}
	
	public void getChanges()
	{
		if(changes.size() <= 0)
		{
			System.out.println("No changes for now");
		}
		else
		{
			for(int i = 0; i < changes.size(); i++) {   
				System.out.println("something");
			} 			
		}

	}
}


	
//double axeX1=cos(angle1*PI/180.0);
//double axeY1=sin(angle1*PI/180.0);