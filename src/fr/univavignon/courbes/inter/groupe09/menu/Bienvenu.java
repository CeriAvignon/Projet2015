package fr.univavignon.courbes.inter.groupe09.menu;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.inscription.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


	

/**			
 * @author groupe09
 *
 */
public class Bienvenu extends Fenetre implements ActionListener {
	
	/**
	 * 
	 */
	private JButton b1,b2,b3;
	/**
	 * 
	 */
	private JPanel j;
	/**
	 * 
	 */
	private JLabel l;
	
	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	public Bienvenu(String titre, int x, int y) 
	{
		super(titre, x, y);
		j = new JPanel();
		l = new JLabel("!!!!!! BIENVENUE !!!!!!");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		j.setLayout(new GridLayout(4, 0, 50, 25));
		contenu.add(j);
		j.add(l);
		b1 = new JButton("INSCRIPTION");
		b2 = new JButton("AUTHENTIFICATION");
		b3 = new JButton("QUITTER");
		j.add(b1);
		j.add(b2);
		j.add(b3);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		setVisible(true);
		setResizable(false);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String nom = event.getActionCommand();
		switch (nom) {
			case "INSCRIPTION": 
				new Inscription("Inscription", 400, 400);
				this.dispose();
				break;
			case "AUTHENTIFICATION": 
				new Authentification("Authentification", 400, 400);
				this.dispose();
				break;
			case "QUITTER": 
				System.exit(0);
				break;
		}
	}
	public static void main(String[] args) {
	
		// teste des menu du jeu
		Bienvenu m1 = new Bienvenu("Atchung Die Kurv", 300, 250);		
		}

	
}