package fr.univavignon.courbes.inter.simpleimpl;

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

import javax.swing.JFrame;

import fr.univavignon.courbes.inter.simpleimpl.menus.Menu;

/**
 * 
 * 
 * @author	L3 Info UAPV 2015-16
 */
public class Launcher extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Permet de lancer la première fenêtre.
	 */
	  public static void main(String[] args){

		  	new Menu();
		  
	  }
	
}
