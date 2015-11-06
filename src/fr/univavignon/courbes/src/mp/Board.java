package fr.univavignon.courbes.src.mp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
	
	HashMap<Position, String> mapTrace = new HashMap<Position, String>(); 
	HashMap<Position, String> mapItem = new HashMap<Position, String>();
	
	public ArrayList<Snake> listSnake = new ArrayList<Snake>();
	
}
