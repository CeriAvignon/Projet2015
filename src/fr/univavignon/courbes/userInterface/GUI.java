package fr.univavignon.courbes.userInterface;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;


/**
 * Classe permettant d'initialiser la fenêtre principale ainsi que la classe Menu
 * 
 * @author Florian DEMOL - Alexis MASSIA
 */

public class GUI extends JFrame implements WindowListener{

	static GUI fenetre;
	Menu menu;
	


	public static void main(String[] args) {
		fenetre = new GUI();
		fenetre.initMenu();
	}

	public GUI(){

		addWindowListener(this);
		this.setTitle("Curve Fever");
		this.setResizable(false);
		this.setSize(1280, 720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setVisible(true);
		
	}       




	public void initMenu()
	{
		menu = new Menu(this);
		menu.afficherMenuPrincipal();
		fenetre.add(menu,BorderLayout.CENTER);
		fenetre.revalidate();
		fenetre.repaint();
	}
	
	


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {


	}
}



