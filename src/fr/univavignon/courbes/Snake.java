package fr.univavignon.courbes;
import java.util.ArrayList;

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
	private ArrayList changes;     // List of Items

	
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
}


//double axeX1=cos(angle1*PI/180.0);
//double axeY1=sin(angle1*PI/180.0);