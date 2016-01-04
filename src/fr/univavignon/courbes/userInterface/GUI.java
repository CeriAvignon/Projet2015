package fr.univavignon.courbes.userInterface;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class GUI extends JFrame implements WindowListener{

	static GUI fenetre;
	Menu menu;
	//MenuLancerPartie menulancerpartie;
	


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
	
	/*public void initMenuLancerPartie()
	{
		fenetre.remove(menu);
		menulancerpartie = new MenuLancerPartie(this);
		menulancerpartie.MenuLancerUnePartie();
		fenetre.add(menulancerpartie,BorderLayout.CENTER);
		fenetre.revalidate();
		fenetre.repaint();
	}*/
	


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


