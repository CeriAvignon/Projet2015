package fr.univavignon.courbes.inter.groupe17;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Classe récupérant les commandes des utilisateurs
 * @author Florian DEMOL - Alexis MASSIA
 */


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
		
	}
	
	public void sendCommand(){
		
	}
	
	
}
