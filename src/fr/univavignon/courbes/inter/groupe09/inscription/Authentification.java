package fr.univavignon.courbes.inter.groupe09.inscription;
import fr.univavignon.courbes.inter.groupe09.Error;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.menu.Bienvenu;
import fr.univavignon.courbes.inter.groupe09.menu.Menu;
import fr.univavignon.courbes.common.Profile;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;



/** 
 * @author Groupe09
 * Classe correspendant au menu d'authentification afficher au joueur lorsqu'il demande à s'enthetifier
 */
public class Authentification extends Fenetre implements ActionListener {

	/**
	 * 
	 */
	private JLabel userName, password, ath, vide;
	/**
	 * 
	 */
	private JTextField txt1, txt2;
	/**
	 * 
	 */
	private JPanel p1, p2;
	/**
	 * 
	 */
	private JButton b1, b2;
	/**
	 * 
	 */
	public static Error err = new Error();
	Controle c = new Controle();
	boolean client = false;
	Myprofil pro = new Myprofil();
	
	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	public Authentification(String titre, int x, int y, boolean client) 
	
	{
		super(titre, x, y);
		this.client = client;
		p1 = new JPanel();
		p2 = new JPanel();
		b1 = new JButton("Connexion");
		b2 = new JButton("ANNULER");
		ath = new JLabel("AUTHENTIFICATION");
		vide = new JLabel("");
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		contenu.add(p1);
		p1.setLayout(new GridLayout(4, 2, 5, 5));
		userName = new JLabel("userName");
		password = new JLabel("password");
		txt1 = new JTextField();
		txt2 = new JTextField();
		
		p1.add(ath);
		p1.add(vide);
		p1.add(userName);
		p1.add(txt1);
		p1.add(password);;
		p1.add(txt2);
		p1.add(b1);
		p1.add(b2);
		b1.addActionListener(this);
		b2.addActionListener(this);

		setVisible(true);
		setResizable(false);
			
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String nom = event.getActionCommand();
		int rep = c.controleConnexion(txt1.getText(), txt2.getText());
		Error err = new Error();
		switch (nom) {
			case "Connexion":
				switch (rep) {
					case 0: 
						err.displayError("Veuillez saisire votre Username");
						break;
					case 1: 
						user = txt1.getText();
						this.dispose();
						if(client) {
							
							new Menu("JEUX", 500, 500);
							
						}
						else {
							Profile p = new Profile();
							p = pro.getProfile(user);
							clientProfile.add(p);
						}
						break;
					case 2: 
						err.displayError("Username incorrecte");
						break;
					case 3: 
						err.displayError("Veuillez saisire votre Mot de passe");
						break;
					case 4: 
						err.displayError("Mot de passe Incorrecte");
						break;
				}
				break;
			case "ANNULER":
				new Bienvenu("ma fenere", 300, 400);
				this.dispose();
				break;
		}
	}
	
}