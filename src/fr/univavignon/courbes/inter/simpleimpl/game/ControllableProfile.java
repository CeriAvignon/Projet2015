package fr.univavignon.courbes.inter.simpleimpl.game;

import java.awt.event.KeyEvent;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.data.PrintableProfile;

public class ControllableProfile extends PrintableProfile{
	
	private KeyEvent left;
	private KeyEvent right;
	
	public KeyEvent getLeft() {
		return left;
	}
	public void setLeft(KeyEvent left) {
		this.left = left;
	}
	public KeyEvent getRight() {
		return right;
	}
	public void setRight(KeyEvent right) {
		this.right = right;
	}
	
	public int getLeftKeyCode(){
		return left == null ? -1 : left.getKeyCode();
	}
	
	public int getRightKeyCode(){
		return right == null ? -1 : right.getKeyCode();
	}

}
