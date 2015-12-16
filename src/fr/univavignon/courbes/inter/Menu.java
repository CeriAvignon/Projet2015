package fr.univavignon.courbes.inter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import fr.univavignon.courbes.inter.Menu;


public class Menu extends Fenetre{
	/**
	 * @param largeur
	 * @param hauteur
	 */
	public Menu( int largeur, int hauteur, boolean connect){
		super(largeur, hauteur);
		if(connect == true){
			MenuUser();
		}
		else{
			MenuGuest();
		}
	}
	public void MenuUser(){
		setTitle("Profile");
	    JPanel stats = new JPanel(new GridLayout(0,1,1,1));
	    stats.add(new JLabel("Parties : 10", SwingConstants.CENTER));
	    stats.add(new JLabel("Victoires : 8", SwingConstants.CENTER));
	    stats.add(new JLabel("Défaites : 2", SwingConstants.CENTER));
	    stats.add(new JLabel("Ratio : 4", SwingConstants.CENTER));
	    JButton Retour = new JButton("Retour au menu");
	    stats.add(Retour);
		Retour.addActionListener(new RetourListen());
	    container.setLayout(new FlowLayout(FlowLayout.CENTER));
	    container.add(stats);
	    setVisible(true);
	}
	public void MenuGuest(){
		setTitle("Atchung Die Kurv");
		JPanel MenuPrincipal = new JPanel(new BorderLayout());
	    JButton Connexion = new JButton("Connexion");
	    JButton Inscription = new JButton("Inscription");
	    Connexion.addActionListener(new ConnexionListen());
	    Inscription.addActionListener(new InscriptionListen());
	    JPanel text = new JPanel(new GridLayout(0,1,1,1));
	    JTextField login = new JTextField();
	    text.add(new JLabel("Login", SwingConstants.CENTER));	
	    text.add(login); 
	    JTextField password = new JTextField();
	    text.add(new JLabel("Password", SwingConstants.CENTER));
	    text.add(password);  
	    JPanel center = new JPanel(new GridLayout(1,1,1,1));
	    center.add(Connexion);
	    Connexion.setBackground(Color.RED);
	    center.add(Inscription);
	    Inscription.setBackground(Color.RED);
	    center.setBorder(new EmptyBorder(40,40,40,40));
	    MenuPrincipal.add(text, BorderLayout.NORTH);
	    MenuPrincipal.add(center, BorderLayout.CENTER);
	    container.setLayout(new FlowLayout(FlowLayout.CENTER));
	    container.add(MenuPrincipal);
	    setVisible(true);
	}
	
	class ConnexionListen implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
			new Menu(400,250,true);	
		}
	}
	
	class InscriptionListen implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			new Inscription();	
		}
		
	}
	class RetourListen implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {  
			setVisible(false);
			new Menu(400,250,false);
		}
	}
}