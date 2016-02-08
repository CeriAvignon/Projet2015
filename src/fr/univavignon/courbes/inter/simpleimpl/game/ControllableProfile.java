package fr.univavignon.courbes.inter.simpleimpl.game;

/*
 * Courbes
 * Copyright 2015-16 L3 Info UAPV 2015-16
 * 
 * This file is part of Courbes.
 * 
 * Courbes is free software: you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License as published by the Free Software Foundation, 
 * either version 2 of the License, or (at your option) any later version.
 * 
 * Courbes is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Courbes. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.event.KeyEvent;

import fr.univavignon.courbes.common.Profile;
import fr.univavignon.courbes.inter.simpleimpl.data.PrintableProfile;

/**
 * 
 * 
 * @author	L3 Info UAPV 2015-16
 */
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
