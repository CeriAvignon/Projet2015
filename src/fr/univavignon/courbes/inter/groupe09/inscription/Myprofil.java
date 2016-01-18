package fr.univavignon.courbes.inter.groupe09.inscription;
import fr.univavignon.courbes.inter.groupe09.Fenetre;
import fr.univavignon.courbes.inter.groupe09.menu.Menu;
import fr.univavignon.courbes.common.Profile;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;


public class Myprofil extends Fenetre implements ActionListener {
	
	/**
	 * 
	 */
	private JLabel myprof,l1 ,email,l2, userName,l3, country,l4, timeZone , noth, score, varscore;
	/**
	 * 
	 */
	private JPanel p1;
	/**
	 * 
	 */
	private JButton b1;
	Profile p = new Profile();
	
	/**
	 * @param titre
	 * Titre de la fenetre
	 * @param x
	 * Taille en abscisse de la fenetre 
	 * @param y
	 * Taille en ordonné de la fenetre
	 */
	public Myprofil() {
		
	}
	public Myprofil(String titre , int x, int y) {
		
		super(titre , x ,y);
		contenu.setLayout(new FlowLayout(FlowLayout.CENTER));
		p1 =  new JPanel();
		contenu.add(p1);
		p1.setLayout(new GridLayout(7, 2, 5, 5));
		myprof = new JLabel("PROFIL ");
		email = new JLabel("email :");
		userName = new JLabel("userName :");
		country = new JLabel("country :");
		timeZone = new JLabel("timeZone :");
		score = new JLabel("meilleurs scores :");
		
		String ligne ="";
		String tab [] = null;
		if (!v) {
			p = getProfile(user);
			
		               	l1 = new JLabel(p.email);
		               	l2 = new JLabel(p.userName);
		               	l3 = new JLabel(p.country);
		               	l4 = new JLabel(p.timeZone);
		               	
		               	varscore = new JLabel(p.score+"");
		               	
		           
		
		}
		else {
			
			l1 = new JLabel((String) profil.get(0));
			l2 = new JLabel((String) profil.get(1));
			l3 = new JLabel((String) profil.get(2));
			l4 = new JLabel((String) profil.get(3));
			varscore = new JLabel((String) profil.get(4));
			
		}
		
		noth =new JLabel();
		b1= new JButton ("return");
		
		p1.add(myprof);
		p1.add(noth);
		p1.add(email);
		p1.add(l1);
		p1.add(userName);
		p1.add(l2);
		p1.add(country);
		p1.add(l3);
		p1.add(timeZone);
		p1.add(l4);
		p1.add(score);
		p1.add(varscore);
		p1.add(b1);
		
		
		b1.addActionListener(this);
		setVisible(true);
		setResizable(false);	
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		new Menu(" Menu ", 400, 400);
		this.dispose();
			
	}
	
	public Profile getProfile(String pass) {
		Profile p = new Profile();
		String ligne = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("src/fr/univavignon/courbes/inter/groupe09/txt/user.txt")));
			while((ligne = reader.readLine()) != null) {
				String el[] = ligne.split(";");
				if (ligne.contains(pass)) {
					
					String lign[] = ligne.split(";");
					p.userName = lign[0];
					p.password = lign[1];
					p.profileId = Integer.parseInt(lign[2]);
					p.email = lign[3];
					p.country = lign[4];
					p.timeZone = lign[5];
					p.score = Integer.parseInt(lign[6]);

	           
				}
	        }
	    } 
		catch (Exception ex) {
			System.err.println("Error. "+ex.getMessage());
	    }
		
		return p;
	}

	
}