package fr.univavignon.courbes.userInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Command implements KeyListener{
	public boolean left = false;
	public boolean right = false;

	public void keyPressed(KeyEvent e) { 
		if(e.getKeyCode() == 37) left = true;
		if(e.getKeyCode() == 39) right = true;
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 37) left = false;
		if(e.getKeyCode() == 39) right = false;
	}


	public void keyTyped(KeyEvent e) {

	}


	public void getCommand(){
			if(left == true) System.out.println("gauche");
			if(right == true) System.out.println("droite");			
	}
	
	public void sendCommand(){
		
	}
	
	
}

